package com.xuecheng.content.service;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description TODO
 * @date 2023/2/12 14:49
 */
public interface CourseCategoryService {
 /**
  * 课程分类树形结构查询
  *
  * @return
  */
 public List<CourseCategoryTreeDto> queryTreeNodes(String id);

 /**
  * 使用子查询实现，速度较慢
  * @param id
  * @return java.util.List<com.xuecheng.content.model.dto.CourseCategoryTreeDto>
  */

 public List<CourseCategoryTreeDto> queryTreeNodesWithChildern(String id);
}
