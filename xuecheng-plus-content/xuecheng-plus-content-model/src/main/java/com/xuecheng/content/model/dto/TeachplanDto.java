package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/*
 * @description: 课程计划树形结构DTO
 * @author: Venture
 * @date: 2023-04-27 19:48
 */
@Data
public class TeachplanDto extends Teachplan {

    // 课程媒资相关信息
    private TeachplanMedia teachplanMedia;
    // 子节点
    List<TeachplanDto> teachPlanTreeNodes;
}
