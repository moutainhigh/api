<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>微信推送消息</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/wxpushmsg.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>

    </style>
</head>
<body>
    <div class="container">
        <section class="f">
            <c:forEach items="${accounts.manager}" var="account">
                <div class="sales">
                    <div class="sales-man">
                    <span>
                        <img src="${account.headImg}" onerror="this.src='../resource/img/cat.png'">
                    </span>
                        <span>${account.name}</span>
                    <span>
                        <div class="jcbing" >店长</div>
                    </span>
                    </div>
                </div>
            </c:forEach>
            <c:forEach items="${accounts.cashier}" var="account">
                <div class="sales">
                    <div class="sales-man">
                    <span>
                        <img src="${account.headImg}" onerror="this.src='../resource/img/cat.png'">
                    </span>
                        <span>${account.name}</span>
                    <span>
                        <div class="jcbing" onclick="delRole(${account.id})">解除绑定</div>
                    </span>
                    </div>
                </div>
            </c:forEach>
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

    }
    function delRole(accountId){
        $.post("./delRoleById",{"accountId":accountId,"auth":auth},function(obj){
            if(obj.code == 0){
                location.href = "./toWXPushMessage?auth="+auth;
            }
        });
    }
</script>