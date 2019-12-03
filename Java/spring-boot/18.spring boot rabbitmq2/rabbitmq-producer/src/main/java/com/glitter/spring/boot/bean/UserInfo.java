package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo extends BaseBean {

    /** 主键 */
    private Long id;
    /** 账号 */
    private String account;
    /** 密码 */
    private String password;
    /** 手机 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 手机验证标识(0:未认证 1:已认证) */
    private Boolean phoneVerified;
    /** 邮箱验证标识(0:未认证 1:已认证) */
    private Boolean emailVerified;
    /** 姓名 */
    private String fullName;
    /** 昵称 */
    private String nickName;
    /** 年龄 */
    private Integer age;
    /** 身高(单位mm) */
    private Integer height;
    /** 备注 */
    private String remark;
    /** 0:未删除 1：已删除 */
    private Boolean deleteFlag;
    /** 注册时间 */
    private Date registerTime;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
 
}