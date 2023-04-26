package com.xuecheng.content.service.impl;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.service.CourseCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CourseCategoryServiceImplTest {
    @Autowired
    CourseCategoryService courseCategoryService;

    @Test
    void queryTreeNodes() {
        long start = System.currentTimeMillis();
        courseCategoryService.queryTreeNodes("1");
        long finish = System.currentTimeMillis();
        System.out.println(finish - start );
    }

    @Test
    void queryTreeNodesWithChildern() {
        long start = System.currentTimeMillis();
        List<CourseCategoryTreeDto> list = courseCategoryService.queryTreeNodesWithChildern("1");
        long finish = System.currentTimeMillis();
        System.out.println(finish - start);
    }
}