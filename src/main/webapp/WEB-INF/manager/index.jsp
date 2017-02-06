<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>商户进件</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/index.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>

    </style>
</head>
<body>
    <input value="${auth}" id="auth" name="auth" type="hidden">
    <div class="container">
          <section class="f1">
              <div class="common-pd clearfix">
                   <div class="today">
                       <p>
                           <span>今日交易</span>
                       </p>
                       <p>
                           <span id="sumDeal">￥0.00</span>
                       </p>
                   </div>
                  <div class="number">
                      <p>
                          <span>交易笔数</span>
                      </p>
                      <p>
                          <span id="countDeal">0</span>
                      </p>
                  </div>
              </div>
          </section>
          <section class="f2">
              <div class="common-pd">
                  <div class="watch">
                      <ul class="clearfix">
                          <li id="newInsert">
                              <p>
                                  <img src="../resource/img/jinjianshanghu.png">
                              </p>
                              <p>
                                  <span>进件商户</span>
                              </p>
                          </li>
                          <li id="searchStore">
                              <p>
                                  <img src="../resource/img/chaxunjinjian.png">
                              </p>
                              <p>
                                  <span>查询进件</span>
                              </p>
                          </li>
                          <li id="searchDeal">
                              <p>
                                  <img src="../resource/img/shanghujiaoyi.png">
                              </p>
                              <p>
                                  <span>商户交易</span>
                              </p>
                          </li>
                          <li id="opendAcount">
                              <p>
                                  <img src="../resource/img/zhixiaoyinhang.png">
                              </p>
                              <p>
                                  <span>直销银行</span>
                              </p>
                          </li>
                      </ul>
                  </div>
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
        $("#newInsert").on("touchend",function(){
            window.open("./newInsert?auth="+auth) ;
        });
        $("#opendAcount").on("touchend",function(){
            window.open("https://www.mszxyh.com/wapserver/outer/index.html?Page=login&ChannelId=mszx02279");
        });

        $("#searchStore").on("touchend",function(){
           alert("暂未开通");
        });
        $("#searchDeal").on("touchend",function(){
            alert("暂未开通");
        });

        var auth = $("#auth").val();
        $.post("./countDeal",{"auth":auth},function(obj){
            if(obj.code == 0){
                $("#sumDeal").text(obj.data.sum);
                $("#countDeal").text(obj.data.count);
            }
        });
    }
</script>