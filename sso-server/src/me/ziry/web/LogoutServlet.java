package me.ziry.web;

import me.ziry.constants.SSOConstent;
import me.ziry.utils.HttpUtil;

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

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("退出登录");

        request.getSession().setAttribute("user",null);
        request.getSession().setAttribute("SSO_TOKEN", null);

        //SSO客户端注销会带上token
        String token = request.getParameter("SSO_TOKEN");
        if(token!=null) {

            //从application域里移除对应token
            ServletContext application = request.getSession().getServletContext();
            Map<String,Date> ssoTokenMap = (Map<String, Date>) application.getAttribute("SSO_TOKEN_MAP");
            if(ssoTokenMap != null) {
                ssoTokenMap.remove(token);
            }

            //通知所有SSO客户端，注销对应token的session
            String[] ssoList = SSOConstent.SSO_CLIENT_LIST;
            for(String client : ssoList) {
                HttpUtil.doGet(client+"?token="+token);
            }

            success(response);
            return;
        }

        request.setAttribute("msg", "退出成功！");
        request.getRequestDispatcher("login.jsp").forward(request,response);
        return;

    }

    public void success(HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");

        String jsonStr ="{\"code\":\"success\",\"msg\":\"退出成功!\"}";
        PrintWriter out = response.getWriter() ;
        out.write(jsonStr);
        out.close();
    }

}
