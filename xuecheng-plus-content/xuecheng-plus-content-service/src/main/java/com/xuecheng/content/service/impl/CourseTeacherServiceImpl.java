package com.xuecheng.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.mapper.CourseTeacherMapper;
import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTeacherServiceImpl implements CourseTeacherService {
    @Autowired
    CourseTeacherMapper courseTeacherMapper;

    @Override
    public List<CourseTeacher> queryTeacherList(Long courseid) {
        LambdaQueryWrapper<CourseTeacher> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CourseTeacher::getCourseId,courseid);
        List<CourseTeacher> courseTeachers = courseTeacherMapper.selectList(lqw);
        return courseTeachers;
    }

    @Override
    public CourseTeacher insertTeacher(CourseTeacher courseTeacher) {
        int insert = courseTeacherMapper.insert(courseTeacher);
        // 直接返回修改对象，插入后会更新自增id
        return courseTeacher;
    }

    @Override
    public CourseTeacher updateTeacher(CourseTeacher courseTeacher) {
        LambdaQueryWrapper<CourseTeacher> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CourseTeacher::getId,courseTeacher.getId()).eq(CourseTeacher::getCourseId,courseTeacher.getCourseId());
        courseTeacherMapper.update(courseTeacher,lqw);
        return courseTeacher;
    }

    @Override
    public void deleteTeacher(Long courseId, Long teacherId) {
        LambdaQueryWrapper<CourseTeacher> lqw = new LambdaQueryWrapper<>();
        lqw.eq(CourseTeacher::getCourseId,courseId).eq(CourseTeacher::getId,teacherId);
        courseTeacherMapper.delete(lqw);
        return;
    }
}
