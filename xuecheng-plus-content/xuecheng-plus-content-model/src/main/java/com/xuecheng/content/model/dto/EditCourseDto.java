package com.xuecheng.content.model.dto;

/*
 * @description: 修改课程信息
 * @author: Venture
 * @date: 2023-04-27 9:31
 */

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EditCourseDto extends AddCourseDto{

    @NotNull(message = "课程id不能为空")
    @ApiModelProperty(value = "课程id", required = true)
    private Long id;
}
