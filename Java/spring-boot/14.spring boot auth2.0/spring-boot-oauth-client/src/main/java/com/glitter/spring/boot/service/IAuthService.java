package com.glitter.spring.boot.service;


import com.glitter.spring.boot.bean.AuthStateInfo;

public interface IAuthService {

    /**
     * 生成state值
     * @return
     */
    String generateState(AuthStateInfo authStateInfo);

    /**
     * 验证state
     * @param state
     * @param authStateInfo
     * @return
     */
    boolean validateState(String state, AuthStateInfo authStateInfo);

}