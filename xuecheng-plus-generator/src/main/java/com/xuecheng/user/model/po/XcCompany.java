package com.xuecheng.user.model.po;

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
@TableName("xc_company")
public class XcCompany implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 联系人名称
     */
    private String linkname;

    /**
     * 名称
     */
    private String name;

    private String mobile;

    private String email;

    /**
     * 简介
     */
    private String intro;

    /**
     * logo
     */
    private String logo;

    /**
     * 身份证照片
     */
    private String identitypic;

    /**
     * 工具性质
     */
    private String worktype;

    /**
     * 营业执照
     */
    private String businesspic;

    /**
     * 企业状态
     */
    private String status;


}
