package me.ziry.web;

import me.ziry.model.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录，Get请求访问页面，Post请求处理登录
 * 采用写死的方式模仿登录：
 * 用户名：test，密码：123456
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    /**
     * Get请求访问页面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession().getAttribute("user");
        String backUrl = request.getParameter("backUrl");

        String tokenParam = (String) request.getSession().getAttribute("SSO_TOKEN");
        if(tokenParam == null || user==null || backUrl==null) {
            request.setAttribute("backUrl",backUrl);
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        ServletContext application = request.getSession().getServletContext();
        Map<String,Date> ssoTokenMap = (Map<String, Date>) application.getAttribute("SSO_TOKEN_MAP");

        if(ssoTokenMap == null) {
            request.setAttribute("backUrl",backUrl);
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        Date validity = ssoTokenMap.get(tokenParam);
        if(validity == null) {
            request.setAttribute("backUrl",backUrl);
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        //判断是否已过期
        long differTime =  new Date().getTime()-validity.getTime();
        if(differTime > 1000 * 60 * 30) { //大于30分钟
            ssoTokenMap.remove(tokenParam);
            request.setAttribute("backUrl",backUrl);
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        //当前token有效，无需重复登录
        String token = (String) request.getSession().getAttribute("SSO_TOKEN");
        response.sendRedirect(backUrl+"?token="+token);
        return;

    }

    /**
     * Post请求
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String backUrl = request.getParameter("backUrl");

        System.out.println("获取到登录信息, username:"+username+", password:"+password);

        //加载登录用户, 模仿登录（实际应访问数据库）
        User user = this.loadLoginUser(username, password);

        if(user==null) {
            request.setAttribute("msg", "账户密码错误！");
            request.setAttribute("backUrl", backUrl);
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;
        }

        request.getSession().setAttribute("user",user);

        String token = UUID.randomUUID().toString();
        request.getSession().setAttribute("SSO_TOKEN",token);

        //将当前生成的token, 注册到application域, 并记录当前时间，用于过期判断。
        ServletContext application = request.getSession().getServletContext();
        Map<String,Date> ssoTokenMap = (Map<String, Date>) application.getAttribute("SSO_TOKEN_MAP");
        if(ssoTokenMap == null) {
            ssoTokenMap = new HashMap<>();
        }
        ssoTokenMap.put(token, new Date());
        application.setAttribute("SSO_TOKEN_MAP", ssoTokenMap);

        if(backUrl!=null && backUrl.length()!=0) {
            response.sendRedirect(backUrl+"?token="+token);
            return;
        }

        response.sendRedirect("/IndexServlet");
    }


    /**
     * 加载登录用户
     * !!!模仿登录（实际应访问数据库）
     * @param username 用户名
     * @param password 密码
     * @return
     */
    private User loadLoginUser(String username, String password) {

        //模仿登录
        if(!username.equals("test") || !password.equals("123456")) {
            return null;
        }

        User user = new User();
        user.setName("李四");
        user.setAge(18);
        user.setSex(1);//1是男，2是女、
        user.setId(1);

        return user;
    }

}
