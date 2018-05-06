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

    public static String[] SSO_CLIENT_LIST = {
            "http://test-sso-client01.com:8081/SSOLogoutServlet",
            "http://test-sso-client02.com:8082/SSOLogoutServlet"
    };

}
