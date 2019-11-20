package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenOutParam;

public interface IAccessToken4AuthorizationCodeService {

    /**
     * 获取accessToken信息
     */
    AccessTokenOutParam getAccessTokenInfo(String client_id, String client_secret, String redirect_uri, String code, String grant_type);

}