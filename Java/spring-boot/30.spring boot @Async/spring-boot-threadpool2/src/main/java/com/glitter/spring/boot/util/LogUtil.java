package com.glitter.spring.boot.util;

import com.alibaba.fastjson.JSONObject;
import com.glitter.spring.boot.bean.UserInfo;
import com.glitter.spring.boot.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /**
     * new HashMap<String, Object>(){{put(param1, param1);put(param1, param2);}};
     */
    public static String getInMsg(Object... params) {
        return getLogMsg("-2-输入参数", params);
    }

    public static String getMiddleMsg(Object... params) {
        return getLogMsg("-2-中间值", params);
    }

    public static String getOutMsg(Object... params) {
        return getLogMsg("-2-输出参数", params);
    }

    private static String getLogMsg(String position, Object... params) {
        Map<String, Object> map = null == params ? null : new HashMap<>();
        int halfFlag = params.length % 2;
        if (halfFlag != 0) {
            logger.error("[LogUtil.getLogMsg]输入参数:position:{},params:{}", position, JSONObject.toJSONString(params));
            throw new BusinessException("-1", "日志参数格式异常");
        }
        int halfSize = params.length / 2;
        for (int i=0; i < halfSize; i++) {
            map.put(String.valueOf(params[i]),params[i+halfSize]);
        }
        String result = "";
        try {
            String formatStr = "[{0}.{1}]{2}:{3}";
            String[] jsonObj = new String[4];
            jsonObj[0] = Thread.currentThread().getStackTrace()[3].getClassName().substring(Thread.currentThread().getStackTrace()[3].getClassName().lastIndexOf(".") + 1);
            jsonObj[1] = Thread.currentThread().getStackTrace()[3].getMethodName();
            jsonObj[2] = position;
            jsonObj[3] = JSONObject.toJSONString(map);
            result = MessageFormat.format(formatStr, jsonObj);
        } catch (Exception ex) {
            logger.error("[LogUtil.getLogMsg]输入参数:position:{},params:{},异常信息:{}", position, JSONObject.toJSONString(params), JSONObject.toJSONString(ex));
            throw new BusinessException("-1", "日志参数处理异常");
        }
        return result;
    }

    public static String getPatternMsg(String pattern, Object... params) {
        String result = "";
        try {
            String className = Thread.currentThread().getStackTrace()[2].getClassName().substring(Thread.currentThread().getStackTrace()[2].getClassName().lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
            if (!pattern.contains("{")) {
                String formatStr = "[{0}.{1}]-2-{2}";
                String[] jsonObj = new String[3];
                jsonObj[0] = className;
                jsonObj[1] = methodName;
                jsonObj[2] = pattern;
                result = MessageFormat.format(formatStr, jsonObj);
                return result;
            }

            String patternStr = "";
            String[] patternStrArray = pattern.split("\\}");
            for (int i = 0; i < patternStrArray.length; i++) {
                int num = i + 2;
                patternStrArray[i] = patternStrArray[i] + num + "}";
                patternStr = patternStr + patternStrArray[i];
            }

            String formatStr = "[{0}.{1}]-2-" + patternStr;
            String[] jsonObj = null == params ? new String[3] : new String[params.length + 2];
            jsonObj[0] = className;
            jsonObj[1] = methodName;

            if (null == params) {
                jsonObj[2] = null;
            } else {
                for (int i = 0; i < params.length; i++) {
                    Object o = params[i];
                    if (o instanceof String) {
                        jsonObj[i + 2] = String.valueOf(o);
                    } else {
                        jsonObj[i + 2] = JSONObject.toJSONString(o);
                    }
                }
            }
            result = MessageFormat.format(formatStr, jsonObj);
        } catch (Exception ex) {
            logger.error("[LogUtil.getLogMsg]输入参数:pattern:{},params:{},异常信息:{}", pattern, JSONObject.toJSONString(params), JSONObject.toJSONString(ex));
            throw new BusinessException("-1", "日志参数处理异常");
        }
        return result;
    }

    public static void main(String[] args) {
        UserInfo userInfo = new UserInfo();
        userInfo.setAccount("高兴");

        String result1 = LogUtil.getInMsg("userName", "uaerAge", "张三", "100");
        System.out.println("result1--------"+result1);
        String result2 = LogUtil.getInMsg("id", "用户信息", "123", userInfo);
        System.out.println("result2--------"+result2);
        String result3 = LogUtil.getPatternMsg("调用xx方法,地址是:{},输入参数:{}", "http://xxx.com", "666");
        System.out.println("result3--------"+result3);
        String result4 = LogUtil.getPatternMsg("调用xx方法,地址是:{},输入参数:{},返回参数:{}", "http://xxx.com", "123", userInfo);
        System.out.println("result4--------"+result4);
        String result5 = LogUtil.getPatternMsg("就是高兴打日志,就是玩");
        System.out.println("result5--------"+result5);
        String result6 = LogUtil.getPatternMsg("参数值:{}",null);
        System.out.println("result6--------"+result6);
        String result7 = LogUtil.getPatternMsg("参数值:{},返回值:{}",null, "12");
        System.out.println("result7--------"+result7);
        String result8 = LogUtil.getPatternMsg("参数值:{},返回值:{}","21", null);
        System.out.println("result8--------"+result8);
        String result9 = LogUtil.getPatternMsg("参数值:{},中间值:{},返回值:{}","21", null, userInfo);
        System.out.println("result9--------"+result9);
    }


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
