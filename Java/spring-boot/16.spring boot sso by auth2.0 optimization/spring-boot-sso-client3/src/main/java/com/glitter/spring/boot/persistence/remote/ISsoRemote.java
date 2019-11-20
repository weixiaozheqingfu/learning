package com.glitter.spring.boot.persistence.remote;


import com.glitter.spring.boot.bean.UserInfo;

import java.util.Map;

public interface ISsoRemote {

    Map getAccessToken(String client_id, String client_secret, String redirect_uri, String code, String grant_type);

    Map refreshToken(String client_id, String refresh_token, String grant_type);

    Map auth(String accessToken);

    UserInfo getUerInfo(String access_token);

    void keepAlive(String access_token);

    void logout(String accessToken);

}
