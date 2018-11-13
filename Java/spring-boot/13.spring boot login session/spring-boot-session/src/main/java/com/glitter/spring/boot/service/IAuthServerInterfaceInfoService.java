package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AuthServerInterfaceInfo;

public interface IAuthServerInterfaceInfoService {

    /**
     * 创建第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param authServerInterfaceInfo
     */
    void create(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
     * 修改第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param authServerInterfaceInfo
     */
    void modifyById(AuthServerInterfaceInfo authServerInterfaceInfo);

    /**
     * 根据主键删除第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取第三方auth服务平台声明接口信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    AuthServerInterfaceInfo getAuthServerInterfaceInfoById(Long id);

}