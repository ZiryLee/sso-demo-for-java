package me.ziry.filter;

import me.ziry.constants.SSOConstent;
import me.ziry.model.User;
import me.ziry.utils.HttpUtil;
import net.sf.json.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录拦截器
 * Created by Ziry on 2018/4/23.
 */
@WebFilter(urlPatterns = { "/IndexServlet"})
public class LoginFilter implements Filter {

    public void destroy() {
        System.out.println("LoginFilter destroy ...");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("LoginFilter doFilter ...");

        //拦截未登录，登录页面除外
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        if (request.getSession().getAttribute("user") == null) {

            System.out.println("未登录拦截：" + request.getRequestURI());

            String ssoToken = request.getParameter("token");
            if (ssoToken == null || ssoToken.length() == 0) {
                gotoLogin(request,response);
                return;
            }

            System.out.println("校验token：" + ssoToken);

            //校验token
            JSONObject resultJson = HttpUtil.doGet(SSOConstent.SSO_LOGIN_TOKEN_VERIFY + "?token=" + ssoToken);
            if(resultJson==null) {
                gotoLogin(request,response);
                return;
            }

            String code = (String) resultJson.get("code");
            if(code==null || !code.equals("success")) {

                System.out.println("校验token失败："+ resultJson.get("msg"));

                gotoLogin(request,response);
                return;
            }

            System.out.println("校验token成功! 自动登录... " );

            //加载登录用户, 模仿登录（实际应访问数据库）
            User user = loadLoginUserByToken(ssoToken);

            request.getSession().setAttribute("user",user);
            request.getSession().setAttribute("SSO_TOKEN",ssoToken);

            /* 将当前登录的sessionId，注册到application域中， 用于sso注销*/
            ServletContext application = request.getSession().getServletContext();
            Map<String,String> ssoTokenMap = (Map<String, String>) application.getAttribute("SSO_TOKEN_MAP");
            if(ssoTokenMap == null) {
                ssoTokenMap = new HashMap<>();
            }
            ssoTokenMap.put(ssoToken, request.getSession().getId());
            application.setAttribute("SSO_TOKEN_MAP", ssoTokenMap);

        }

        chain.doFilter(req, resp);
    }

    /**
     * 加载登录用户
     * !!!模仿登录（实际应访问数据库）
     * @param ssoToken
     * @return
     */
    private User loadLoginUserByToken(String ssoToken) {
        User user = new User();
        user.setName("李四");
        user.setAge(18);
        user.setSex(1);//1是男，2是女、
        user.setId(1);

        return user;
    }

    /**
     * 重定向到登录页面
     * @param request
     * @param response
     * @throws IOException
     */
    public void gotoLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String backUrl = request.getRequestURL().toString();
        response.sendRedirect(SSOConstent.SSO_LOGIN_URL + "?backUrl=" + URLEncoder.encode(backUrl,"utf-8"));
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println("LoginFilter init ...");
    }

}
