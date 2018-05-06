<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
    <script type="application/javascript">
        var msg = "${msg}";
        if(msg != "") {
            alert(msg);
        }
    </script>
</head>
<body>
    <form action="/LoginServlet" method="post">
        <h1>SSO登录系统</h1>
    用户名：<input type="text" name="username"><br>
    密码：<input type="password" name="password"><br>
        <input type="hidden" name="backUrl" value="${backUrl}">
    <input type="submit" title="提交">
    </form>
</body>
</html>
