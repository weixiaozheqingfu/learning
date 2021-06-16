package cn.huimin100.erp.aop.log.bean;

import java.util.Map;

public class ResponseLogInfo {

//    private String ip;
//
//    private String host;
//
//    private Integer port;

    private String url;

    private String uri;

    private String className;

    private String methodName;

//    private Map<String,Object> paramMap;

//    private Map<String,String> requestHeaderMap;

//    private Map<String,String> responseHeaderMap;

    private Integer status;

    private String contentType;

    private Object returnObj;

    private Object ex;


//    public String getIp() {
//        return ip;
//    }
//
//    public void setIp(String ip) {
//        this.ip = ip;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String host) {
//        this.host = host;
//    }
//
//    public Integer getPort() {
//        return port;
//    }
//
//    public void setPort(Integer port) {
//        this.port = port;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

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

//    public Map<String, Object> getParamMap() {
//        return paramMap;
//    }
//
//    public void setParamMap(Map<String, Object> paramMap) {
//        this.paramMap = paramMap;
//    }
//
//    public Map<String, String> getRequestHeaderMap() {
//        return requestHeaderMap;
//    }
//
//    public void setRequestHeaderMap(Map<String, String> requestHeaderMap) {
//        this.requestHeaderMap = requestHeaderMap;
//    }
//
//    public Map<String, String> getResponseHeaderMap() {
//        return responseHeaderMap;
//    }

//    public void setResponseHeaderMap(Map<String, String> responseHeaderMap) {
//        this.responseHeaderMap = responseHeaderMap;
//    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Object getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(Object returnObj) {
        this.returnObj = returnObj;
    }

    public Object getEx() {
        return ex;
    }

    public void setEx(Object ex) {
        this.ex = ex;
    }

}