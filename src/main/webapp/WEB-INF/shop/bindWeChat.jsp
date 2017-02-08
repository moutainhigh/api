<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">
        body{
            background: #FFF;
        }
        .container{
            text-align: center;
            margin:0 auto;
            background: #FFF;
        }
        .f1{
            margin:2em 0 1.5em;
        }
        .logo img{
            width: 6em;
            height: 6em;
            border-radius: 10px;
            padding: .5em;
            border: 1px solid #EEE;
        }
        .concept{
            margin-top:1em;
        }
        .concept img{
            width:10em;
        }
        .f2{
            margin-top:1.5em;
        }
        .login-box label img{
            width:1.4em;
            height:1.4em;
        }
        .login-box .username,
        .login-box .pw{
            padding:1.25em 0 .75em;
            border-bottom: 1px solid #DDD;
            margin:0 2em;
            position: relative;
        }
        .login-box .username label,
        .login-box .pw label{
            position: absolute;
            left:.5em;
        }
        .login-box .username input,
        .login-box .pw input{
            height:1.5em;
            font-size: 17px;
            margin-left:2.8em;
            font-weight: bold;
            opacity: .5;
        }
        .login-box .login-btn{
            margin-top:2em;
        }
        .login-box .login-btn .login{
            color:#FFF;
            background: #fc324a;
            padding:.5em 4em;
            border-radius: 20px;
        }
    </style>
</head>
<body>
<input type="hidden" name="openId" id="openId" value="${openId}">
<div class="container">
    <section class="f1">
        <div class="logo">
                  <span>
                      <img src="../resource/img/app/logo.png">
                  </span>
        </div>
        <div class="concept">
            <span><img src="../resource/img/app/biaoyu.png"></span>
        </div>
    </section>
    <section class="f2">
        <div class="login-box">
            <div class="username">
                <label for="account">
                    <img src="../resource/img/app/yonghuming.png">
                </label>
                <input type="text" placeholder="请输入用户名" id="account">
            </div>
            <div class="pw">
                <label for="password">
                    <img src="../resource/img/app/mima.png">
                </label>
                <input type="password" placeholder="请输入密码" id="password">
            </div>
            <div class="login-btn">
                <div class="inner">
                    <span class="login" id="login">登录</span>
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>
<script>
    $(function(){
        $("#login").on("click",function(){
            _submit();
        })
    })
    function _submit(){
        var account = $("#account").val();
        var password = $("#password").val();
        var openId = $("#openId").val();
        $.post("./bindWeChat",{"account":account,"password":password,"openId":openId},function(data){
            if(data.code == 0){
                location.href = "./index";
            }else{
                jalert.show(data.msg);
            }
        });
    }
</script>
