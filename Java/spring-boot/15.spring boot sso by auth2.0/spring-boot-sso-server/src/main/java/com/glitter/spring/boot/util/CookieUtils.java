package com.glitter.spring.boot.util;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MaJingcao on 2018/3/14.
 * Copyright by syswin
 */
public class CookieUtils {

    /**
     * 更新cookie
     *
     * @param response response
     * @param name     cookie名称
     * @param value    cookie值
     * @param path     cookie存放路径
     * @param domain   cookie域
     * @param maxAge   cookie最长时间
     */
    public static void updateCookie(HttpServletResponse response, String name, String value, String path, String domain, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }


    /**
     * 更新cookie
     *
     * @param response response
     * @param cookie   cookie
     */
    public static void updateCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }


    /**
     * 获取cookie
     *
     * @param request request
     * @param name    cookie的名称
     * @return cookie
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (!cookieMap.containsKey(name)) { return null; }

        return cookieMap.get(name);
    }

    /**
     * 获取cookie的值
     *
     * @param request request
     * @param name    cookie的名称
     * @return cookie值
     */
    public static String getCookieValueByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (!cookieMap.containsKey(name)) { return null; }
        if(null == cookieMap.get(name)) { return null; }

        return cookieMap.get(name).getValue();
    }

    /**
     * 获取cookie转成map
     *
     * @param request request
     * @return map
     */
    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();

        Cookie[] cookies = request.getCookies();
        if (null == cookies || cookies.length <=0) {
            return cookieMap;
        }

        for (Cookie cookie : cookies) {
            if(null == cookie || StringUtils.isBlank(cookie.getName())) { continue;}
            cookieMap.put(cookie.getName(), cookie);
        }
        return cookieMap;
    }
}