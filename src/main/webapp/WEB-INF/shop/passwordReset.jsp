<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>密码重置</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/passwordReset.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style>


    </style>
</head>
<body>
<div class="container">
    <section class="f">
        <div class="p-group">
            <p >
                <span class="span-label">旧密码</span>
                <span class="span-result"><input type="password" id="password" placeholder="请输入就密码"></span>
                <span class="forget">忘记密码?</span>
            </p>
            <p >
                <span class="span-label">新密码</span>
                <span class="span-result"><input type="password" id="newPassword" placeholder="请输入新密码"></span>
            </p>
        </div>

    </section>

    <section class="f1">
        <div class="save">保存</div>
    </section>

</div>
</body>
</html>
<script>
    var auth = "${auth}";
    $(function(){
        load();
    })
    function load(){
        $(".f1").on("touchend",function(){
            _submit();
        });
    }

    function _submit(){
        var pw = $.trim($("#password").val());
        var npw = $.trim($("#newPassword").val());
        if(pw == "" || npw ==""){
            jalert.show("不能为空");
            return;
        }

        if(pw == npw){
            jalert("新旧密码不能一样");
            return;
        }
        $.post("./passwordReset",{"auth":auth,"password":pw,"newPassword":npw},function(obj){
            if(obj.code == 0){
                jalert.show("修改成功");
            }else{
                jalert.show(obj.msg);
            }
        });

    }
</script>