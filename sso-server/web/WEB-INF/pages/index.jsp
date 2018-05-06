<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>个人信息</title>
  </head>
  <body>
  <h1>SSO服务端：个人信息</h1>
    用户名：${user.name}<br>
    年龄：${user.age}<br>
    <a href="/LogoutServlet">退出登录</a>
  </body>
</html>
