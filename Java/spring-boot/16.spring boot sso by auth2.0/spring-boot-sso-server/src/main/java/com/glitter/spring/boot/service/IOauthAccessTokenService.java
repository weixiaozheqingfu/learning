package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenInParam;
import com.glitter.spring.boot.bean.OauthAccessToken;

import java.util.List;

public interface IOauthAccessTokenService {

    /**
     * 内部验证accessToken
     */
    AccessTokenInParam validateAccessToken(String accessToken);

    OauthAccessToken getOauthAccessToken(String accssToken);

    List<OauthAccessToken> getOauthAccessTokensByJsessionid(String jsessionid);

    void updateJsessionidClientByAccessToken(String accessToken, Long jsessionidClient);

}