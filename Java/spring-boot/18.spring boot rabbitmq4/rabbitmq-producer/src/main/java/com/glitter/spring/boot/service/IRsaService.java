package com.glitter.spring.boot.service;

public interface IRsaService {

    /**
     * 获取公钥
     * @return
     */
    String getPublicKey();

    /**
     * 公钥加密
     */
    String encrypt(String str) throws Exception;

    /**
     * 私钥解密
     * @param str
     * @return
     */
    String decrypt(String str) throws Exception;

}