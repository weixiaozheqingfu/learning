package com.glitter.spring.boot.service;


import com.glitter.spring.boot.bean.AuthStateInfo;

public interface IAuthService {

    /**
     * 生成state值
     * @return
     */
    String generateState(String serverType);

    /**
     * 验证state
     * @param state
     * @param serverType
     * @return
     */
    boolean validateState(String state, String serverType);

}