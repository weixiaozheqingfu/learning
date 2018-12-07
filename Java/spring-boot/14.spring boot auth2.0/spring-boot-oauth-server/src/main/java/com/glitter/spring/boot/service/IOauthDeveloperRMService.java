package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthDeveloperRM;

public interface IOauthDeveloperRMService {

    /**
     * 创建开放平台开发者账号的资源信息映射
     * @param developerId
     * @param userId
     * @return
     */
    String create(Long developerId, Long userId);

    /**
     * 修改开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param oauthDeveloperRM
     */
    void modifyById(OauthDeveloperRM oauthDeveloperRM);

    /**
     * 根据主键删除开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取开放平台开发者账号的资源信息映射表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthDeveloperRM getOauthDeveloperRMById(Long id);

    OauthDeveloperRM getByDeveloperIdAndUserId(Long developerId, Long userId);

}