package com.glitter.spring.boot.service.impl;

import com.glitter.spring.boot.service.IRsaService;
import com.glitter.spring.boot.util.RSAUtils;
import org.springframework.stereotype.Service;

@Service
public class RsaServiceImpl implements IRsaService {

    static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA534pEZcnJKf+FZNUIlnZAA+HkAZxCTGgjuwy\n" +
            "kgrfBT/PRfbEf0KERsBYK39mkn+bZPJ2kF5QYTdMmoy3vjwXN6/XttOYIM9An4YfkvnylmpWjJh/\n" +
            "khdjYLUNArn+xHe5KP4z4/utlQlI0B5rP7RZdwLwI0LhO+FrrdQAlLX9ewIDAQAB";
    static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIDnfikRlyckp/4Vk1QiWdkAD4eQ\n" +
            "BnEJMaCO7DKSCt8FP89F9sR/QoRGwFgrf2aSf5tk8naQXlBhN0yajLe+PBc3r9e205ggz0Cfhh+S\n" +
            "+fKWalaMmH+SF2NgtQ0Cuf7Ed7ko/jPj+62VCUjQHms/tFl3AvAjQuE74Wut1ACUtf17AgMBAAEC\n" +
            "gYAXcQGiWk6QkbvzGt1V+YE5Uyd/0ZM6Tx+1QQh3EdPkRsIZ4XFEvpfMEkm8PvzGHHIUQkFNimd8\n" +
            "WpGB3sEkUPYASB3mv7XkoaCRUBoADPmosGfqGPeTsigKm9VxrnuMVBSKNj3q0njRNe2Bx2+L2K6F\n" +
            "KLHsBWrogObqVn0hC150IQJBANIEPE7kEtXxVumMQpf2lhQiHS8GPUvlsG2kTGPxFAHxs3jfWKaC\n" +
            "YVvBfiSfnemRgM1T0yvkL26twL4o3oKDSvkCQQCdIMixxT2PJdnjGVmLUqFUbjuqmyMtPWklf+ak\n" +
            "QeAoGxaC93BjwlZCrF4s0fIQBGQV66vzvcRoTPzUyW4ZmRUTAkBQu3IXfZNEDN81LkbXNeJhYO/J\n" +
            "COP9ThFxRGxdKupfCPPN7kWc1Jfqdsov9+/zPoy94ZBW2gPlzwM91mYa06+5AkALCc6Pjoewkrwn\n" +
            "1BQSUY+72rKeXZ+vAe1tEPsm9unEZ4LBWJiwqWpd7LYA+A7FSUmusIu359DwctggKrZtXe+5AkBM\n" +
            "N5NnmHB4qRyCN/wEMGgS1MyDFn/suGRxMpoi8FpG3raBhyhqF0s8ocLNLL0PApxjimJOmLkdifbV\n" +
            "1vfPKmx2";

    // 静态代码块动态初始化公私钥的方式在集群环境下会有问题,每个服务器产生各自的,这样前端使用时可能拿到A服务器的公钥,然后在请求时,可能到B服务器去传加密串解密
//    static {
//        try {
//            Map<String, Object> keyMap = RSAUtils.genKeyPair();
//            publicKey = RSAUtils.getPublicKey(keyMap);
//            privateKey = RSAUtils.getPrivateKey(keyMap);
//
//            System.out.println("publicKey:" + publicKey);
//            System.out.println("privateKey:" + privateKey);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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