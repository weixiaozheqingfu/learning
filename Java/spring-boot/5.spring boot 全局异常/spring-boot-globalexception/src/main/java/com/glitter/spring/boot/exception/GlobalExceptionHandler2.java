package com.glitter.spring.boot.exception;

import com.glitter.spring.boot.util.TemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler2 {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler2.class);
    public static final String DEFAULT_ERROR_VIEW = "/templates/error.html";

    /** TODO 最好采用模板技术(FreeMarker,Groovy,Thymeleaf,Mustache等),而不是纯静态html,这样页面可以通过表达式渲染后台的异常信息 */
    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest req, Model m, Exception e) {
        logger.error(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        System.out.println(TemplateUtil.getExceptionLogMsg(this.getClass().getName(), Thread.currentThread().getStackTrace()[1].getMethodName(), e));
        m.addAttribute("exception", e);
        m.addAttribute("url", req.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }

}