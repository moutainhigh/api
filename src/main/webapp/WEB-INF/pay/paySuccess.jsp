<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
  <head>
    <title>买单成功</title>
	<meta charset="utf-8">
    <meta name="keywords" content="keyword1,keyword2,keyword3">
    <meta name="description" content="this is my page">
    <meta name="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-touch-fullscreen" content="no">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" type="text/css" href="../resource/css/pay/common.css">
    <link rel="stylesheet" type="text/css" href="../resource/css/pay/pay.css">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
      <script type="text/javascript">
          $(document).ready(function(){
              if(1 == ${order.payMethod} ){
                  $("#payMethod").text("微信支付");
                  $("#shoplogo").prop("src","../resource/img/pay.png");
                  $("#payColor").css("color","#14d212");
              }else if( 2== ${order.payMethod}){
                  $("#payMethod").text("支付宝支付");
                  $("#shoplogo").prop("src","../resource/img/alipay.png");
                  $("#payColor").css("color","#3289ce");
              }
              var newDate = new Date();

              $("#orderTime").text(newDate.toLocaleDateString());
          });
      </script>
  </head>
  
  <body>
     <div class="container">
         <div class="pay">
                <div class="pay-flag">
                   <p><img src="" width=50 id="shoplogo"></p>
                   <p id="payColor">支付成功</p>
                   <p>
                      <span>￥</span><span>${order.actualChargeAmount}</span><del><span>￥</span>${order.planChargeAmount}</del></p>
                </div>
         </div>
         <div class="pay">
                <div class="pay-detail">
                   <p class="clearfix">
                        <span class="span-label">交易时间</span>
                        <span class="span-result" id="orderTime"></span>
                   </p>
                   <p class="clearfix">
                          <span class="span-label">订单号</span>
                          <span class="span-result">${order.orderId}</span>
                   </p>
                   <p class="clearfix">
                           <span class="span-label">付款方式</span>
                           <span class="span-result" id="payMethod"></span>
                   </p>
                </div>
         </div>
        
        
     </div>
  </body>
</html>
