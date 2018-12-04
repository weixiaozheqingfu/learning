package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthAccessToken;

public interface IOauthAccessTokenService {

    /**
     * 创建auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param oauthAccessToken
     */
    void create(OauthAccessToken oauthAccessToken);

    /**
     * 修改auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param oauthAccessToken
     */
    void modifyById(OauthAccessToken oauthAccessToken);

    /**
     * 根据主键删除auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthAccessToken getOauthAccessTokenById(Long id);

}