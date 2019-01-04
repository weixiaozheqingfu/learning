package com.glitter.spring.boot.bean;

import lombok.Data;

import java.util.List;

@Data
public class UserAuthorizationInfo extends BaseBean {

    /** 主键 */
    private Long id;

    /** 客户端名称 */
    private String clientName;

    /** 客户端权限 */
    private List<String> scopeName;
 
}