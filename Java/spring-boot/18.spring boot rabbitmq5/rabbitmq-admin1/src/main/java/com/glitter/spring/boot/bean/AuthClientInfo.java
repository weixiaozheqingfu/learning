package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class AuthClientInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 第三方auth服务平台分配的客户端id */
    private String clientId;
    /** 第三方auth服务平台分配的客户端密码 */
    private String clientSecret;
    /** 第三方auth服务端信息表主键 */
    private Long authServerInfoId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}