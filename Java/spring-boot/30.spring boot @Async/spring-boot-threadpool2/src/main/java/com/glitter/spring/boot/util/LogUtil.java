package com.glitter.spring.boot.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.glitter.spring.boot.exception.BusinessException;
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
    public static String getParamMsg(Object... params) {
        return getLogMsg("输入参数", params);
    }

    public static String getMiddleMsg(Object... params) {
        return getLogMsg("中间值", params);
    }

    public static String getResultMsg(Object... params) {
        return getLogMsg("输出参数", params);
    }

    private static String getLogMsg(String position, Object... params) {
        Map<String, Object> map = null == params ? null : new HashMap<>();
        int halfFlag = params.length % 2;
        if (halfFlag != 0) {
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
            logger.error(JSONObject.toJSONString(ex));
            throw new BusinessException("-1", "日志参数处理异常");
        }
        return result;
    }

    public static void main(String[] args) {
        Exception e = new NullPointerException("空指针异常");
        String result2 = LogUtil.getParamMsg("某类", "某方法", "参数一", "参数二");
        System.out.print(result2);
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
