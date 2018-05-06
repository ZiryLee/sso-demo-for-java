package me.ziry.constants;

/**
 *  这是SSO系统的常量配置
 *
 *  ！！这里需要配置hosts，通过这种方式模仿访问3个不通域名地址，解决浏览器记录重复jsessionid问题。
 *  127.0.0.1 test-sso-server.com
 *  127.0.0.1 test-sso-client01.com
 *  127.0.0.1 test-sso-client02.com
 *
 * Created by Ziry on 2018/4/23.
 */
public class SSOConstent {

    /**
     * 单点登录系统登录地址
     */
    public static final String SSO_LOGIN_URL = "http://test-sso-server.com:8080/LoginServlet";

    /**
     * 单点登录系统登录地址
     */
    public static final String SSO_LOGOUT_URL = "http://test-sso-server.com:8080/LogoutServlet";

    /**
     * 单点登录系统Token校验地址
     */
    public static final String SSO_LOGIN_TOKEN_VERIFY = "http://test-sso-server.com:8080/VerifyServlet";

}
