package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import com.glitter.spring.boot.service.IOauthService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OauthServiceImpl implements IOauthService {

    private static final Logger logger = LoggerFactory.getLogger(OauthServiceImpl.class);

    @Autowired
    ICommonCache commonCache;

    @Autowired
    ICacheKeyManager cacheKeyManager;

    /**
     * 生成state值
     *
     * @param serverType
     * @return
     */
    @Override
    public String generateState(String serverType) {
        String uuid = UUID.randomUUID().toString().replace("-","");
        String key = cacheKeyManager.getAuthStateKey(uuid);
        commonCache.add(key, serverType, cacheKeyManager.getAuthStateKeyExpireTime());
        return uuid;
    }

    /**
     * 验证state
     * @param state
     * @param serverType
     * @return
     */
    @Override
    public boolean validateState(String state, String serverType) {
        if (StringUtils.isBlank(serverType)) {
            return false;
        }

        String key = cacheKeyManager.getAuthStateKey(state);
        String serverTypeRedis = commonCache.get(key);
        if (StringUtils.isBlank(serverTypeRedis)) {
            return false;
        }
        if(!serverType.equals(serverTypeRedis)){
            return false;
        }
        commonCache.del(key);
        return true;
    }
}