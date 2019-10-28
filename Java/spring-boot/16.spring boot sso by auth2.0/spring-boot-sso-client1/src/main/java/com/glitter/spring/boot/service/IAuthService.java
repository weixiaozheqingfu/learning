package com.glitter.spring.boot.service;


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