package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AuthServerInfo;

public interface IAuthServerInfoService {

    /**
     * 创建第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param authServerInfo
     */
    void create(AuthServerInfo authServerInfo);

    /**
     * 修改第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param authServerInfo
     */
    void modifyById(AuthServerInfo authServerInfo);

    /**
     * 根据主键删除第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取第三方auth服务平台信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    AuthServerInfo getAuthServerInfoById(Long id);

}