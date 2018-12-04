package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthAccessToken;

public interface IOauthAccessTokenService {

    /**
     * 创建accessToken表; InnoDB free: 488448 kB
     * @param oauthAccessToken
     */
    void create(OauthAccessToken oauthAccessToken);

    /**
     * 修改accessToken表; InnoDB free: 488448 kB
     * @param oauthAccessToken
     */
    void modifyById(OauthAccessToken oauthAccessToken);

    /**
     * 根据主键删除accessToken表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取accessToken表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthAccessToken getOauthAccessTokenById(Long id);

}