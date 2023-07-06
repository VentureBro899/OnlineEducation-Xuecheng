package com.xuecheng.user.service.impl;

import com.xuecheng.user.model.po.OauthApprovals;
import com.xuecheng.user.mapper.OauthApprovalsMapper;
import com.xuecheng.user.service.OauthApprovalsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venture
 */
@Slf4j
@Service
public class OauthApprovalsServiceImpl extends ServiceImpl<OauthApprovalsMapper, OauthApprovals> implements OauthApprovalsService {

}
