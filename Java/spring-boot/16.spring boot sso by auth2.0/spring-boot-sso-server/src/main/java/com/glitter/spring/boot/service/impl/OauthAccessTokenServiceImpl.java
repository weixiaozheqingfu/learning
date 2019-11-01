package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.AccessTokenInParam;
import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.service.IOauthAccessTokenService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class OauthAccessTokenServiceImpl implements IOauthAccessTokenService{

    private static final Logger logger = LoggerFactory.getLogger(OauthAccessTokenServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;

    /**
     * 验证accessToken
     * @param accessToken
     * @return
     */
    @Override
    public AccessTokenInParam validateAccessToken(String accessToken) {
        // 1.验证accessToken是否存在
        OauthAccessToken record = new OauthAccessToken();
        record.setAccessToken(accessToken);
        OauthAccessToken oauthAccessTokenDb = oauthAccessTokenDao.get(record);
        if (null == oauthAccessTokenDb) {
            throw new BusinessException("50035", "accessToken不存在");
        }

        // 2.验证accessToken是否在有效期内
        if(System.currentTimeMillis() > oauthAccessTokenDb.getAccessTokenExpireTime().getTime()){
            throw new BusinessException("50036", "accessToken失效");
        }

        // 3.返回当前请求AccessToken的权限相关数据
        AccessTokenInParam accessTokenInParam = new AccessTokenInParam();
        accessTokenInParam.setJsessionid(oauthAccessTokenDb.getJsessionId());
        accessTokenInParam.setAccess_token(accessToken);
        accessTokenInParam.setClientId(oauthAccessTokenDb.getClientId());
        accessTokenInParam.setUserId(oauthAccessTokenDb.getUserId());
        accessTokenInParam.setInterfaceUri(Arrays.asList(StringUtils.join(oauthAccessTokenDb.getInterfaceUri(),",")));

        return accessTokenInParam;
    }

    @Override
    public OauthAccessToken getOauthAccessToken(String accssToken) {
        if (StringUtils.isBlank(accssToken)) {
            return null;
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setAccessToken(accssToken);
        return oauthAccessTokenDao.get(record);
    }

    @Override
    public List<OauthAccessToken> getOauthAccessTokensByJsessionid(String jsessionid) {
        if (StringUtils.isBlank(jsessionid)) {
            return null;
        }
        OauthAccessToken record = new OauthAccessToken();
        record.setJsessionId(jsessionid);
        return oauthAccessTokenDao.findList(record);
    }

}