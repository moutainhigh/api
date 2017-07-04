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
	   var pm = 1;
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
					   pm = obj.payMethod;
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
		   if(pm == 3){
			   jalert.show("不支持银联卡退款");
			   return;
		   }
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
   
	  ////////////////////////////////////////////////////////////
	  //设置键盘高度
        var wheight = ($(window).height() / 2.4) / 5;
        $('.wwt_keyboard > .wwt_num').css({
            'height' : wheight,
            'line-height' : wheight + "px"
        });
        var kh = ($(window).height() / 3) / 5;
        $('.wwt_keyboard > .first').css({
            'height' : kh,
            'line-height' : kh + "px"
        });
        //默认focus对象。
	    var cur_focus_obj = $("#transId");
	    function cur_focus(obj){
	    	cur_focus_obj = $(obj);
	    	document.activeElement.blur();
	    	$('.wwt_keyboard').show().removeClass('slideOutDown').addClass('slideInUp');
            $("input").removeClass("cur_input");
            cur_focus_obj.addClass("cur_input");
	    };
	  
        $(".complete").on("click",function(){
        	$('.wwt_keyboard').removeClass('slideInUp').addClass('slideOutDown');
        });
        
        $(".wwt_num").on("touchstart",function(){
        	var btntext = $(this).text();
        	if(btntext / 1){	
        		clickkey(btntext);
        	}else{
        		if (cur_focus_obj.val() != '0' && btntext == "0") {
                    clickkey('0');
                }
                if (btntext == ".") {
                    clickkey('.');
                }
                if ($.trim(btntext) == "删除") {
                    backspace();
                }
        	}
        });
        
        //数字键
        function clickkey(num) {
            var inputtext = cur_focus_obj.val();
                inputtext = inputtext + num;
            if ((/^[0-9]+(\.{1}[0-9]{0,2})?$/).test(inputtext)) {
                inputtext = inputtext.replace(/[^\d.]/g, "");
                //清除"数字"和"."以外的字符
                inputtext = inputtext.replace(/^\./g, "");
                //验证第一个字符是数字而不是
                inputtext = inputtext.replace(/\.{2,}/g, ".");
                //只保留第一个. 清除多余的
                inputtext = inputtext.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
                inputtext = inputtext.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3');
                //只能输入两个小数
                cur_focus_obj.val(inputtext);
            }

        }
      //退格键方法
        function backspace() {
            var inputtext = cur_focus_obj.val();
            if (inputtext.length > 0) {
                if (inputtext.length == 1) {
                	cur_focus_obj.val('');
                    $('#redBag').html('0');
                    $("#realMoney").html('0.00');
                } else {
                    inputtext = inputtext.substring(0, inputtext.length - 1);
                    cur_focus_obj.val(inputtext);
                }
            }
        }
      