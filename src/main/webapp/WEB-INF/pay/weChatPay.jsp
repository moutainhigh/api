<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
  <head>
    <title>向商家支付</title>
	<meta charset="utf-8">
    <meta name="keywords" content="keyword1,keyword2,keyword3">
    <meta name="description" content="this is my page">
    <meta name="content-type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-touch-fullscreen" content="no">
	<meta content="black" name="apple-mobile-web-app-status-bar-style">
	<meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" type="text/css" href="../resource/css/pay/common.css"/>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
	<script type="text/javascript" src="../resource/js/jquery.alert.js"></script>

  </head>
  
  <body>
	  <input value="${openId}" id="openId" name="openId" type="hidden">
	  <input value="${payMethod}" id="payMethod" name="payMethod" type="hidden">
	  <input value="${store.storeNo}" id="storeNo" name="storeNo" type="hidden">
     <div class="container">
        <div class="store">
           <div class="wechat-header clearfix">
              <div class="logo"><img  src="${store.shopLogo}" onerror="this.src='../resource/img/cat.png'"></div>
              <div class="name">${store.name}</div>
           </div>
        </div>
	     <footer style="height:auto;">
	        <div class="wechat-input">
	             <div class="inputn clearfix">
		            <div class="money-name">金额</div>
		            <div class="money" >
		              <input type="text" placeholder="输入消费金额" readonly id="paymoney">
		            </div>
		            <div class="money-fh">￥</div>
		        </div>
	        </div>
	        <div class="pay-keyboard">
	            <table cellpadding="0" cellspacing="0">
	               <tr>
	                  <td data-val="1">1</td>
	                  <td data-val="2">2</td>
	                  <td data-val="3">3</td>
	                  <td data-val="delete">删除</td>
	               </tr>
	               <tr>
	                  <td data-val="4">4</td>
	                  <td data-val="5">5</td>
	                  <td data-val="6">6</td>
	                  <td rowspan="3" data-val="pay" id="pay">
	                     <span>立即支付</span>
	                  </td>
	               </tr>
	               <tr>
	                  <td data-val="7">7</td>
	                  <td data-val="8">8</td>
	                  <td data-val="9">9</td>
	               </tr>
	               <tr class="last">
	                  <td data-val="cls">清空</td>
	                  <td data-val="0">0</td>
	                  <td data-val=".">.</td>
	               </tr>
	            </table>
	         </div>
	      </footer>
        
     </div>
  </body>
</html>
<script>
  $(function(){
	  $("td").on("touchend",function(){
	      var paymoney = $("#paymoney").val(),
		      v = $(this).attr("data-val");
		  if(v === "delete"){//删除
			  var monlen =paymoney.length;
			  if(monlen > 1 ){
				  $("#paymoney").val(paymoney.substr(0,monlen-1));
			  }else{
				  $("#paymoney").val("");
			  }
		  }else if(v == "pay"){//支付
			  if(paymoney != "" && Number(paymoney) >0){
				  pay();
			  }else{
				  return false;
			  }
		  }else if(v === "cls"){//清除
			  $("#paymoney").val("");
		  }else{
			    if(Number($("#paymoney").val()+v.toString()) >= 10000){
			    	return false;
			    }
				var arr=paymoney.split(".");
				if(arr.length<2){
						$("#paymoney").val(paymoney+v.toString());
				}else{
					if(v.toString()=="."){
						return;
					}else{
						if(arr[1].length>1){
							return;
						}else{
							$("#paymoney").val(paymoney+v.toString());
						}
					}
				}
		  }
		  if(($("#paymoney").val()).length > 0){//
	    	  $("#pay").addClass("wechat-pay");
	    	  $("#paymoney").addClass("wechat-input-gl");
	      }else{
	    	  $("#pay").removeClass("wechat-pay");
	    	  $("#paymoney").removeClass("wechat-input-gl");
	      }
	  });
  });


  function pay() {
	  var paymoney = $("#paymoney").val();
	  var payMethod = $("#payMethod").val();
	  var storeNo = $("#storeNo").val();
	  var discountType = $("#discountType").val();
	  var discountId = $("#discountId").val();
	  var discountPrice = $("#discountPrice").val();
	  var openId = $("#openId").val();

	  var postData = {
		  "orderPrice": paymoney,
		  "payMethod": payMethod,
		  "storeNo": storeNo,
		  "discountType": discountType,
		  "discountId": discountId,
		  "discountPrice": discountPrice,
		  "openId": openId
	  }
	  $.post("./payMoney", postData, function (result) {
		  if(result.code == 0){

			  appId = result.data.appId+"";
			  timeStamp = result.data.timeStamp+"";
			  nonceStr = result.data.nonceStr+"";
			  package = result.data.package+"";
			  signType = result.data.signType+"";
			  paySign = result.data.paySign+"";
			  orderId = result.data.orderId;
		  }else{
			  jalert.show("支付失败");
			  return;
		  }
		  if (typeof WeixinJSBridge == "undefined"){
			  jalert.show("使用微信");
		  }else{
			  onBridgeReady();
		  }
	  });

  }

  var appId = "";
  var timeStamp = "";
  var nonceStr = "";
  var package = "";
  var signType = "";
  var paySign = "";
  var orderId="";

  function onBridgeReady(){
	  WeixinJSBridge.invoke('getBrandWCPayRequest', {
				  "appId": appId,                //公众号API
				  "timeStamp": timeStamp,          //时间戳，自 1970 年以来的秒数
				  "nonceStr": nonceStr,          //随机串
				  "package": package,       //商品包信息
				  "signType": signType,          //微信签名方式
				  "paySign": paySign             //微信签名
			  },
			  function(res){
				  if(res.err_msg == "get_brand_wcpay_request:ok" ) {
					  location.href = "./paySuccess?orderId="+orderId;
				  }else{
					  jalert.show(res.err_msg);
				  } // 最后显示所有的属性 }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
			  }
	  );
  }


</script>
