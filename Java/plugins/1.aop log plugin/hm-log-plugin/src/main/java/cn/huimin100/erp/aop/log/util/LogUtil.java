package cn.huimin100.erp.aop.log.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {

    private static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

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

    public static void main(String[] args) {
        Exception e = new NullPointerException("空指针异常");
        String result2 = LogUtil.getParamMsg("某类", "某方法", "参数一", "参数二");
        System.out.print(result2);
    }

}
