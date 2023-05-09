package com.xuecheng.content.service;

import com.xuecheng.content.model.po.CourseTeacher;

import java.util.List;

public interface CourseTeacherService {
    /**
     * 获取课程师资列表
     * @param courseid 课程id
     * @return java.util.List<com.xuecheng.content.model.po.CourseTeacher>
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-08 13:13
     */

    public List<CourseTeacher> queryTeacherList(Long courseid);

    /**
     * 添加师资
     *
     * @param courseTeacher
     * @return com.xuecheng.content.model.po.CourseTeacher
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-08 19:12
     */
    
    CourseTeacher insertTeacher(CourseTeacher courseTeacher);

    /**
     * 更新教师信息
     * @param courseTeacher
     * @return com.xuecheng.content.model.po.CourseTeacher
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-08 19:12
     */
    
    CourseTeacher updateTeacher(CourseTeacher courseTeacher);
    /**
     * 删除指定课程的指定教师
     * @param courseId 课程id
	 * @param teacherId 教师id
     * @return void
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-08 19:12
     */

    void deleteTeacher(Long courseId, Long teacherId);
}
