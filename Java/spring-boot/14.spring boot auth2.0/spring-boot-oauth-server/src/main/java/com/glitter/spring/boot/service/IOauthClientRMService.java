package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthClientRM;

public interface IOauthClientRMService {

    /**
     * 创建客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthClientRM
     */
    void create(OauthClientRM oauthClientRM);

    /**
     * 修改客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthClientRM
     */
    void modifyById(OauthClientRM oauthClientRM);

    /**
     * 根据主键删除客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取客户端的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthClientRM getOauthClientRMById(Long id);

}