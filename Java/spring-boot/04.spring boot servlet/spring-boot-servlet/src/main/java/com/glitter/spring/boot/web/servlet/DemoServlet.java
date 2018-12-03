package com.glitter.spring.boot.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无论内置tomcat容器还是外置tomcat容器,项目配置多个servlet时,loadOnStartup值越小,init方法加载越早。
 *
 * 至于具体的doGet方法和doPost方法,匹配到哪个就访问哪个,这中间有一个匹配度的问题,哪个更匹配就访问哪个,还有一个远近的问题,优先使用本项目的servlet,然后才是jar包中的。
 *
 */
@WebServlet(urlPatterns = {"/cemo/*"}, loadOnStartup = 1)
public class DemoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-----------DemoServlet doGet----------------");
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("-----------DemoServlet doPost----------------");
        resp.getWriter().print("<h1>Hello DemoServlet</h1>");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("-----------DemoServlet init----------------");
        super.init();
    }

}
