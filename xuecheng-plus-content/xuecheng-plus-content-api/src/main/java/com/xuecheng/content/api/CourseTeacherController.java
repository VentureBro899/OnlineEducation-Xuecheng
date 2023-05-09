package com.xuecheng.content.api;

import com.xuecheng.content.model.po.CourseTeacher;
import com.xuecheng.content.service.CourseTeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @description: 课程师资相关接口
 * @author: Venture
 * @date: 2023-05-08 13:01
 */
@RestController
@RequestMapping("/courseTeacher")
public class CourseTeacherController {
    @Autowired
    CourseTeacherService courseTeacherService;

    @ApiOperation("获取课程师资列表")
    @GetMapping("/list/{courseid}")
    public List<CourseTeacher> list(@PathVariable Long courseid){
        return courseTeacherService.queryTeacherList(courseid);
    }

    @ApiOperation("添加教师")
    @PostMapping
    public CourseTeacher addTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.insertTeacher(courseTeacher);
    }

    @ApiOperation("修改教师信息")
    @PutMapping
    public CourseTeacher modifyTeacher(@RequestBody CourseTeacher courseTeacher){
        return courseTeacherService.updateTeacher(courseTeacher);
    }

    @ApiOperation("删除教师")
    @DeleteMapping("/course/{courseId}/{teacherId}")
    public void DeleteTeacher(@PathVariable Long courseId,@PathVariable Long teacherId){
        courseTeacherService.deleteTeacher(courseId,teacherId);
    }
}
