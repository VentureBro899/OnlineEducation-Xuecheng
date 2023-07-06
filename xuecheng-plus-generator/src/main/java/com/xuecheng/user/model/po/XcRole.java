package com.xuecheng.user.model.po;

import java.time.LocalDateTime;
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
@TableName("xc_role")
public class XcRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String roleName;

    private String roleCode;

    private String description;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String status;


}
