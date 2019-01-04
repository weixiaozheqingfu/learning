package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenInner;
import com.glitter.spring.boot.bean.OauthAccessToken;

public interface IOauthAccessTokenService {


    /**
     * 内部验证accessToken
     */
    AccessTokenInner validateAccessToken(String accessToken);


    OauthAccessToken getOauthAccessToken(String accssToken);

}