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
		    html{
				-webkit-tap-highlight-color:rgba(0,0,0,0);
				-webkit-tap-highlight-color:transparent;
				-moz-user-select:none;
			    -webkit-user-select:none;
			    -ms-user-select:none;
			    -khtml-user-select:none;
			    user-select:none;
			}
    
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
          .cur_input,.row_ipt input:focus{
             border:1px solid #ff9649 !important;
          }
          
          .serach{
             text-align:center;
          }  
          .refund-w{
            text-align: center;
		    display: inline-block;
		    margin-left: .7rem;
		    vertical-align: middle;
          }
          #serach, #refund{
             padding:2px .95rem;
             background:#ff9649;
             color:#FFF;
             border-radius:5px;
             box-shadow: 0 0 5px #ff9649;
          }  
          #refund{
             font-size:.8rem;
          }
          .redund_detail{
             padding:.5rem;
          }  
          .redund_detail .refund_ipt {
            width:40% !important;
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
          .refund_order img{
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
			         <label>交易号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="transId" placeholder="输入退款的交易号" onkeypress="javascript:return false" onfocus="cur_focus(this)"  >
			         </div>
			     </div>   
			     <div class="row">
			         <label>订单号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="orderId" placeholder="输入退款的订单号"  onkeypress="javascript:return false" onfocus="cur_focus(this)" value="10001170608382451285" >
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
	                   <label>退款金额:</label>
	                   <div class="row_ipt refund_ipt">
				             <input type="number" id="money" value="" onkeypress="javascript:return false" onfocus="cur_focus(this)"  >
				         </div>
		              <div class="refund-w">
					         <span id="refund">退&nbsp;款</span>
					  </div>
	                </div>
	                <div class="row">
	                   <label>
	                       <img alt="" src="../resource/img/app/order/wechat-ico.png" id="logo">
	                   </label>
	                   <div class="row_ipt details">
	                       <div><span class="color">￥<span id="am"></span></span><del>￥<span id="pm"></span></del><span id="status"></span></div>
	                       <div><span class="color" id="ctime"></span></div>
                       </div>
	                </div>
	                
	             </div>
	         </div>   
	  </div>
	  <style>
	    .wwt_keyboard{
	        overflow: hidden;
		    position: fixed;
		    bottom: 0;
		    left: 0;
		    right: 0;
/* 		    -webkit-animation-name: slideOutDown; */
/* 		    animation-name: slideOutDown; */
		    -webkit-animation-duration: .8s;
		    animation-duration: .8s;
		    -webkit-animation-fill-mode: both;
		    animation-fill-mode: both;
		    background: #fff;
		    display:none;
	    }
	    .wwt_num{
	        width:33.3%;
	        float:left;
	        text-align:center;
	        color:#697790;
	        position:relative;
	        font-size:1.2rem;
	    }
	    .keyboard:before, .complete:before, .wwt_num:before{
	        right: 0;
		    height: 1px;
		    width: 100%;
		    top: 0;
		    border-top: 1px solid #dbdee5;
		    margin-top: -1px;
		    content: " ";
		    position: absolute;
		    -webkit-transform-origin: 0 100%;
		    transform-origin: 0 100%;
		    -webkit-transform: scaleY(.5);
		    transform: scaleY(.5);
	    }
	    .keyboard:after, .complete:after, .wwt_num:after{
	        left: 0;
		    width: 1px;
		    height: 100%;
		    top: 0;
		    border-left: 1px solid #dbdee5;
		    content: " ";
		    position: absolute;
		    -webkit-transform-origin: 0 100%;
		    transform-origin: 0 100%;
		    -webkit-transform: scaleX(.5);
		    transform: scaleX(.5);
	    }
	    .keyboard{
	        width:66.6% !important;
		    text-align: center;
		    color: #C3C3C3;
		    position: relative;
		    font-size: 1.4rem;
		    float:left;
		    letter-spacing: 2px;
	    }
	    .complete{
	        width:33.3% !important;
		    text-align: center;
		    position: relative;
		    font-size: .9rem;
		    float:left;
		    background:#C3C3C3;
		    color:#FFF;
	    }
	    
	    @-webkit-keyframes slideInDown {
		    from {
		        -webkit-transform: translate3d(0, -100%, 0);
		        transform: translate3d(0, -100%, 0);
		        visibility: visible
		    }
		
		    to {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		}
		
		@keyframes slideInDown {
		    from {
		        -webkit-transform: translate3d(0, -100%, 0);
		        transform: translate3d(0, -100%, 0);
		        visibility: visible
		    }
		
		    to {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		}
		
		.slideInDown {
		    -webkit-animation-name: slideInDown;
		    animation-name: slideInDown
		}
		
		.animated {
		    -webkit-animation-duration: .8s;
		    animation-duration: .8s;
		    -webkit-animation-fill-mode: both;
		    animation-fill-mode: both
		}
		
		@-webkit-keyframes slideInUp {
		    from {
		        -webkit-transform: translate3d(0, 100%, 0);
		        transform: translate3d(0, 100%, 0);
		        visibility: visible
		    }
		
		    to {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		}
		
		@keyframes slideInUp {
		    from {
		        -webkit-transform: translate3d(0, 100%, 0);
		        transform: translate3d(0, 100%, 0);
		        visibility: visible
		    }
		
		    to {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		}
		
		.slideInUp {
		    -webkit-animation-name: slideInUp;
		    animation-name: slideInUp
		}
		
		@-webkit-keyframes slideOutDown {
		    from {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		
		    to {
		        visibility: hidden;
		        -webkit-transform: translate3d(0, 120%, 0);
		        transform: translate3d(0, 120%, 0)
		    }
		}
		
		@keyframes slideOutDown {
		    from {
		        -webkit-transform: translate3d(0, 0, 0);
		        transform: translate3d(0, 0, 0)
		    }
		
		    to {
		        visibility: hidden;
		        -webkit-transform: translate3d(0, 120%, 0);
		        transform: translate3d(0, 120%, 0)
		    }
		}
		
		.slideOutDown {
		    -webkit-animation-name: slideOutDown;
		    animation-name: slideOutDown
		}
	    
	    
	    .delbtn i {
	        display: block;
		    width: 20px;
		    height: 20px;
		    background: #697790;
		    position: absolute;
		    left: 50%;
		    margin-left: -8px;
		    margin-top: -13px;
		    font-size: 0;
		    top: 50%;
	    }
	    .delbtn i:after {
		    border: 10px solid transparent;
		    border-right-color: #697790;
		    content: '';
		    display: block;
		    position: absolute;
		    left: -20px;
		    top: 0;
		}
	    .ico-del:before {
    content: "\e901";
}
	   .delbtn i s {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 10px;
    height: 10px;
    margin-left: -7px;
    margin-top: -5px;
    -webkit-transform: rotate(45deg);
    -moz-transform: rotate(45deg);
    -ms-transform: rotate(45deg);
    -o-transform: rotate(45deg);
    transform: rotate(45deg);
}

.delbtn i s:after, .delbtn i s:before {
    width: 10px;
    height: 2px;
    content: '';
    display: block;
    background: #fff;
    position: absolute;
    left: 50%;
    top: 50%;
    margin-left: -5px;
    margin-top: -1px;
}

.delbtn i s:after {
    width: 2px;
    height: 10px;
    margin-top: -5px;
    margin-left: -1px;
} 
	    
	  </style>
	  <div class="wwt_keyboard">
	     <div class="keyboard first" id="keyboard">
	       <span>智慧商街</span>
	     </div>
	     <div class="complete first" id="complete">
	       <span>完成</span>
	     </div>
	     <div class="wwt_num" id="no1">1</div>
	     <div class="wwt_num" id="no2">2</div>
	     <div class="wwt_num" id="no3">3</div>
	     <div class="wwt_num" id="no4">4</div>
	     <div class="wwt_num" id="no5">5</div>
	     <div class="wwt_num" id="no6">6</div>
	     <div class="wwt_num" id="no7">7</div>
	     <div class="wwt_num" id="no8">8</div>
	     <div class="wwt_num" id="no9">9</div>
	     <div class="wwt_num" id="no10">.</div>
	     <div class="wwt_num" id="no0">0</div>
	     <div class="wwt_num delbtn" id="no11">
	        <i class="ico-del">删除<s></s></i>
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
      
</script>
