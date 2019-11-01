package com.glitter.spring.boot.persistence.remote;

public interface IClientRemote {

    void logoutClient(String url, String jsessionidClient) throws Exception;

}
