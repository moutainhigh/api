<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>签约信息</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/signInfo.css" type="text/css" rel="stylesheet">
    <style>

    </style>
</head>
<body>
   <div class="container">
       <section class="f1">
           <span>费率</span>
       </section>
       <section class="f2">
           <div class="pay">
               <div class="aliwx">
                   <span>
                        <img src="../resource/img/app/zhifubao.png">
                   </span>
                   <span>支付宝收款</span>
                   <span>${rate.alRate}</span>
               </div>
           </div>
           <div class="pay">
               <div class="aliwx">
                   <span>
                        <img src="../resource/img/app/weixinzhifu.png">
                   </span>
                   <span>微信收款</span>
                   <span>${rate.wxRate}</span>
               </div>
           </div>
       </section>
       <section class="f3">
           <div class="agreement">
               <div class="content">
                   <span>协议与条款</span>
               </div>
           </div>
       </section>
   </div>
</body>
</html>