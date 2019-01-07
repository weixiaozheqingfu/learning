package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.UserAuthorizationInfo;

import java.util.List;

public interface IUserInfoAuthorizationService {

    List<UserAuthorizationInfo> getUserAuthorizationInfosByUserId(Long userId);

    void recallUserAuthorization(Long id);

}