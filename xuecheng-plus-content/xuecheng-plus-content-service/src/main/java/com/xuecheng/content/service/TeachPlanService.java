package com.xuecheng.content.service;/*
 * @author: Venture
 * @description: TODO
 */

import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

public interface TeachPlanService {
    /**
     * @description 查询课程计划树型结构
     * @param courseId  课程id
     * @return List<TeachplanDto>
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);

    /**
     * @description 保存课程计划，包括大章节校长姐
     * @param teachplanDto  课程计划信息
     * @return void
     */
    public void saveTeachplan(SaveTeachplanDto teachplanDto);

    /**
     * 删除课程计划
     * @param id
     * @return void
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-05 22:21
     */

    public void deleteTeachplan(Long id);

    /**
     *
     * @param id 课程计划下移
     * @return void
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-05 16:12
     */

    public void movedown(Long id);

    /**
     *
     * @param id 课程计划上移
     * @return void
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-05 16:12
     */
    public void moveup(Long id);


}
