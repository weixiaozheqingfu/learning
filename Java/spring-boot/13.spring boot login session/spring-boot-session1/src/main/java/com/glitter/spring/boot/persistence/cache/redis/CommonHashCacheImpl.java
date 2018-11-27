package com.glitter.spring.boot.persistence.cache.redis;

import com.glitter.spring.boot.persistence.cache.ICommonHashCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CommonHashCacheImpl implements ICommonHashCache {

    private static final Logger logger = LoggerFactory.getLogger(CommonHashCacheImpl.class);

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void put(String key, Object mkey, Object value) {
        redisTemplate.opsForHash().put(key, mkey, value);
    }

    @Override
    public void putAll(String key, Map<Object, Object> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    @Override
    public void del(String key, Object... o) {
        if(StringUtils.isBlank(key)) { return; }
        redisTemplate.opsForHash().delete(key, o);
    }

    @Override
    public void delAll(String key) {
        if(StringUtils.isBlank(key)) { return; }
        redisTemplate.opsForHash().delete(key);
    }

    @Override
    public void renewal(String key, Long expirtTime) {
        redisTemplate.expire(key, expirtTime, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getValue(String key, Object mkey) {
        T o = (T) redisTemplate.opsForHash().get(key, mkey);
        return o;
    }

    @Override
    public List<Object> getKeys(String key) {
        Set<Object> sets = redisTemplate.opsForHash().keys(key);
        List<Object> keys = null == sets ? null : new ArrayList<>(sets);
        return keys;
    }

    @Override
    public <T,K> Map<T,K> getAll(String key) {
        Map<T, K> map = redisTemplate.opsForHash().entries(key);
        return map;
    }

    @Override
    public boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public boolean isExists(String key, Object mkey) {
        return redisTemplate.opsForHash().hasKey(key, mkey);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    @Override
    public Long getExpire(String key,TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }


}
