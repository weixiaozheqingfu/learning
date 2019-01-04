package com.glitter.spring.boot.service;

import com.glitter.spring.boot.bean.OauthCode;

public interface IOauthCodeService {

    String generateCode(OauthCode oauthCode);

    void deleteById(Long id);

    void deleteByCode(String code);

    OauthCode getOauthCodeById(Long id);

    OauthCode getOauthCodeByCode(String code);

}