package com.xuecheng.content.service;/*
 * @author: Venture
 * @description: TODO
 */

import com.xuecheng.content.model.dto.TeachplanDto;

import java.util.List;

public interface TeachPlanService {
    /**
     * @description 查询课程计划树型结构
     * @param courseId  课程id
     * @return List<TeachplanDto>
     */
    public List<TeachplanDto> findTeachplanTree(long courseId);

}
