package com.glitter.spring.boot.domain;

import com.glitter.spring.boot.bean.UserInfo;

import java.util.concurrent.Future;

public interface ILogDomain {

    Future<UserInfo> getFutureUserInfo (String id);

    UserInfo getUserInfo(String id);

    void execute(String id);

}