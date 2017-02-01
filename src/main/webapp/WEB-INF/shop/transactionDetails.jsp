<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>交易明细</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/transactionDetails.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <style type="text/css">
    </style>
</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
   <div class="container">
       <section class="f1" id="_searchParam">
           <div class="common-mg pd">
               <div class="tran-month common-pd">
                   <span class="tm">交易当月</span>
                   <span class="m">当月</span>
               </div>
           </div>
       </section>
       <section class="f2">
           <div class="common-mg">
              <div class="common-pd">
                 <span>累计交易:</span><span class="color-read sum">0</span>元
              </div>
           </div>
       </section>
       <section class="f3">
              <div class="clearfix ui-border-bottom">
                      <div class="date-day common-pd">
                          <span>2016-07-15</span><span>周五</span>
                      </div>
                      <div class="number-money common-pd">
                           <span>共<span class="color-read">6</span>笔</span><span class="color-read">￥6.00</span>
                      </div>
              </div>
           <ul>
               <li class="ui-border-bottom">
                  <div class="common-mg">
                      <span class="info">
                          <em>1.00</em>
                      </span>
                      <span class="item" >
                          <img src="../resource/img/app/weixinzhifu.png">
                          <div class="cont">
                              <p class="desc"><span>收款码收款</span></p>
                              <p class="time">21:02:56</p>
                          </div>
                      </span>
                  </div>
               </li>
               <li class="ui-border-bottom">
                   <div class="common-mg">
                      <span class="info">
                          <em>1.00</em>
                      </span>
                       <span class="item" >
                          <img src="../resource/img/app/zhifubao.png">
                          <div class="cont">
                              <p class="desc">
                                  <span>收款码收款</span>
                                  <span>立减</span>
                                  <span><del>￥2.00</del></span>
                              </p>
                              <p class="time">21:02:56</p>
                          </div>
                      </span>
                   </div>
               </li>


           </ul>
       </section>
       <section class="f4">
           <div class="common-mg">

           </div>
       </section>

   </div>
</body>
</html>
<script>
    var auth = $("#auth").val();
    $(function(){
        load();
    })
    function load(){
        //查询条件
        $("#_searchParam").on("touchend",function(){
            location.href = "./toPaystyle?auth="+auth;
        });

    }
</script>