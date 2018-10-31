package com.glitter.spring.boot.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class TemplateUtil {

    private static final Logger logger = LoggerFactory.getLogger(TemplateUtil.class);

    public static String getExceptionLogMsg(String className, String methodName, Exception e) {
        String result = "";
        try {
            String formatStr = "系统异常日志:{0}.{1}()方法执行异常,异常信息:{2}";
            String[] jsonObj = new String[3];
            jsonObj[0] = className;
            jsonObj[1] = methodName;
            jsonObj[2] = JSONObject.toJSONString(e);
            result = MessageFormat.format(formatStr, jsonObj);
        } catch (Exception ex) {
            result = "系统异常日志:TemplateUtil类getExceptionLogMsg方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
        return result;
    }

    /**
     * 历史方法
     *
     * @param arguments
     * @return
     */
    @Deprecated
    public static String getExceptionLogMsg(Object[] arguments) {
        try {
            return MessageFormat.format("系统异常日志:{0}.{1}()方法执行异常,异常信息:{2}", arguments);
        } catch (Exception ex) {
            return "系统异常日志:TemplateUtil类getExceptionLogMsg方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
    }

    /**
     * 推荐使用该方法
     *
     * @param className
     * @param methodName
     * @param e
     * @param arguments
     * @return
     */
    public static String getExceptionLogMsg(String className, String methodName, Exception e, Object... arguments) {
        String result = "";
        try {
            String paramStr = TemplateUtil.getInParams(className, methodName, arguments);
            String exceptionStr = TemplateUtil.getException(e);
            result = paramStr + exceptionStr;
        } catch (Exception ex) {
            result = "系统异常日志:TemplateUtil类getExceptionLogMsg方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
        return result;
    }

    public static String getInParams(String className, String methodName, Object... arguments) {
        String result = "";
        try {
            if(null == arguments){
                result += "系统异常日志:"+className+"."+methodName+"()方法执行异常,输入参数依次为:null";
                return result;
            }

            String formatStr = "系统异常日志:{0}.{1}()方法执行异常,输入参数依次为:";

            String[] jsonObj = new String[arguments.length + 2];
            jsonObj[0] = className;
            jsonObj[1] = methodName;
            for (int i = 0; i < arguments.length; i++) {
                formatStr += "{" + (i + 2) + "},";
                jsonObj[i + 2] = JSONObject.toJSONString(arguments[i]);
            }
            result = MessageFormat.format(formatStr, jsonObj);
        } catch (Exception ex) {
            result = "系统异常日志:TemplateUtil类getInParams方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
        return result;
    }

    private static String getException(Exception e) {
        String result = "";
        try {
            String formatStr = "异常信息:{0}";
            String exceptionStr = JSONObject.toJSONString(e);
            result = MessageFormat.format(formatStr, exceptionStr);
        } catch (Exception ex) {
            result = "系统异常日志:TemplateUtil类getException方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
        return result;
    }

    public static void main(String[] args) {
        Exception e = new NullPointerException("空指针异常");
        String result2 = TemplateUtil.getExceptionLogMsg("某类", "某方法", e, "参数一", "参数二");
        System.out.print(result2);
    }
}
