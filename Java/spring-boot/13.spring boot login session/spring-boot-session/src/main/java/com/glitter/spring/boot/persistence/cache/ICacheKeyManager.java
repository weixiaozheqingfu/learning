package com.glitter.spring.boot.persistence.cache;

import com.glitter.spring.boot.SpringBootStartApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 缓存key的统一管理中心
 * 所有业务相关的key必须全部在这里生成,保证项目中所有地方使用到的key值的统一性
 * 方法命名规则:
 * 以值的所属对象名称+key,如getSessionKey,
 * 如果值是字符串类型,则可以根据其业务含义命名,如getSessionKeyKey,即获取SessionKey的key
 *
 * @param <T>
 */
public interface ICacheKeyManager<T> {

    Logger logger = LoggerFactory.getLogger(SpringBootStartApplication.class);

    String getSessionKey(String uuid);

    Integer getSessionKeyExpireTime();

    String getLimitMultiLoginKey(String userId);

    Integer getLimitMultiLoginKeyExpireTime();

}
