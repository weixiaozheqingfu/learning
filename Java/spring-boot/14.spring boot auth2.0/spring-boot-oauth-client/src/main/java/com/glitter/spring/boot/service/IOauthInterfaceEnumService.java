package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthInterfaceEnum;

public interface IOauthInterfaceEnumService {

    /**
     * 创建授权接口枚举表; InnoDB free: 488448 kB
     * @param oauthInterfaceEnum
     */
    void create(OauthInterfaceEnum oauthInterfaceEnum);

    /**
     * 修改授权接口枚举表; InnoDB free: 488448 kB
     * @param oauthInterfaceEnum
     */
    void modifyById(OauthInterfaceEnum oauthInterfaceEnum);

    /**
     * 根据主键删除授权接口枚举表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取授权接口枚举表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthInterfaceEnum getOauthInterfaceEnumById(Long id);

}