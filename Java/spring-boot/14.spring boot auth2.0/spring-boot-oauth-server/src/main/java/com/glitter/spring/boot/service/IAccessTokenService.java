package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenInfo;

/**
 * 该接口的实现根据grant_type取值的不同可以有多个实现类,调用时使用工厂模式
 */
public interface IAccessTokenService {

    /**
     * 获取accessToken信息
     */
    AccessTokenInfo getAccessTokenInfo(String client_id, String client_secret, String redirect_uri, String code, String grant_type);

}