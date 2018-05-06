package me.ziry.web;

import me.ziry.constants.SSOConstent;
import me.ziry.utils.HttpUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出登录
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("退出登录");

        //通知SSO系统，注销对应token的登录用户
        String SSO_TOKEN = (String) request.getSession().getAttribute("SSO_TOKEN");
        HttpUtil.doGet(SSOConstent.SSO_LOGOUT_URL+"?SSO_TOKEN="+SSO_TOKEN);

        request.getSession().setAttribute("user",null);
        request.getSession().setAttribute("SSO_TOKEN",null);

        response.sendRedirect("/IndexServlet");
        return;
    }
}
