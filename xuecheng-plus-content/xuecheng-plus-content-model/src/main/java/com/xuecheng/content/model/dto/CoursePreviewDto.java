package com.xuecheng.content.model.dto;

import lombok.Data;

import java.util.List;

/*
 * @description: 课程预览Dto
 * @author: Venture
 * @date: 2023-05-30 21:34
 */
@Data
public class CoursePreviewDto {
    //课程基本信息,课程营销信息
    CourseBaseInfoDto courseBase;
    //课程计划信息
    List<TeachplanDto> teachplans;

}
