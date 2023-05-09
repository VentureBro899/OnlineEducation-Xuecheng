package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.base.exception.XueChengPlusException;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import com.xuecheng.content.service.TeachPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @description: TODO
 * @author: Venture
 * @date: 2023-04-27 20:46
 */
@Service
public class TeachPlanServiceImpl implements TeachPlanService {
    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachplanTree(long courseId) {
        List<TeachplanDto> list = teachplanMapper.selectTreeNodes(courseId);
        return list;
    }

    @Override
    public void saveTeachplan(SaveTeachplanDto teachplanDto) {
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getCourseId,teachplanDto.getCourseId()).eq(Teachplan::getParentid,teachplanDto.getParentid());
        if(teachplanDto.getId() != null){ // 修改
            lqw.eq(Teachplan::getId,teachplanDto.getId());
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.update(teachplan,lqw);
        }else{// 新增
            lqw.orderByDesc(Teachplan::getOrderby).last("limit 1");
            Teachplan teachplan = new Teachplan();
            // 排序
            Teachplan lastone = teachplanMapper.selectOne(lqw);
            // 序号，如果前面已经存在节点则在前面节点的序号上+1，否则置为1
            Integer order = lastone != null ? lastone.getOrderby() + 1 : 1;
            teachplan.setOrderby(order);
            BeanUtils.copyProperties(teachplanDto,teachplan);
            teachplanMapper.insert(teachplan);
        }
    }

    @Transactional
    @Override
    public void deleteTeachplan(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        if(teachplan.getParentid() != 0){ // 处理子节点
            teachplanMapper.deleteById(id);
            LambdaQueryWrapper<TeachplanMedia> lqw = new LambdaQueryWrapper<>();
            lqw.eq(TeachplanMedia::getCourseId,teachplan.getCourseId()).eq(TeachplanMedia::getTeachplanId,teachplan.getId());
            // 删除媒资相关信息
            teachplanMediaMapper.delete(lqw);
        }else { // 处理父节点
            LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Teachplan::getParentid,teachplan.getId());
            List<Teachplan> teachplans = teachplanMapper.selectList(lqw);
            if (!teachplans.isEmpty())
                throw new XueChengPlusException("该节点下还存在子节点,请先删除子节点");
            teachplanMapper.deleteById(id);
        }
    }

    @Transactional
    @Override
    public void movedown(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getParentid,teachplan.getParentid()).eq(Teachplan::getCourseId,teachplan.getCourseId()).gt(Teachplan::getOrderby,teachplan.getOrderby()).orderByAsc(Teachplan::getOrderby).last("limit 1");
        Teachplan downone = teachplanMapper.selectOne(lqw);
        if(downone == null)
            return;
        // 交换排序数
        Integer temp = teachplan.getOrderby();
        teachplan.setOrderby(downone.getOrderby());
        downone.setOrderby(temp);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(downone);
        return;
    }

    @Transactional
    @Override
    public void moveup(Long id) {
        Teachplan teachplan = teachplanMapper.selectById(id);
        LambdaQueryWrapper<Teachplan> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Teachplan::getParentid,teachplan.getParentid()).eq(Teachplan::getCourseId,teachplan.getCourseId()).lt(Teachplan::getOrderby,teachplan.getOrderby()).orderByDesc(Teachplan::getOrderby).last("limit 1");
        Teachplan upone = teachplanMapper.selectOne(lqw);
        if(upone == null) // 说明无法再上移，直接返回
            return;
        // 交换排序数
        Integer temp = teachplan.getOrderby();
        teachplan.setOrderby(upone.getOrderby());
        upone.setOrderby(temp);
        teachplanMapper.updateById(teachplan);
        teachplanMapper.updateById(upone);
        return;
    }
}
