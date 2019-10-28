package com.glitter.spring.boot.bean;

import lombok.Data;
import java.util.Date;

@Data
public class OauthClientRM extends BaseBean {

    /** 主键 */
    private Long id;
    /** 客户端id */
    private String clientId;
    /** 用户id */
    private Long userId;
    /** 用户对外开放id,对当前开发者帐号唯一 */
    private String openId;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}