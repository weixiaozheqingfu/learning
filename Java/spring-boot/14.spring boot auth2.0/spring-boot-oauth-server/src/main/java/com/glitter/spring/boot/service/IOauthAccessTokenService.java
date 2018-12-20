package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthAccessToken;

public interface IOauthAccessTokenService {

    /**
     * 创建accessToken表
     * @param oauthAccessToken
     */
    void create(OauthAccessToken oauthAccessToken);

    /**
     * 修改accessToken表
     * @param oauthAccessToken
     */
    void modifyById(OauthAccessToken oauthAccessToken);

    /**
     * 根据主键删除accessToken
     * @param id
     */
    void deleteById(Long id);

}