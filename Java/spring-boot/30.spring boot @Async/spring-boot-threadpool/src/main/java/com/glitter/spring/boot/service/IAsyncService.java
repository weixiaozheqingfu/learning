package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.UserInfo;

import java.util.concurrent.Future;

public interface IAsyncService {

    Future<UserInfo> execute1(String id) throws InterruptedException;

    Future<String> execute2(String id);

    String execute3(String id) throws Throwable;

    void execute4(String id) throws InterruptedException;

}