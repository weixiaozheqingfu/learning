package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthCode;

public interface IOauthCodeService {

    /**
     * 创建预授权码表; InnoDB free: 488448 kB
     * @param oauthCode
     */
    void create(OauthCode oauthCode);

    /**
     * 修改预授权码表; InnoDB free: 488448 kB
     * @param oauthCode
     */
    void modifyById(OauthCode oauthCode);

    /**
     * 根据主键删除预授权码表; InnoDB free: 488448 kB
     * @param id
     */
    void deleteById(Long id);

    /**
     * 根据主键获取预授权码表; InnoDB free: 488448 kB
     * @param id
     * @return
     */
    OauthCode getOauthCodeById(Long id);

}