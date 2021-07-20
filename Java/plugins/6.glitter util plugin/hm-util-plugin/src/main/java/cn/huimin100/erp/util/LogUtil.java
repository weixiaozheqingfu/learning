package cn.huimin100.erp.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具
 *
 * @author limengjun
 * @date 2021/6/13 10:51
 **/
public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

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
            throw new RuntimeException("日志参数格式异常");
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
            throw new RuntimeException("日志参数处理异常");
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
            logger.error("[LogUtil.getPatternMsg]输入参数:pattern:{},params:{},异常信息:{}", pattern, JSONObject.toJSONString(params), JSONObject.toJSONString(ex));
            throw new RuntimeException("日志参数处理异常");
        }
        return result;
    }

//    public static void main(String[] args) {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setAccount("高兴");
//
//        String result1 = com.glitter.spring.boot.util.LogUtil.getParamMsg("userName", "uaerAge", "张三", "100");
//        System.out.println("result1--------"+result1);
//        String result2 = com.glitter.spring.boot.util.LogUtil.getParamMsg("id", "用户信息", "123", userInfo);
//        System.out.println("result2--------"+result2);
//        String result3 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("调用xx方法,地址是:{},输入参数:{}", "http://xxx.com", "666");
//        System.out.println("result3--------"+result3);
//        String result4 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("调用xx方法,地址是:{},输入参数:{},返回参数:{}", "http://xxx.com", "123", userInfo);
//        System.out.println("result4--------"+result4);
//        String result5 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("就是高兴打日志,就是玩");
//        System.out.println("result5--------"+result5);
//        String result6 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("参数值:{}",null);
//        System.out.println("result6--------"+result6);
//        String result7 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("参数值:{},返回值:{}",null, "12");
//        System.out.println("result7--------"+result7);
//        String result8 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("参数值:{},返回值:{}","21", null);
//        System.out.println("result8--------"+result8);
//        String result9 = com.glitter.spring.boot.util.LogUtil.getPatternMsg("参数值:{},中间值:{},返回值:{}","21", null, userInfo);
//        System.out.println("result9--------"+result9);
//    }

}
