package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AccessTokenInner;

public interface IOauthAccessTokenService {


    /**
     * 验证accessToken
     */
    AccessTokenInner validateAccessToken(String accessToken);

    /**
     * 根据主键删除accessToken
     * @param id
     */
    void deleteById(Long id);

}