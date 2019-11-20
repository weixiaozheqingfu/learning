package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenOutParam;

public interface IAccessToken4RefreshTokenService {

    /**
     * 获取accessToken信息
     */
    AccessTokenOutParam getAccessTokenInfo(String client_id, String refresh_token, String grant_type);

}