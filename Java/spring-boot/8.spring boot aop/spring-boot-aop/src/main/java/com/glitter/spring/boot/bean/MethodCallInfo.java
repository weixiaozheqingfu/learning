package com.glitter.spring.boot.bean;

import java.util.Map;

public class MethodCallInfo {

    private String className;

    private String methodName;

    private Map<String,Object> paramMap;

    // TODO header和其他request参数,例如请求路径,ip等信息可以另外写一个类继承该类
    // private String header;


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

}