package com.glitter.spring.boot.persistence.cache;

/**
 * 简单通用缓存
 * 如果有特殊处理的缓存,可以单独写
 */
public interface ICommonCache {

    /**
     * 新增
     * @param o
     */
    void add(String key, Object o);

    /**
     * 新增
     * @param o
     * @param expirtTime
     */
    void add(String key, Object o, Integer expirtTime);

    /**
     * 删除
     * @param key
     */
    void del(String key);

    /**
     * 续期
     * @param key
     */
    void renewal(String key, Integer expirtTime);

    /**
     * 获取
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 获取
     * @param key
     * @return
     */
    <T> T get(String key, Class<T> clazz);

}
