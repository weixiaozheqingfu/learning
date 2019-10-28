package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.bean.OauthAccessToken;
import com.glitter.spring.boot.bean.OauthClientInfo;
import com.glitter.spring.boot.bean.OauthScopeEnum;
import com.glitter.spring.boot.bean.UserAuthorizationInfo;
import com.glitter.spring.boot.persistence.dao.IOauthAccessTokenDao;
import com.glitter.spring.boot.persistence.dao.IOauthClientInfoDao;
import com.glitter.spring.boot.persistence.dao.IOauthScopeEnumDao;
import com.glitter.spring.boot.service.IUserInfoAuthorizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserInfoAuthorizationServiceImpl implements IUserInfoAuthorizationService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoAuthorizationServiceImpl.class);

    @Autowired
    IOauthAccessTokenDao oauthAccessTokenDao;
    @Autowired
    IOauthClientInfoDao oauthClientInfoDao;
    @Autowired
    IOauthScopeEnumDao oauthScopeEnumDao;

    @Override
    public List<UserAuthorizationInfo> getUserAuthorizationInfosByUserId(Long userId) {
        OauthAccessToken record = new OauthAccessToken();
        record.setUserId(userId);
        List<OauthAccessToken> oauthAccessTokens = oauthAccessTokenDao.findList(record);
        if (null == oauthAccessTokens || oauthAccessTokens.size() <= 0) {
            return null;
        }

        List<UserAuthorizationInfo> result = new ArrayList<>();
        UserAuthorizationInfo userAuthorizationInfo = null;
        for (int i = 0; i < oauthAccessTokens.size(); i++) {
            if(System.currentTimeMillis() < oauthAccessTokens.get(i).getRefreshTokenExpireTime().getTime()){
                OauthClientInfo oauthClientInfo = oauthClientInfoDao.getOauthClientInfoByClientId(oauthAccessTokens.get(i).getClientId());
                List<OauthScopeEnum> oauthScopeEnums = oauthScopeEnumDao.getOauthScopeEnumByScopeNames(Arrays.asList(oauthAccessTokens.get(i).getScope().split(",")));
                List<String> scopeNames = oauthScopeEnums.stream().map(OauthScopeEnum::getScopeName).distinct().collect(Collectors.toList());

                userAuthorizationInfo = new UserAuthorizationInfo();
                userAuthorizationInfo.setId(oauthAccessTokens.get(i).getId());
                userAuthorizationInfo.setClientName(oauthClientInfo.getClientName());
                userAuthorizationInfo.setScopeNames(scopeNames);
                result.add(userAuthorizationInfo);
            }
        }


        return null;
    }

    @Override
    public void recallUserAuthorization(Long id) {
        oauthAccessTokenDao.deleteById(id);
    }

}