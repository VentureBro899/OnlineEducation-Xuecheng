package com.xuecheng.content.service;
/*
 * @author: Venture
 * @description: 课程预览、发布接口
 */

import com.xuecheng.content.model.dto.CoursePreviewDto;

import java.io.File;

public interface CoursePublishService {
    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * @description 提交审核,将课程基本信息，师资信息，课程计划提交到课程预发布表
     * @param courseId  课程id
     * @return void
     */
    public void commitAudit(Long companyId,Long courseId);


    /**
     * @description 课程发布接口
     * @param companyId 机构id
     * @param courseId 课程id
     * @return void
     */
    public void publish(Long companyId,Long courseId);

    /**
     * @description 课程静态化
     * @param courseId  课程id
     * @return File 静态化文件
     */
    public File generateCourseHtml(Long courseId);
    /**
     * @description 上传课程静态化页面
     * @param file  静态化文件
     * @return void
     */
    public void  uploadCourseHtml(Long courseId,File file);


}
