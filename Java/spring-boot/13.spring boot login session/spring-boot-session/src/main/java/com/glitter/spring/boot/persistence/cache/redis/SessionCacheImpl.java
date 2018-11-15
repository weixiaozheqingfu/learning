package com.glitter.spring.boot.persistence.cache.redis;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.persistence.cache.ICommonCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionCacheImpl implements ICommonCache {

    private static final Logger logger = LoggerFactory.getLogger(SessionCacheImpl.class);

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
    public void add(String key, Object o, Integer expirtTime) {
        String jsonStr = JSONObject.toJSONString(o);
        if (expirtTime == null) {
            redisTemplate.opsForValue().set(key, jsonStr, expirtTime);
        }else {
            redisTemplate.opsForValue().set(key, jsonStr, expirtTime, TimeUnit.SECONDS);
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
    public void renewal(String key, Integer expirtTime) {
        redisTemplate.expire(key, expirtTime, TimeUnit.SECONDS);
    }

    /**
     * 获取
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        String str = null == o ? null : String.valueOf(o);
        return str;
    }

    /**
     * 获取
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        String jsonStr = this.get(key);
        T t = StringUtils.isBlank(jsonStr) ? null : JSONObject.parseObject(jsonStr, clazz);
        return t;
    }

}
