package com.xuecheng.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.ucenter.mapper.XcUserMapper;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.model.dto.XcUserExt;
import com.xuecheng.ucenter.model.po.XcUser;
import com.xuecheng.ucenter.service.AuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * @description: 使用密码进行认证
 * @author: Venture
 * @date: 2023-06-17 10:12
 */
@Service("password_authservice")
public class PasswordAuthServiceImpl implements AuthService {
    @Autowired
    XcUserMapper xcUserMapper;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    public XcUserExt execute(AuthParamsDto authParamsDto) {
        String username = authParamsDto.getUsername();
        String password = authParamsDto.getPassword();
        LambdaQueryWrapper<XcUser> lqw = new LambdaQueryWrapper<>();
        lqw.eq(XcUser::getUsername,username);
        XcUser xcUser = xcUserMapper.selectOne(lqw);
        if (xcUser == null){// 返回空表示用户不存在
            throw new RuntimeException("账号不存在");
        }
        boolean matches = passwordEncoder.matches(password, xcUser.getPassword());
        if (!matches)
            throw new RuntimeException("用户名或密码错误");
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        return xcUserExt;
    }
}
