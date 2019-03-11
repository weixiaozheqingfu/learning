package com.glitter.spring.boot.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.AuthStateInfo;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.IAuthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    ICommonCache commonCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;

    /**
     * 生成state值
     *
     * @param authStateInfo
     * @return
     */
    @Override
    public String generateState(AuthStateInfo authStateInfo) {
        String uuid = UUID.randomUUID().toString().replace("-","");
        String key = cacheKeyManager.getAuthStateKey(uuid);
        commonCache.add(key, JSONObject.toJSONString(authStateInfo), cacheKeyManager.getAuthStateKeyExpireTime());
        return uuid;
    }

    /**
     * 验证state
     * @param state
     * @param authStateInfo
     * @return
     */
    @Override
    public boolean validateState(String state, AuthStateInfo authStateInfo) {
        boolean result = false;

        if (StringUtils.isBlank(authStateInfo.getClientId())) {
            return result;
        }
        if (StringUtils.isBlank(authStateInfo.getRedirectUri())) {
            return result;
        }

        String key = cacheKeyManager.getAuthStateKey(state);
        AuthStateInfo authStateInfoRedis = commonCache.get(key);
        if (null == authStateInfoRedis) {
            return result;
        }
        if(!authStateInfo.getClientId().equals(authStateInfoRedis.getClientId())){
            return result;
        }
        if(!authStateInfo.getRedirectUri().equals(authStateInfoRedis.getRedirectUri())){
            return result;
        }
        if(!authStateInfo.getScope().equals(authStateInfoRedis.getScope())){
            return result;
        }

        result = true;
        return result;
    }
}