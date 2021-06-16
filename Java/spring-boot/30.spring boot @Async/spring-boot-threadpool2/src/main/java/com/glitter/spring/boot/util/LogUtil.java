package com.glitter.spring.boot.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /**
     * new HashMap<String, Object>(){{put(param1, param1);put(param1, param2);}};
     */


    public static String getParamLogMsg(Map<String, Object> map) {
        return getLogMsg("输入参数", map);
    }

    public static String getMiddleLogMsg(Map<String, Object> map) {
        return getLogMsg("中间值", map);
    }

    public static String getResultLogMsg(Map<String, Object> map) {
        return getLogMsg("输出参数", map);
    }

    private static String getLogMsg(String position,Map<String, Object> map) {
        String result = "";
        try {
            String formatStr = "[{0}.{1}]方法,{2}:{3}";
            String[] jsonObj = new String[4];
            jsonObj[0] = getClassName();
            jsonObj[1] = getMethodName();
            jsonObj[2] = position;
            jsonObj[3] = JSONObject.toJSONString(map);
            result = MessageFormat.format(formatStr, jsonObj);
        } catch (Exception ex) {
            result = "系统异常日志:TemplateUtil类getExceptionLogMsg方法执行异常,异常信息:" + JSONObject.toJSONString(ex);
        }
        return result;
    }

    private static String getClassName(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getClass().getSimpleName();
    };

    private static String getMethodName(){
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        return stackTraceElements[2].getMethodName();
    };


//    public static void main(String[] args) {
//        Exception e = new NullPointerException("空指针异常");
//        String result2 = TemplateUtil.getExceptionLogMsg("某类", "某方法", e, "参数一", "参数二");
//        System.out.print(result2);
//    }


//    public static void main(final String[] arguments) throws Exception {
//        Class<?> clazz = LogUtil.class;
//        Method method = clazz.getDeclaredMethod("test", String.class, int.class, String[].class);
//        System.out.print("test : ");
//        Parameter[] parameters = method.getParameters();
//        for (final Parameter parameter : parameters) {
//            if (parameter.isNamePresent()) {
//                System.out.print(parameter.getName() + ' ');
//            }
//        }
//    }
//
//    public void test(String param1, int param2, String... param3) {
//        new HashMap<String, Object>(){{put(param1, param1);put(param1, param2);}};
//        System.out.println(param1 + param2);
//    }

}
