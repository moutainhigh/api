<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script>
        function _submit(){
            var account = $("#account").val();
            var password = $("#password").val();
            var openId = $("#openId").val();
            $.post("./bindWeChat",{"account":account,"password":password,"openId":openId},function(data){
                if(data.code == 0){
                    location.href = "./index";
                }else{
                    alert(data.msg);
                }
            });
        }

    </script>
</head>
<body>
    <input type="hidden" name="openId" id="openId" value="${openId}">
    用户名：<input type="input" name="account" id="account" value="">
    密码：<input type="input" name="password" id="password" value="">
    <input type="button" value="提交" onclick="_submit()">
store

</body>
</html>
