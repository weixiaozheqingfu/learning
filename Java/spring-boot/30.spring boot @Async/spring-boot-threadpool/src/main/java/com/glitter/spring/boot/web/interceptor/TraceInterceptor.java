package com.glitter.spring.boot.web.interceptor;

import com.glitter.spring.boot.constant.TraceConstants;
import com.glitter.spring.boot.context.ContextManager;
import com.glitter.spring.boot.context.RequestContext;
import com.glitter.spring.boot.context.ResponseContext;
import com.glitter.spring.boot.util.TraceLogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TraceInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // "traceId"
        MDC.put(TraceConstants.LOG_TRACE_ID, TraceLogUtils.getTraceId());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        MDC.clear();
    }

}
