<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
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
    <script src="../resource/js/iscroll.js"></script>
    <script src="../resource/js/fastclick.js"></script>
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
           width:80%;
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
	
	    var myScroll;
	
	    function loaded () {
	        myScroll = new IScroll('#wrapper');
	    }
	    /*fastclick.js*/
	    if ('addEventListener' in document) {
	        document.addEventListener('DOMContentLoaded', function() {
	            FastClick.attach(document.body);
	        }, false);
	    }
    </script>
</head>
<body onload="loaded()">
   <div class="item clearfix">
        <div class="title">
            <ul class="title-con clearfix">
                <li class="fl padding-left-no menu-lists">
                    <strong class="pay-type " >支付类型</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png" data-flag="0"/> </span>
                </li>
                <li class="fl menu-lists">
<!--                     <strong  id='demo2' data-options='{"type":"date","beginYear":2000,"endYear":2086}' class="btn">交易时间</strong> -->
                    <strong>交易时间</strong>
                    <span><img  src="../resource/img/app/order/triangle-symbol.png" data-flag="0"/> </span>
                </li>
                <li class="fl menu-lists">
                    <strong class="pay-status ">交易状态</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png" data-flag="0"/> </span>
                </li>
                <li class="fl width-no menu-lists">
                    <strong class="pay-store ">所属门店</strong>
                    <span><img src="../resource/img/app/order/triangle-symbol.png" data-flag="0"/> </span>
                </li>
            </ul>
        </div>
        <div class="shape">
        
        </div>
        <div class="con_content">
	        <ul class="title-lists con">
	            <li data-pay="-1" class="checked_color">全部</li>
	            <li data-pay="1">微信</li>
	            <li data-pay="2">支付宝</li>
	            <li data-pay="3">银联</li>
	        </ul>
	        <ul class="title-lists tran_time">
	            <li>
	               <ul class="clearfix">
	                  <li class="checked_time" data-time="1">今日</li>
	                  <li data-time="2">昨日</li>
	                  <li data-time="3">本周</li>
	               </ul>
	            </li>
	            <li>
	               <ul class="clearfix">
	                  <li data-time="11">上周</li>
	                  <li data-time="12">本月</li>
	                  <li data-time="13">上月</li>
	               </ul>
	            </li>
	            <li>
	               <ul class="clearfix">
	                  <li data-time="21" id='start' data-options='{"type":"date","beginYear":2000,"endYear":2086}' class="btn" data-val="">开始时间</li>
	                  <li data-time="22" id='end' data-options='{"type":"date","beginYear":2000,"endYear":2086}' class="btn" data-val="">结束时间</li>
	               </ul>
	            </li>
	        </ul>
	        <ul class="title-lists con">
	            <li class="checked_color" data-status="-1">全部状态</li>
	            <li data-status="1">已收款</li>
	            <li data-status="2">已退款</li>
	        </ul>
	        <ul class="title-lists con">
	            <li class="checked_color" data-store="-1">全部门店</li>
	            <li>所属门店1</li>
	            <li>所属门店2</li>
	            <li>所属门店3</li>
	            <li>所属门店2</li>
	            <li>所属门店3</li>
	            <li>所属门店2222222222222</li>
	            <li>所属门店3</li>
	            <li>所属门店2</li>
	            <li>所属门店3</li>
	        </ul>
        </div>
        <div class="scroll_box"  id="wrapper">
            <div id="scroller">
                <div class="list wepay-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/wechat-ico.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">微信</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.03</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">商家优惠</h2>
                            <h2 class="business-num">商家收款码</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">支付宝</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="business-num">商家收款码</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">支付宝</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="business-num">商家收款码</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">已撤销</h2>
                        </li>
                    </ul>
                </div>
                <div class="list pos-box">
                    <ul class="clearfix" >
                        <li class="fl">
                            <span><img src="../resource/img/app/order/pay-icon.png"/> </span>
                        </li>
                        <li class="fl">
                            <h2 class="time-user"><span class="type-font">银联</span>用户</h2>
                            <h3 class="pay-time">11:48</h3>
                        </li>
                        <li class="fr">
                            <h2 class=" present-price">￥0.01</h2>
                            <h2 class="old-price">￥0.08</h2>
                        </li>
                        <li class="fr business-cheap">
                            <h2 class="business">POS收款</h2>
                            <h2 class="">最后最后最后</h2>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
<script src="../resource/js/FJL.min.js"></script>
<script src="../resource/js/FJL.picker.min.js"></script>
<script type="text/javascript">
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
    }else{
    	$('.menu-lists').removeClass("checked_time");
    	$(this).find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
        $('.menu-lists').removeClass("checked_time");
    }
    $(".shape").show();
});

$('.con li').on('click',function(){
	var idx = $('.title-lists').index($(this).parent());
	if(($(this).text()).gblen() > 8){
		$($('.menu-lists')[idx]).find("strong").addClass("crement");
	}else{
		$($('.menu-lists')[idx]).find("strong").removeClass("crement");
	}
	$($('.menu-lists')[idx]).find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
    $('.on').text($(this).text());
    $('.title-lists').slideUp();
    $(this).siblings().removeClass("checked_color");
    $(this).addClass("checked_color");
    $('.menu-lists').removeClass("checked_time");
    $(".shape").hide();
});

$('.tran_time .clearfix li').on("click",function(){
	if($(this).attr("data-time") < 20){
		var idx = $('.title-lists').index($(this).parents(".tran_time"));
		$($('.menu-lists')[idx]).removeClass("checked_time").find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
		$('.on').text($(this).text());
		$(this).parents(".tran_time").find("li").removeClass("checked_time");
	    $(this).addClass("checked_time");
        $('.title-lists').slideUp();
        $("#start").attr("data-val","").text("开始时间");
        $("#end").attr("data-val","").text("结束时间");
        $(".shape").hide();
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
                $(that).attr("data-val",rs.text);
                $(that).parents("li").siblings().find("li").removeClass("checked_time");
                $(that).text(rs.text);
                $(that).addClass("checked_time");
                picker.dispose();
                if($("#start").attr("data-val") != "" && $("#end").attr("data-val") != ""){
                	$('.on').text('自定义');
            		$('.title-lists').slideUp();
            		$($('.menu-lists')[1]).removeClass("checked_time").find("img").attr("src","../resource/img/app/order/triangle-symbol.png");
            		$(".shape").hide();
            	}
            });
        }, false);
    });
})(mui,$);
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
</script>