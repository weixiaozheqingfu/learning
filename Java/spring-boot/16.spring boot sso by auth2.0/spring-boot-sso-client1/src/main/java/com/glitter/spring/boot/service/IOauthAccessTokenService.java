package com.glitter.spring.boot.service;


import com.glitter.spring.boot.bean.OauthAccessToken;

public interface IOauthAccessTokenService {

    /**
     * 创建access_token
     * @param oauthAccessToken
     */
    void create(OauthAccessToken oauthAccessToken);

    /**
     * 修改access_token
     * @param oauthAccessToken
     */
    void modifyById(OauthAccessToken oauthAccessToken);

    /**
     * 根据主键删除access_token
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取access_token
     * @param id
     * @return
     */
    OauthAccessToken getOauthAccessTokenById(Long id);

    /**
     * 根据jsessionid获取access_token
     * @param jsessionid
     * @return
     */
    OauthAccessToken getOauthAccessTokenByJsessionid(String jsessionid);

}