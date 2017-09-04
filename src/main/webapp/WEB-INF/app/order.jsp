<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>交易列表</title>
    <meta charset="UTF-8">
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/FJL.css">
    <link rel="stylesheet" type="text/css" href="../resource/css/app/FJL.picker.css" />
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/details.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
<!--     <script src="../resource/js/iscroll.js"></script> -->
<!--     <script src="../resource/js/fastclick.js"></script> -->
    <style type="text/css">
        html,
        body,
        .mui-content {
            height: 0;
            margin: 0;
            background: #fff;

        }
        h5.mui-content-padded {
            margin-left: 3px;
            margin-top: 20px !important;
        }
        h5.mui-content-padded:first-child {
            margin-top: 12px !important;
        }
        .mui-btn {

        }
        .ui-alert {
            text-align: center;
            padding: 20px 10px;
            font-size: 16px;
        }
        * {
            -webkit-touch-callout: none;
            -webkit-user-select: none;
        }
        .checked_color,.checked_time{
           color: #ff9649;
        }
        .checked_color:before{
           content: url(../resource/img/app/order/check-orange.png);
           position:absolute;
           left:.5rem;
        }
        .tran_time >li:not(:last-child) >ul > li{
           list-style:none;
           width:33.3%;
           float:left;
        }
        .tran_time >li:last-child >ul > li{
           list-style:none;
           width:50%;
           float:left;
        }
        .tran_time > li{
          padding: .4rem 0 !important;
          text-align:center;
        }
        .crement{
           white-space:nowrap;
           overflow:hidden;
           text-overflow:ellipsis;
           vertical-align:middle;
           display:inline-block;
           width:70%;
        }
        .shape{
          width:100%;
		  height:100%;
		  background:rgba(0,0,0,.3);
		  position:fixed;
		  top:2rem;
		  left:0;
		  right:0;
		  bottom:0;
		  z-index:100;
		  display:none;
        }
        .title{
            position: fixed;
		    top: 0;
		    left: 0;
		    right: 0;
		    z-index: 1001;
		    background: #FFF;
        }
        .con_content{
            position: fixed;
		    z-index: 1001;
		    top: 2rem;
		    left: 0;
		    right: 0;
		    width: 100%;
         }
         .shape_loading{
            position:fixed;
            display:none;
            z-index:1005;
         }
         .statistics{
            position:fixed;
            height:2.8rem;
            top:2rem;
            left:0;
            right:0;
            z-index: 1000;
            background: #FFF;
         }
         .statistics ul{
           list-style:none;
           border-bottom:1px solid #CCC;
         }
         .statistics ul li{
           float:left;
           width:33.333%;
           text-align:center;
         }
         .statistics ul li:not(:last-child){
           border-right:1px solid #CCC;
         }
         .statistics ul li p {
             font-size:.75rem;
             margin:0 !important;
             color:#666;
             line-height:1.4rem;
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
	    /*滚动条事件*/
	
// 	    var myScroll;
	
// 	    function loaded () {
// 	        myScroll = new IScroll('#wrapper');
// 	    }
	    /*fastclick.js*/
// 	    if ('addEventListener' in document) {
// 	        document.addEventListener('DOMContentLoaded', function() {
// 	            FastClick.attach(document.body);
// 	        }, false);
// 	    }
    </script>
</head>
<body>
   <div class="item clearfix">
        <div class="title">
            <ul class="title-con clearfix">
                <li class="fl padding-left-no menu-lists" data-val="-1" id="payChannel">
                    <strong class="payChannel " >渠道</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png"/> </span>
                </li>
                <li class="fl padding-left-no menu-lists" data-val="-1" id="payMethod">
                    <strong class="pay-type " >类型</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png"/> </span>
                </li>
                <li class="fl menu-lists">
                    <strong>时间</strong>
                    <span><img  src="../resource/img/app/order/triangle-symbol.png"/> </span>
                </li>
                <li class="fl menu-lists" data-val="-1" id="status">
                    <strong class="pay-status ">状态</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png"/> </span>
                </li>
                <li class="fl width-no menu-lists" data-val="-1" id="storeNo">
                    <strong class="pay-store ">门店</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png" /> </span>
                </li>
            </ul>
        </div>
        <div class="shape">
        </div>
        <div class="shape_loading">
           <img src="../resource/img/app/order/loading.gif">
        </div>
        <div class="con_content">
            <ul class="title-lists con">
	            <li data-val="-1" class="checked_color">全部</li>
	            <li data-val="1">固定二维码</li>
	            <li data-val="2">被扫</li>
	            <li data-val="3">主扫</li>
	        </ul>
	        <ul class="title-lists con">
	            <li data-val="-1" class="checked_color">全部</li>
	            <li data-val="1">微信</li>
	            <li data-val="2">支付宝</li>
	            <li data-val="3">银联</li>
	        </ul>
	        <ul class="title-lists tran_time">
	            <li>
	               <ul class="clearfix">
	                  <li class="checked_time" data-val="1">今日</li>
	                  <li data-val="2">昨日</li>
	                  <li data-val="3">本周</li>
	               </ul>
	            </li>
	            <li>
	               <ul class="clearfix">
	                  <li data-val="11">上周</li>
	                  <li data-val="12">本月</li>
	                  <li data-val="13">上月</li>
	               </ul>
	            </li>
	            <li>
	               <ul class="clearfix">
	                  <li data-val="21" id='start' data-options='{"type":"date","beginYear":2000,"endYear":2086}' class="btn" data-d="">开始时间</li>
	                  <li data-val="22" id='end' data-options='{"type":"date","beginYear":2000,"endYear":2086}' class="btn" data-d="">结束时间</li>
	               </ul>
	            </li>
	        </ul>
	        <ul class="title-lists con">
	            <li class="checked_color" data-val="-1">全部状态</li>
	            <li data-val="1">支付成功</li>
	            <li data-val="3">退款中</li>
	            <li data-val="4">退款成功</li>
	            <li data-val="5">退款失败</li>
	        </ul>
	        <ul class="title-lists con">
	            <li class="checked_color" data-val="-1">全部门店</li>
	            <c:forEach items="${storeList}" var="store" varStatus="status">
                     <li data-val="${store.storeNo}"><a>${store.name}</a></li>
	            </c:forEach>
	        </ul>
        </div>
        <div class="statistics">
             <ul class="clearfix">
                <li>
                   <p>应收</p>
                   <p>￥<span id="pm"></span></p>
                </li>
                <li>
                   <p>实收</p>
                   <p>￥<span id="am">0.00</span></p>
                </li>
                <li>
                   <p>笔数</p>
                   <p><span id="count">0</span></p>
                </li>
             </ul>
        </div>
        <div class="scroll_box"  id="wrapper">
            <div id="scroller">
               <!-- data -->
            </div>
        </div>
    </div>
</body>
</html>
<script src="../resource/js/FJL.min.js"></script>
<script src="../resource/js/FJL.picker.min.js"></script>
<script type="text/javascript">

String.prototype.gblen = function() {  
	  var len = 0;  
	  for (var i=0; i<this.length; i++) {  
	    if (this.charCodeAt(i)>127 || this.charCodeAt(i)==94) {  
	       len += 2;  
	     } else {  
	       len ++;  
	     }  
	   }  
	  return len;  
	};
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

var date = new Date();
var startTime = new Date(date+" 00:00:00").getTime(), endTime= new Date(date+" 23:59:59").getTime();
$('.menu-lists').on('click',function(){
    $('.menu-lists strong').removeClass('on').eq($(this).index()).addClass('on');
    $('.title-lists').eq($(this).index()).siblings().hide();
    $('.title-lists').eq($(this).index()).slideToggle("fast");
    var idx = $('.menu-lists').index($(this));
    if($($('.title-lists')[idx]).height() == 0){
    	$(this).addClass("checked_time");
    	$(this).siblings().removeClass("checked_time");
    	$(this).find("img").attr("src","../resource/img/app/order/triangle-symbol-orange.png");
     	$(this).siblings().find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
        $(".shape").show();
    }else{
    	$('.menu-lists').removeClass("checked_time");
    	$(this).find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
        $('.menu-lists').removeClass("checked_time");
        $(".shape").hide();
    }
});

$('.con li').on('click',function(){
	var idx = $('.title-lists').index($(this).parent());
	if(($(this).text()).gblen() > 8){
		$($('.menu-lists')[idx]).find("strong").addClass("crement");
	}else{
		$($('.menu-lists')[idx]).find("strong").removeClass("crement");
	}
	$($('.menu-lists')[idx]).attr("data-val",$(this).attr("data-val"));
	$($('.menu-lists')[idx]).find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
    $('.on').text($(this).text());
    $('.title-lists').slideUp();
    $(this).siblings().removeClass("checked_color");
    $(this).addClass("checked_color");
    $('.menu-lists').removeClass("checked_time");
    $(".shape").hide();
    order(1);
    
});
var startTime = 0,endTime = 0;
$('.tran_time .clearfix li').on("click",function(){
	if($(this).attr("data-val") < 20){
		var idx = $('.title-lists').index($(this).parents(".tran_time"));
		$($('.menu-lists')[idx]).removeClass("checked_time").find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
		$('.on').text($(this).text());
		$(this).parents(".tran_time").find("li").removeClass("checked_time");
	    $(this).addClass("checked_time");
        $('.title-lists').slideUp();
        $("#start").attr("data-d","").text("开始时间");
        $("#end").attr("data-d","").text("结束时间");
        $(".shape").hide();
        var trueDate = $(this).attr("data-val");
        if(trueDate == 1){//今日
            var dayTime = new Date(); 
            startTime = dayTime.Format("yyyy/MM/dd");
            endTime = dayTime.Format("yyyy/MM/dd");
        }else if(trueDate == 2){//昨日
            var dayTime = new Date(); 
            dayTime.setDate(dayTime.getDate()-1);
            endTime = dayTime.Format("yyyy/MM/dd");
            startTime = dayTime.Format("yyyy/MM/dd");
        }else if(trueDate == 3){//本周
            var dayTime = new Date(); 
            dayTime.setDate(dayTime.getDate()-dayTime.getDay()+1);
            startTime = dayTime.Format("yyyy/MM/dd");
            endTime = (new Date()).Format("yyyy/MM/dd");
        }else if(trueDate == 11){//上周
            var dayTime = new Date(); 
            dayTime.setDate(dayTime.getDate()-dayTime.getDay());
            endTime = dayTime.Format("yyyy/MM/dd");
            dayTime.setDate(dayTime.getDate()-6);
            startTime = dayTime.Format("yyyy/MM/dd");
        }else if(trueDate == 12){//本月
            var dayTime = new Date(); 
            dayTime.setDate(1);
            startTime = dayTime.Format("yyyy/MM/dd");
            endTime = (new Date()).Format("yyyy/MM/dd");
        }else if(trueDate == 13){//上月
            var dayTime = new Date(); 
            dayTime.setMonth(dayTime.getMonth()-1);
            dayTime.setDate(1);
            startTime = dayTime.Format("yyyy/MM/dd");
            var ed = new Date(dayTime.getFullYear(),dayTime.getMonth()+1,0);
            endTime = ed.Format("yyyy/MM/dd");
        }
        startTime = new Date(startTime +" 00:00:00").getTime()/1000;
        endTime = new Date(endTime +" 23:59:59").getTime()/1000;
        order(1);
	}
});
(function($$,$) {
	$$.init();
    var btns = $('.btn');
    btns.each(function(i, btn) {
        btn.addEventListener('tap', function() {
        	var that = $(this);
            $(btns)[0].style.paddingLeft="0.4rem";
            var optionsJson = this.getAttribute('data-options') || '{}';
            var options = JSON.parse(optionsJson);
            var picker = new $$.DtPicker(options);
            picker.show(function(rs) {
                this.innerText = rs.text;
                var date = rs.text;
                $(that).attr("data-d",date.replace(/-/g,"/"));
                $(that).parents("li").siblings().find("li").removeClass("checked_time");
                $(that).text(rs.text);
                $(that).addClass("checked_time");
                picker.dispose();
                if($("#start").attr("data-d") != "" && $("#end").attr("data-d") != ""){
                	$('.on').text('自定义');
            		$('.title-lists').slideUp();
            		$($('.menu-lists')[1]).removeClass("checked_time").find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
            		$(".shape").hide();
            		startTime = (new Date($("#start").attr("data-d")+" 00:00:00").getTime())/1000 ;
                    endTime = (new Date($("#end").attr("data-d") +" 23:59:59").getTime())/1000;
            		order(1);
            	}
            });
        }, false);
    });
})(mui,$);

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
  }
  
  
  function getCode(code){
	  if(code == ""){
		  return "";
	  }else{
		  return "(#"+code+")";
	  }
  }
    	   function order(page){
    			   var date = new Date();
    			   date = date.Format("yyyy/MM/dd");
    		   if(startTime == undefined || startTime == 0){
    			    startTime = (new Date(date+" 00:00:00").getTime())/1000;
    		   }
    		   if(endTime == undefined || endTime == 0){
    		       endTime = (new Date(date+" 23:59:59").getTime())/1000;
    		   }
    		   var storeNo = $("#storeNo").attr("data-val");
    		   var type = 0;
    		   if("-1" == storeNo){
    			   type = 1;
    			   storeNo = "${storeNo}";
    		   }
    		   
    		var data = {
    		    payChannel:$("#payChannel").attr("data-val"),
    			payMethod:$("#payMethod").attr("data-val"),
    			status:$("#status").attr("data-val"),
    			storeNo:storeNo,
    			type:type,
    			pageSize:10,
    			page:page,
    			startTime:startTime,
    			endTime:endTime
    		};
//     		console.log(data);
//     		return;
    		$.post("./orderList",data,function(result){
    			if(result.code == 0){
    				var obj = result.data.list;
    				if(page == 1){
    					totalPage = Math.round(result.data.count/10);
    					var sta = result.data.orderSta;
    					$("#pm").text(sta.pm);
    					$("#am").text(sta.am);
    					$("#count").text(sta.count);
    					document.body.scrollTop = 0;
    					$("#scroller").empty();
    				}
    				for(var i=0,len=obj.length;i<len;i++){
    					var time = new Date(obj[i].ctime*1000).Format("yyyy-MM-dd hh:mm:ss");
    					var t = time.split(" ");
    					var content = '<div class="list wepay-box">'
	    						   +  '<ul class="clearfix" >'
	    						   +  '        <li class="fl">';
	    						   if(obj[i].payMethod == 1){
	    				                    content+= '<span><img src="../resource/img/app/order/wechat-ico.png"/> </span>';
	    						   }else if(obj[i].payMethod == 2){
	    							        content+= '<span><img src="../resource/img/app/order/pay-icon.png"/> </span>';
	    						   }else if(obj[i].payMethod == 3){
	    							        content+= '<span><img src="../resource/img/app/order/unionPay-icon.png"/> </span>';
	    						   }
	    						   content +=  '</li>'
	    						   +  '        <li class="fl">'
	    						   +  '              <h2 class="time-user"><span class="type-font">'+t[0]+'</span></h2>'
	    						   +  '            <h3 class="pay-time">'+t[1]+'</h3>'
	    						   +  '          </li>'
	    						   +  '        <li class="fr">'
	    						   +  '             <h2 class=" present-price">￥'+obj[i].actualChargeAmount+'</h2>'
	    						   +  '             <h2 class="old-price">￥'+obj[i].planChargeAmount+'</h2>'
	    						   +  '        </li>';
	    						   if(obj[i].status != 4){
	    							   content += ' <li class="fl business-cheap"><h2 class="business-num">'+getStatus(obj[i].status)+getCode(obj[i].code)+'</h2><h2 >';
		    						   if(obj[i].storeDiscountPrice != 0){
		    						       content +=  '<span class="business">商家优惠</span>';
		    						   }
		    						   if(obj[i].orgDiscountPrice != 0){
		    							   content +=  '<span class="business">平台优惠</span>';
		    						   }
	    						   }else{
	    							   content += ' <li class="fl business-cheap"><h2 class="business-num">￥'+obj[i].refundMoney+'</h2>';
	    						       content +=  '<h2 style="margin-top:.65rem;"><span >'+getStatus(obj[i].status)+'</span>';
	    						   }
	    						   content +=  '  </h2>'
	    						   +  '         </li>'
	    						   +  '     </ul>'
	    						   +  '  </div>';
    					
    				             $("#scroller").append(content);
    				}
    			}else{
    				alert(result.msg);
    			}
    			
    		});
    		
    	}
    	
    	
    	   var page = 1;
    	   var totalPage = 0;
    	   window.onscroll = load;
    	   function load(){
    		   
	    		 if(document.body.scrollTop > 0){
                       $(".statistics").css("box-shadow","0 0 10px #666");
	             }else{
	                   $(".statistics").css("box-shadow","none");
	             }
    	   	     if(page <= totalPage){
    	   	    	 if(document.body.scrollHeight > (page * window.innerHeight -10)){
	    	   			   if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
	    	   				   order(++page);
	    	   			   }
    	   	    	 }
    	   		  }
    	   	}
    	$(function(){
    		$(document).ajaxSend(function(){
        		$(".shape_loading").css({
        			top:$(window).height()/2+"px",
        			left:($(window).width())/2+"px"
        		}).show();
        	}).ajaxComplete(function(){
        		 $(".shape_loading").hide();
        	});
    		
    		
    		order(1);
    	});
</script>