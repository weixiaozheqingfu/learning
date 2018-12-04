package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthServerEnum;

public interface IOauthServerEnumService {

    /**
     * 创建第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param oauthServerEnum
     */
    void create(OauthServerEnum oauthServerEnum);

    /**
     * 修改第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param oauthServerEnum
     */
    void modifyById(OauthServerEnum oauthServerEnum);

    /**
     * 根据主键删除第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取第三方auth服务平台枚举信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthServerEnum getOauthServerEnumById(Long id);

}