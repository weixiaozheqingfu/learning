package com.glitter.spring.boot.persistence.cache.redis;

import com.glitter.spring.boot.exception.BusinessException;
import com.glitter.spring.boot.persistence.cache.ICacheKeyManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CacheKeyManagerImpl implements ICacheKeyManager {

    private static final Logger logger = LoggerFactory.getLogger(CacheKeyManagerImpl.class);

    /** 10分钟 */
    private static final Long DEFAULT_EXPIRE_TIME = 60 * 10L;

    /**
     * 接口变量默认修饰符public、static、final可省
     */
    private static final String KEY_PREFIX = "gliter:sso_server";

    static String getKey(String... str) {
        String result = KEY_PREFIX;
        if (null == str || str.length <= 0) {
            logger.error("系统运行异常,ICommonCache接口getKey方法输入参数为空");
            throw new BusinessException("-1", "系统运行异常");
        }
        for (int i = 0; i < str.length; i++) {
            if (StringUtils.isBlank(str[i])) {
                continue;
            }
            result += ":" + str[i];
        }
        return result;
    }

    @Override
    public String getSessionKey(String uuid) {
        return CacheKeyManagerImpl.getKey("session", "sessions", uuid);
    }

    @Override
    public Long getSessionKeyExpireTime() {
        return DEFAULT_EXPIRE_TIME;
    }


    @Override
    public Long getLimitMultiLoginKeyExpireTime() {
        return DEFAULT_EXPIRE_TIME;
    }

    /**
     * 限制多端同时登陆时,getSessionKeyKey
     * @param userId
     * @return
     */
    @Override
    public String getLimitMultiLoginKey(String userId) {
        return CacheKeyManagerImpl.getKey("session", "limitMultiLogin", "userId", userId);
    }

}
