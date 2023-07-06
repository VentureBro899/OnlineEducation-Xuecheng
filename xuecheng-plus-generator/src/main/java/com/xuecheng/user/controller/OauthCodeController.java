package com.xuecheng.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xuecheng.user.service.OauthCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venture
 */
@Slf4j
@RestController
@RequestMapping("oauthCode")
public class OauthCodeController {

    @Autowired
    private OauthCodeService  oauthCodeService;
}
