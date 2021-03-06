package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;
@Data
public class OauthScopeEnum extends BaseBean {

    /** 主键 */
    private Long id;
    /** 授权范围名称 */
    private String scopeName;
    /** 授权范围描述 */
    private String scopeDesc;
    /** 授权类型 */
    private String grantType;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}