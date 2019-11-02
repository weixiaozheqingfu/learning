package com.glitter.spring.boot.persistence.remote;


import java.util.Map;

public interface ISsoRemote {

    Map getOauthServerAccessToken(String client_id, String client_secret, String redirect_uri, String code, String grant_type);


}
