package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.service.TeachPlanService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @description: 课程计划(课程列表)接口
 * @author: Venture
 * @date: 2023-04-27 17:52
 */
@RestController
@RequestMapping("/teachplan")
public class TechPlanController {
    @Autowired
    TeachPlanService teachPlanService;


    @ApiOperation("查询课程计划树形结构")
    @ApiImplicitParam(value = "courseId",name = "课程Id",required = true,dataType = "Long",paramType = "path")
    @GetMapping("/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId){
        return teachPlanService.findTeachplanTree(courseId);
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping
    public void saveTeachplan( @RequestBody SaveTeachplanDto teachplan){
        teachPlanService.saveTeachplan(teachplan);
    }

    @ApiOperation("课程计划修改")
    @DeleteMapping("/{id}")
    public void deleteTeachplan(@PathVariable Long id){
        teachPlanService.deleteTeachplan(id);
    }


    @ApiOperation("课程计划下移")
    @PostMapping("/movedown/{id}")
    public void movedown( @PathVariable Long id){
        teachPlanService.movedown(id);
    }

    @ApiOperation("课程计划上移")
    @PostMapping("/moveup/{id}")
    public void moveup( @PathVariable Long id){
        teachPlanService.moveup(id);
    }

    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/association/media")
    public void associationMedia(@RequestBody BindTeachplanMediaDto bindTeachplanMediaDto){
        teachPlanService.associationMedia(bindTeachplanMediaDto);
    }


    @ApiOperation(value = "课程计划和媒资信息解绑")
    @DeleteMapping("/association/media/{teachPlanId}/{mediaId}")
    public void deBind(@PathVariable Long teachPlanId,@PathVariable String mediaId){
        teachPlanService.deBind(teachPlanId,mediaId);
    }

}
