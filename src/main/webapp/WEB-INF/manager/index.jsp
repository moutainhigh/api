<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/index.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>

    <style>
         .logout{
            background:#fc324a;
            color:#FFF;
            text-align:center;
            padding:10px 0 ;
            font-weight:bold;
         }
         
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
                                  <span>进件商户(民生)</span>
                              </p>
                          </li>
                          <li id="newInsertFY">
                              <p>
                                  <img src="../resource/img/jinjianshanghu.png">
                              </p>
                              <p>
                                  <span>进件商户(富友)</span>
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
    <footer>
       <div class="logout" id="logout">
          <span>退出登录</span>
       </div>
    </footer>
</body>
</html>
<script>
    var auth = $("#auth").val();
    $(function(){
        load();
        
        //退出
        $("#logout").on("click",function(){
            $.post("./logout",{
            	auth:auth
            },function(result){
            	if(result.code == 0){
            		if (typeof WeixinJSBridge == "undefined"){
                        window.close();
                    }else{
                       WeixinJSBridge.call('closeWindow');
                    }
            	}else{
            		jalert.show(result.msg);
            	}
            });
        });
    });

    function load(){
        $("#newInsert").on("click",function(){
            window.open("../mchAdd/new?auth="+auth) ;
        });
        $("#opendAcount").on("click",function(){
            window.open("https://www.mszxyh.com/wapserver/outer/index.html?Page=login&ChannelId=mszx02279");
        });

        $("#newInsertFY").on("click",function(){
        	 window.open("../mchAddFY/new?auth="+auth) ;
        });
        $("#searchDeal").on("touchend",function(){
            jalert.show("暂未开通");
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