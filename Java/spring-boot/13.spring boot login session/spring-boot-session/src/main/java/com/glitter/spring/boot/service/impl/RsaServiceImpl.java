package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.observer.GlitterPublisher;
import com.glitter.spring.boot.service.IRsaService;
import com.glitter.spring.boot.util.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RsaServiceImpl implements IRsaService {

    @Autowired
    GlitterPublisher glitterPublisher;

    static String publicKey;
    static String privateKey;

    static {
        try {
            Map<String, Object> keyMap = RSAUtils.genKeyPair();
            publicKey = RSAUtils.getPublicKey(keyMap);
            privateKey = RSAUtils.getPrivateKey(keyMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥
     *
     * @return
     */
    @Override
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * 公钥加密
     *
     * @param str
     */
    @Override
    public String encrypt(String str) throws Exception {
        byte[] encodedData = str.getBytes();
        byte[] encodedDataEncrypt = RSAUtils.encryptByPublicKey(encodedData, publicKey);
        String result = RSAUtils.encode(encodedDataEncrypt);
        return result;
    }

    /**
     * 私钥解密
     *
     * @param str
     * @return
     */
    @Override
    public String decrypt(String str) throws Exception {
        // http的GET请求数据在网络上传输时，非字母数字字符都将被替换成百分号（%）后跟两位十六进制数，
        // 而base64编码在传输到后端的时候，+会变成空格，因此前端先替换掉。后端再替换回来。
        // str.replace("%2B","+");
        byte[] decodedData = RSAUtils.decode(str);
        byte[] decodedDataDecrypt = RSAUtils.decryptByPrivateKey(decodedData, privateKey);
        String result = new String(decodedDataDecrypt);
        return result;
    }

}