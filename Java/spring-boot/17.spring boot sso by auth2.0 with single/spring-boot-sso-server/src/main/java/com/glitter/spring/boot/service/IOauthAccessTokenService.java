package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenInParam;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.constant.GlitterConstants;
import com.glitter.spring.boot.exception.BusinessException;

import java.util.List;

public interface IOauthAccessTokenService {

    /**
     * 验证accessToken是否有效，sso业务场景中，accessToken依附于jsessionid，jsessionid失效，则accessToken也是失效的。
     */
    AccessTokenInParam validateAccessToken(String accessToken);

    /**
     * 验证jsessionid是否有效
     */
    void validateJsessionid(String jsessionid);

    OauthAccessToken getOauthAccessToken(String accssToken);

    List<OauthAccessToken> getOauthAccessTokensByJsessionid(String jsessionid);

    void deleteAccessTokensByJsessionid(String jsessionid);

}