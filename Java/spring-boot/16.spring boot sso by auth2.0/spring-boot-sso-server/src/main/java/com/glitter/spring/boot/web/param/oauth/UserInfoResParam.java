package com.glitter.spring.boot.web.param.oauth;

import com.glitter.spring.boot.bean.BaseBean;
import lombok.Data;

@Data
public class UserInfoResParam extends BaseBean {

    /** 授权用户唯一标识 */
    private Long userId;
    /** 姓名 */
    private String fullName;
    /** 昵称 */
    private String nickName;
    /** 手机号 */
    private String phone;
    /** 0:未填写,1:男,2:女,3:未知 */
    private Integer sex;
    /** 年龄 */
    private Integer age;

}