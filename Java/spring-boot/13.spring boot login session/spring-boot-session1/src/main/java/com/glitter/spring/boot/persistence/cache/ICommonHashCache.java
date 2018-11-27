package com.glitter.spring.boot.persistence.cache;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface ICommonHashCache {

    void put(String key, Object mkey, Object value);

    void putAll(String key, Map<Object, Object> map);

    void putIfAbsent(String key, Object mkey, Object value);

    void del(String key, Object... o);

    void delAll(String key);

    void renewal(String key, Long expirtTime);

    <T> T getValue(String key, Object mkey);

    <T,K> Map<T,K> getAll(String key);

    List<Object> getKeys(String key);

    boolean isExists(String key);

    boolean isExists(String key, Object mkey);

    Long getExpire(String key);

    Long getExpire(String key, TimeUnit timeUnit);
}
