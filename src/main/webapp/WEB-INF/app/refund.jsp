<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <title>退款</title>
    <meta charset="UTF-8">
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <style type="text/css">
    body{
	    font: 12px/1.5 Microsoft YaHei,tahoma,arial,Hiragino Sans GB,\\5b8b\4f53,sans-serif;
	    color: #666;
    }
            .content{
               font-size:1rem;
            }
		    .row{
		       padding:.45rem 0;
		       font-size:0.9rem;
		    }
		    fieldset {
		        border-color:#EEE !important;
		    }
		    legend{
		       color:#ff9649;
		    }
          .row label{
             width:25%;
             text-align:right;
             display:inline-block;
             color:#4a4949;
             vertical-align:middle;
          }
          .row .row_ipt{
             width:70%;
             text-align:left;
             display:inline-block;
          } 
          .row_ipt input{
             width:100%;
             padding:.25rem;
             border:1px solid #CCC;
             border-radius:5px;
             font-size:.75rem;
          }   
          .row_ipt input:focus{
             border:1px solid #ff9649;
          }
          .serach{
             text-align:center;
          }  
          #serach, #refund{
             padding:2px .95rem;
             background:#ff9649;
             color:#FFF;
             border-radius:5px;
             box-shadow: 0 0 5px #ff9649;
          }  
          
          .redund_detail{
             padding:.5rem;
          }  
          
          .refund_order{
             margin-top:1.35rem;
             background:#EEE;
             position:relative;
             display:none;
          }
          .refund_order .tips{
            position:absolute;
            top: -2.2rem;
            left: 2rem;
          }
          .tips i{
		    width: 0;
		    height: 0;
		    border-left: 1rem solid transparent;
		    border-right: 1rem solid transparent;
		    border-bottom: 1rem solid #EEE;
            
          }
          .refund_order input{
             border:0;
          }
          img{
            width:2.75rem;
            height:2.75rem;
          }
          .details{
            line-height:1.325rem;
            vertical-align: top;
            font-size:.8rem;
          }
          .details div{
             border-bottom:1px solid #DDD;
             padding:0 10px;
          }
          .details del{
             font-size:.6rem;
             color:#bbb;
             margin-left:.5rem;
          }
          #status{
            margin-left:1rem;
            padding:1px .3rem;
            background:#ff9649;
            color:#EEE;
            border-radius:5px;
            font-size:0.6rem;
          }
          .color{
            color:#666;
          }
          .reminders{
              text-align: center;
              margin-top: 2rem;
              transition: all .1s;
              display:none; 
          }
          .reminders_container{
              padding: .3rem;
              background: #ff9649;
              border-radius:5px;
              font-size:.8rem;
              color:#FFF;
              line-height: 1rem;
              margin: 0 auto;
              display: -webkit-inline-box;
          }
          .reminders_container i{
              width:1rem;
              height:1rem;
              border-radius:50%;
              background:#FFF;
              color:#ff9649;
              display:inline-block;
              font-style: normal;
          }
          .tt{
            text-align: left;
		    margin-left: 4rem;
		    line-height: 1rem;
		    padding: 0;
		    color: red;
            font-size:.6rem;
          }
    </style>
    <script type="text/javascript">
    (function(win,doc){
        function change(){
            doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/375+'px';
        }
        change();
        win.addEventListener('resize',change,false);
    })(window,document);
    
    </script>
  </head>
  
  <body>
     <div class="content">
         <input type="hidden" id="storeNo" value="${storeNo }">
         <input type="hidden" id="accountId" value="${accountId }">
         <div class="refund_con">
             <fieldset>
			     <legend>退款条件</legend>
			     <div class="row">
			         <label>订单号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="orderId" placeholder="输入退款的订单号" autofocus="autofocus">
			         </div>
			     </div>   
			     <div class="row">
			         <label>交易号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="transId" placeholder="输入退款的交易号">
			         </div>
			     </div>   
			     <div class="row tt">
			        <span id="t">*&nbsp;订单号和交易号至少一个必填!</span>
			     </div>     
			     <div class="row serach">
			         <span id="serach">搜&nbsp;索</span>
			     </div>
			     
			  </fieldset>
         </div>
	     <div class="reminders">
	           <div class="reminders_container">
	             <i>!</i>
		         <span id="rem"></span>
	         </div>
	     </div>
	     <div class="refund_order">
	             <div class="tips">
	               <i></i>
	             </div>
	             <div class="redund_detail">
	                <input type="hidden" id="oid">
	                <div class="row">
	                   <label>
	                       <img alt="" src="../resource/img/app/order/wechat-ico.png" id="logo">
	                   </label>
	                   <div class="row_ipt details">
	                       <div><span class="color">￥<span id="am"></span></span><del>￥<span id="pm"></span></del><span id="status"></span></div>
	                       <div><span class="color" id="ctime"></span></div>
                       </div>
	                </div>
	                
	                <div class="row">
	                   <label>退款金额:</label>
	                   <div class="row_ipt">
				             <input type="number" id="money" value="" >
				         </div>
	                </div>
	                <div class="row serach">
				         <span id="refund">退&nbsp;款</span>
				     </div>
	             </div>
	         </div>   
	  </div>
  </body>
</html>
<script type="text/javascript">
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
		   if(data.price <= 0 || data.price > $("#am").text() ){
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
   
   
</script>
