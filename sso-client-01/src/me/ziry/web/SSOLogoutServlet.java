package me.ziry.web;

import me.ziry.listener.MySessionContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * SSO系统注销通知Servlet
 */
@WebServlet("/SSOLogoutServlet")
public class SSOLogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("SSO退出登录通知");

        String token = request.getParameter("token");
        if ( token == null || token.length()==0 ) {
            error(response, "参数错误!");
            return;
        }

        //在application域里取到，该token对应的sessionId
        ServletContext application = request.getSession().getServletContext();
        Map<String,String> ssoTokenMap = (Map<String, String>) application.getAttribute("SSO_TOKEN_MAP");
        if(ssoTokenMap == null) {
            error(response, "未找到登录用户!");
            return;
        }

        String sessionId = ssoTokenMap.get(token);
        if(sessionId == null || sessionId.length()==0) {
            error(response, "未找到登录用户!");
            return;
        }

        //通过sessionId注销对应的Session
        MySessionContext.delSessionById(sessionId);

        success(response);
    }

    public void success(HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        String jsonStr ="{\"code\":\"success\",\"msg\":\"退出成功!\"}";
        PrintWriter out = response.getWriter() ;
        out.write(jsonStr);
        out.close();
    }

    public void error(HttpServletResponse response,String msg) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        String jsonStr ="{\"code\":\"error\",\"msg\":\""+msg+"\"}";
        PrintWriter out = response.getWriter() ;
        out.write(jsonStr);
        out.close();

    }
}
