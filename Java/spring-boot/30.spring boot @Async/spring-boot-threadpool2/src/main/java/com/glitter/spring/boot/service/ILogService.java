package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.UserInfo;

import java.util.concurrent.Future;

public interface ILogService {

    Future<UserInfo> getFutureUserInfo (String id);

    UserInfo getUserInfo(String id);

    void execute(String id);

}