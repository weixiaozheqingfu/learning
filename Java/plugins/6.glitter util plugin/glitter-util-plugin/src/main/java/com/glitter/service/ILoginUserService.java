package com.glitter.service;

import com.glitter.dto.LoginUserDTO;

public interface ILoginUserService {

    /**
     * 获取登录用户信息
     * @return
     * @throws Exception
     */
    LoginUserDTO getLoginUserInfo();

}
