package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AuthTokenInfo;

public interface IAuthTokenInfoService {

    /**
     * 创建auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param authTokenInfo
     */
    void create(AuthTokenInfo authTokenInfo);

    /**
     * 修改auth认证信息表,也是三方账户表; InnoDB free: 488448 kB
     * @param authTokenInfo
     */
    void modifyById(AuthTokenInfo authTokenInfo);

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
    AuthTokenInfo getAuthTokenInfoById(Long id);

}