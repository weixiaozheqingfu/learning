package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class AuthTokenInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 用户表主键 */
    private Long userInfoId;
    /** 三方账号id */
    private String thirdAccountId;
    /** access_token */
    private String accessToken;
    /** access_token过期时间 */
    private Long accessExpire;
    /** refresh_token */
    private String refreshToken;
    /** 第三方auth服务平台信息表主键 */
    private Long authServerInfoId;
    /** 可通过auth授权访问第三方资源接口列表(多个以英文逗号分隔) */
    private String authServerInterfaceInfoId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}