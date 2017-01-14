<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" type="text/css" href="../resource/css/common.css">
    <script type="text/javascript" src="../resource/js/adapter.js"></script>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
	  <script src="https://as.alipayobjects.com/g/component/antbridge/1.1.1/antbridge.min.js"></script>

  </head>
  
  <body>
  <input value="${buyerId}" id="buyerId" name="buyerId" type="hidden">
  <input value="${payMethod}" id="payMethod" name="payMethod" type="hidden">
  <input value="${store.storeNo}" id="storeNo" name="storeNo" type="hidden">
     <div class="container">
        <div class="store">
           <div class="ali-header clearfix">
              <div class="logo"><img alt="商家logo" src="${store.shopLogo}"  onerror="this.src='../resource/img/cat.png'"></div>
              <div class="name">${store.name}</div>
           </div>
        </div>
        <div class="ali-input">
	             <div class="inputn clearfix">
		            <div class="money-name">金额</div>
		            <div class="money" >
		              <input type="text" placeholder="输入消费金额" readonly id="paymoney">
		            </div>
		            <div class="money-fh">￥</div>
		        </div>
	        </div>
	     <footer style="height:auto;">
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
			    if(Number($("#paymoney").val()+v.toString()) > 10000){
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
	    	  $("#pay").addClass("ali-pay");
	      }else{
	    	  $("#pay").removeClass("ali-pay");
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
	  var buyerId = $("#buyerId").val();

	  var postData = {
		  "orderPrice": paymoney,
		  "payMethod": payMethod,
		  "storeNo": storeNo,
		  "discountType": discountType,
		  "discountId": discountId,
		  "discountPrice": discountPrice,
		  "buyerId": buyerId
	  }
	  $.post("./payMoney", postData, function (result) {
		  if(result.code == 0){
			  _tradeNO = result.data.trade_no
			  orderId = result.data.orderId;
		  }else{
			  alert("支付失败");
			  return;
		  }
		  AlipayJSBridgeReady();
	  });

  }

  var _tradeNO = "";
  var orderId = "";
  function AlipayJSBridgeReady(){
	  document.addEventListener('AlipayJSBridgeReady', function () {
		  AlipayJSBridge.call("tradePay", {tradeNO:_tradeNO}, function (result) {
			  var _data = {"data":result};
			  $.post("./payNotifyAli",_data);
			  location.href = "./paySuccess?orderId="+orderId;
//
//                alert(JSON.stringify(result));
		  });}, false);
  }


</script>