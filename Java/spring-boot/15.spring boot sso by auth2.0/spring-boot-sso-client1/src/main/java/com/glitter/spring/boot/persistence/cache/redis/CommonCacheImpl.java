package com.glitter.spring.boot.persistence.cache.redis;

import com.glitter.spring.boot.persistence.cache.ICommonCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CommonCacheImpl implements ICommonCache {

    private static final Logger logger = LoggerFactory.getLogger(CommonCacheImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增
     * @param key
     * @param o
     */
    @Override
    public void add(String key, Object o) {
        this.add(key, o, null);
    }

    /**
     * 新增
     * @param key
     * @param o
     * @param expirtTime
     */
    @Override
    public void add(String key, Object o, Long expirtTime) {
        if (expirtTime == null) {
            redisTemplate.opsForValue().set(key, o, expirtTime);
        }else {
            redisTemplate.opsForValue().set(key, o, expirtTime, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除
     *
     * @param key
     */
    @Override
    public void del(String key) {
        if(StringUtils.isBlank(key)) { return; }
        redisTemplate.delete(key);
    }


    /**
     * 续期
     *
     * @param key
     */
    @Override
    public void renewal(String key, Long expirtTime) {
        redisTemplate.expire(key, expirtTime, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key) {
        T o = (T)redisTemplate.opsForValue().get(key);
        return o;
    }

    /**
     * 验证键是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

}
