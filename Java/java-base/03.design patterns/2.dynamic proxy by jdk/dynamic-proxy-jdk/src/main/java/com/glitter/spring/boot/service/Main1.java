package com.glitter.spring.boot.service;

import com.glitter.spring.boot.service.impl.AliPayServiceImpl;
import com.glitter.spring.boot.service.impl.WeChatPayServiceImpl;
import com.glitter.spring.boot.service.proxy.TimeProxyInvocationHandler1;
import sun.misc.ProxyGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Proxy;

public class Main1 extends AliPayServiceImpl {

    public static void main(String[] args){
        WeChatPayServiceImpl weChatPayService = new WeChatPayServiceImpl();

        TimeProxyInvocationHandler1 h1 = new TimeProxyInvocationHandler1(weChatPayService);

        IPayService payService$proxy1 = (IPayService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IPayService.class}, h1);

        payService$proxy1.pay(50L);

        Long money = payService$proxy1.accept(60L);
        System.out.println("money:" + money);

        createProxyClassFile("payService$proxy1", new Class[]{IPayService.class});
    }
















    /** 将生成的字节码保存到本地,方便理解代理模式的本质 */
    private static void createProxyClassFile(String proxyClassName, Class<?>[] interfaces) {
        byte[] data = ProxyGenerator.generateProxyClass(proxyClassName, interfaces);
        FileOutputStream out = null;
        try {
            String filePath = "E:\\workspaces-idea\\GITHUB\\learning\\Java\\java-base\\03.design patterns\\2.dynamic proxy by jdk\\dynamic-proxy-jdk\\src\\main\\java\\com\\glitter\\spring\\boot\\service\\impl\\" + proxyClassName + ".class";
            System.out.print(filePath);
            out = new FileOutputStream(filePath);
            out.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != out){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
