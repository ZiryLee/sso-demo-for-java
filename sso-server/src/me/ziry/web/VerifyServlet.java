package me.ziry.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * 验证token是否有效
 * Created by Ziry on 2018/4/23.
 */
@WebServlet("/VerifyServlet")
public class VerifyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tokenParam = request.getParameter("token");
        if(tokenParam == null || tokenParam.length()==0) {
            error(response, "未找到token");
            return;
        }

        ServletContext application = request.getSession().getServletContext();
        Map<String,Date> ssoTokenMap = (Map<String, Date>) application.getAttribute("SSO_TOKEN_MAP");

        if(ssoTokenMap == null) {
            error(response, "未找到token");
            return;
        }

        Date validity = ssoTokenMap.get(tokenParam);
        if(validity == null) {
            error(response, "未找到token");
            return;
        }

        //判断是否已过期
        long differTime =  new Date().getTime()-validity.getTime();
        if(differTime > 1000 * 60 * 30) { //大于30分钟
            ssoTokenMap.remove(tokenParam);
            error(response, "token已过期");
            return;
        }

        success(response);

    }

    public void success(HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        String jsonStr ="{\"code\":\"success\",\"msg\":\"有效token\"}";
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
