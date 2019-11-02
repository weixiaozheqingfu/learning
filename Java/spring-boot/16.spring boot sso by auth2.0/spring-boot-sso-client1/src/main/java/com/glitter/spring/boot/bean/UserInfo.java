package com.glitter.spring.boot.bean;

import lombok.Data;

@Data
public class UserInfo extends BaseBean {

    /** 主键 */
    private Long userId;
    /** 昵称 */
    private String nickName;
    /** 0:未填写,1:男,2:女,3:未知 */
    private Integer sex;
    /** 年龄 */
    private Integer age;
 
}