package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class DeveloperInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 账号 */
    private String account;
    /** 密码 */
    private String password;
    /** 姓名 */
    private String fullName;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}