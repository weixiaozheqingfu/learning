package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenOuter;

public interface IAccessToken4RefreshTokenService {

    /**
     * 获取accessToken信息
     */
    AccessTokenOuter getAccessTokenInfo(String client_id, String refresh_token, String grant_type);

}