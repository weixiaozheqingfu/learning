package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthClientInfo;

public interface IOauthClientInfoService {

    /**
     * 创建客户端信息表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    void create(OauthClientInfo oauthClientInfo);

    /**
     * 修改客户端信息表; InnoDB free: 488448 kB
     * @param oauthClientInfo
     */
    void modifyById(OauthClientInfo oauthClientInfo);

    /**
     * 根据主键删除客户端信息表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取客户端信息表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthClientInfo getOauthClientInfoById(Long id);

}