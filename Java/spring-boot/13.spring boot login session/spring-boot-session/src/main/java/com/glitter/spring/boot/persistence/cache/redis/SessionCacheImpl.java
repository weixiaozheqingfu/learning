package com.glitter.spring.boot.persistence.cache.redis;

import com.glitter.spring.boot.persistence.cache.ISessionCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SessionCacheImpl implements ISessionCache {

    private static final Logger logger = LoggerFactory.getLogger(SessionCacheImpl.class);

    /** 30分钟 */
    private static final int EXPIRE_TIME = 60 * 30;

    @Autowired
    private RedisTemplate redisTemplate;

}
