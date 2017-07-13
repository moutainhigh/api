Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "y+": this.getYear(),
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
   $(function(){
	   $("#serach").on("click",function(){
		   var data = {
				 orderId:$.trim($("#orderId").val()),
		         transId:$.trim($("#transId").val()),
		         storeNo:$("#storeNo").val()
		   };
		   if(data.orderId == "" && data.transId == ""){
			   $(".reminders").show();
			   $("#rem").text("请填写订单号或者交易号");
			   return;
		   }
		   $.post("./serach",data,function(result){
			   if(result.code == 0){
					   var obj = result.data;
					if(obj.status == 1){
					   $(".reminders").hide();
					   if(obj.payMethod == 2){
						   $("#logo").attr("src","../resource/img/app/order/pay-icon.png");
					   }else if(obj.payMethod == 3){
					       $("#logo").attr("src","../resource/img/app/order/unionPay-ico.png");
					   }
					   $("#am").text(obj.actualChargeAmount);
					   $("#pm").text(obj.planChargeAmount);
					   $("#status").text(getStatus(obj.status));
					   $("#ctime").text(new Date(obj.ctime*1000).Format("yyyy-MM-dd hh:mm:ss"));
					   $("#money").val(obj.actualChargeAmount);
					   $("#oid").val(obj.id);
					   $(".refund_order").slideDown("slow");
				   }else{
					   $(".refund_order").hide();
					   $(".reminders").show();
					   $("#rem").text(result.msg);
				   }
			   }else{
				   $(".refund_order").hide();
				   $(".reminders").show();
				   $("#rem").text(result.msg);
			   }
			   
		   });
	   });
	   
	   
	   $("#refund").on("click",function(){
		   var data = {
				 id:$("#oid").val(),
				 price:$("#money").val(),
				 accountId:$("#accountId").val()
		   };
		   if((data.price <= 0) || (data.price > $("#am").text())){
			   alert("退款金额有误");
			   return;
		   }
		   $.post("./refund",data,function(result){
			   if(result.code == 0){
				   $(".refund_order").hide();
				   $(".reminders").show();
				   $("#rem").text(result.msg);
			   }else{
				   $(".refund_order").hide();
				   $(".reminders").show();
				   $("#rem").text(result.msg);
			   }
		   });
	   });
	   
   });
   
   function getStatus(sta){
		    var state = "";
			switch (sta) {
			case 0:
				state = "支付中";
				break;
			case 1:
				state = "支付成功";
				break;
			case 2:
				state = "支付失败";
				break;
			case 3:
				state = "退款中";
				break;
			case 4:
				state = "退款成功";
				break;
			case 5:
				state = "退款失败";
				break;
			}
			return state;
	  };
   
      