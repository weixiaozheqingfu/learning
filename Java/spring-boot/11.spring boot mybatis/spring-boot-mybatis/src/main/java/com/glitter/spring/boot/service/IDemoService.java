package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.UserInfo;

public interface IDemoService {

    /**
     * 获取用户信息
     * @param id
     * @return
     * @throws Exception
     */
    UserInfo getUserInfo(Long id) throws Exception;

}
