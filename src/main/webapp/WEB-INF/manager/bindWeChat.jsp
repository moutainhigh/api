<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
    </script>
</head>
<body>
<form  action="./bindWeChat">
    <input type="hidden" name="openId" id="openId" value="${openId}">
    用户名：<input type="input" name="account" id="account" value="">
    密码：<input type="input" name="password" id="password" value="">
    <input type="submit" value="提交">
</form>


</body>
</html>
