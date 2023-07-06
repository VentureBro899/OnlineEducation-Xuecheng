package com.xuecheng.user.model.po;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author venture
 */
@Data
@TableName("oauth_approvals")
public class OauthApprovals implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("userId")
    private String userid;

    @TableField("clientId")
    private String clientid;

    private String scope;

    private String status;

    @TableField("expiresAt")
    private LocalDateTime expiresat;

    @TableField("lastModifiedAt")
    private LocalDateTime lastmodifiedat;


}
