package com.glitter.spring.boot.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
