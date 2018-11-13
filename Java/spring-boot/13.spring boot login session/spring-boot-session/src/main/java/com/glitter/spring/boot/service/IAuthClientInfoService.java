package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.AuthClientInfo;

public interface IAuthClientInfoService {

    /**
     * 创建auth客户端信息表; InnoDB free: 488448 kB
     * @param authClientInfo
     */
    void create(AuthClientInfo authClientInfo);

    /**
     * 修改auth客户端信息表; InnoDB free: 488448 kB
     * @param authClientInfo
     */
    void modifyById(AuthClientInfo authClientInfo);

    /**
     * 根据主键删除auth客户端信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取auth客户端信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    AuthClientInfo getAuthClientInfoById(Long id);

}