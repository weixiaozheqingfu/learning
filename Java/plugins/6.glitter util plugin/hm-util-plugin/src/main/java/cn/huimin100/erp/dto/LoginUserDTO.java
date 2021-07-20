package cn.huimin100.erp.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 当前登录用户信息
 *
 * @author limengjun
 * @date 2020/5/25 11:36
 **/
@Data
public class LoginUserDTO implements Serializable{
    /** 用户id */
    private Integer userId;
    /** 用户名称 */
    private String userName;
    /** 用户账号 */
    private String userAccount;
}
