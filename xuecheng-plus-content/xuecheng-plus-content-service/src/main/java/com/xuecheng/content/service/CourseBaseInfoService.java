package com.xuecheng.content.service;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author Mr.M
 * @version 1.0
 * @description 课程信息管理接口
 * @date 2023/2/12 10:14
 */
public interface CourseBaseInfoService {

    /**
     * 课程分页查询
     * @param pageParams 分页查询参数
     * @param courseParamsDto 查询条件
     * @return 查询结果
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams pageParams, QueryCourseParamsDto courseParamsDto);

    /**
     * 新增课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 课程详细信息
     */


    public CourseBaseInfoDto createCourseBase(Long companyId,AddCourseDto addCourseDto);

    /**
     * 获取课程信息
     * @param id 课程id
     * @return com.xuecheng.content.model.dto.CourseBaseInfoDto
     */

    public CourseBaseInfoDto getCourseBaseInfo(long id);

    public CourseBaseInfoDto UpdateCourseBaseInfo(Long companyId, EditCourseDto dto);

    /**
     * 删除课程信息,包括课程相关的基本信息、营销信息、课程计划、课程教师信息
     *
     * @param companyId
     * @param courseid
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2023-05-09 17:10
     */

    void deleteCourseBase(Long companyId, Long courseid);
}
