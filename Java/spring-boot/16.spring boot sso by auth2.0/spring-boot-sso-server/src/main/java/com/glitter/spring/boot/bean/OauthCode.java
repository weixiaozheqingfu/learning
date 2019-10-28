package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class OauthCode extends BaseBean {

    /** 主键ID */
    private Long id;
    /** jsessionid */
    private String jsessionId;
    /** 用户id */
    private Long userId;
    /** 应用id */
    private String clientId;
    /** 授权作用域,授权多个作用域用逗号（,）分隔 */
    private String scope;
    /** 接口地址,授权多个接口地址用逗号（,）分隔 */
    private String interfaceUri;
    /** 预授权码 */
    private String code;
    /** 预授权码过期时长,单位秒 */
    private Long expireIn;
    /** 预授权码过期时间 */
    private Date expireTime;
    /** 创建时间 */
    private Date createTime;
    /** 修改时间 */
    private Date updateTime;
 
}