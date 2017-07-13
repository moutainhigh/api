<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>设置</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/setting.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>

    </style>
</head>
<body>
   <div class="container">
       <input type="hidden" id="auth" value="${auth }">
       <section class="f">
            <div class="p-group">
                <p class="update-pw common-bg" id="passwordReset">
                    <span class="span-label">修改密码</span>
                </p>
            </div>
       </section>

       <section class="f1">
             <div class="logout">退出登录</div>
       </section>

   </div>
</body>
</html>
<script>
    var _auth = $("#auth").val();
    $(function(){
        load();
    });
    function load() {
        //重置密码
        $("#passwordReset").on("click", function () {
            location.href = "./toPasswordReset?auth="+_auth;
        });


        //退出
        $(".f1").on("click", function () {
            $.post("../shop/logout",{"auth":_auth},function(obj){
                if(obj.code == 0){
                	 location.href = "../merchant/index?appId=wx79bd044fd98536f4";
                }
            });
        });

    }
</script>