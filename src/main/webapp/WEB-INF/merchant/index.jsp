<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商家管理系统</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" href="../resource/css/merchant/base.css"/>
    <link rel="stylesheet" href="../resource/css/merchant/index.css"/>
    <link rel="stylesheet" href="../resource/css/merchant/swiper.min.css"/>
    <link rel="stylesheet" href="../resource/css/merchant/assistant.css"/>
    <link rel="stylesheet" href="../resource/css/merchant/mine.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
	<script src="../resource/js/swiper.min.js"></script>
	<script src="../resource/js/fast-click.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*50/375+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>
</head>
<body style="padding-bottom: 3.6rem;">
<input value="${auth}" id="auth" name="auth" type="hidden">
    <div class="item">
        <!--小店start-->
       <div class="tab-shop">
            <div class="top">
                <p class="top-tit">首页</p>
            </div>
            <div class="middle">
                <div class="receivables clearfix">
                    <div class="today-money fl">
                        <h3>今日应收金额:</h3>
                        <p>0.00元</p>
                    </div>
                    <div class="today-money fr" style="background: #ffad5c;">
                        <h3>今日实收金额:</h3>
                        <p>0.00元</p>
                    </div>
                </div>
                <div class="trade clearfix">
                    <div class="trade-number trade-border fl">
                        <h2>交易笔数</h2>
                        <p>0</p>
                    </div>
                    <div class="trade-number trade-border  fl">
                        <h2>交易笔数</h2>
                        <p>0</p>
                    </div>
                    <div class="trade-number  fl">
                        <h2>交易笔数</h2>
                        <p>0</p>
                    </div>
                </div>
                <div class="shop-manager">
                    <h1><span class="shop-tit"></span><i class="shop-title">店铺管理</i></h1>
                    <div class="shop-icon clearfix">
                        <div class="shop-con shop-border fl">
                            <p><img src="../resource/img/merchant/shop-manager.png"/> </p>
                            <h2>管理门店</h2>
                        </div>
                        <div class="shop-con shop-border fl">
                            <p><img src="../resource/img/merchant/shop-people-manager-color.png"/> </p>
                            <h2>管理店员</h2>
                        </div>
                        <div class="shop-con fl">
                            <p><img src="../resource/img/merchant/back-money.png"/> </p>
                            <h2>退款审核</h2>
                        </div>
                    </div>
                    <div class="shop-icon clearfix shop-border-no">
                        <div class="shop-con shop-border fl">
                            <p><img src="../resource/img/merchant/money-view.png"/> </p>
                            <h2>查看流水</h2>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <!--小店end-->

        <div class="bottom ">
            <ul class="clearfix footer" id="mainMenu">
            </ul>
        </div>
    </div>
</body>
</html>

<script>
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        slidesPerView: 1,
        paginationClickable: true,
        spaceBetween: 30,
        loop: true
    });
</script>
<script>
	$(function(){
		load();
	})
	
	function load(){
		loadMainMenu();
	}
	
	var _auth = $("#auth").val();
	
	function loadMainMenu(){
		 $.post("../module/getMainMenu",{"auth":_auth},function(data){
	            if(data.code == 0){
	            	var _html = "";
	            	$.each(data.data,function(index,value){
	            		var imgUrl = value.iconUrl;
	            		imgUrl += "-line.png";
	            		_html+="<li class=\"fl\" onclick=\"loadSecondMenu("+value.id+",'"+value.url+"',this)\">";
	            		_html+="<p class=\"foot-button\"><img  src=\"../resource/img/merchant/"+imgUrl+"\" \/><\/p>";
	            		_html+="<span class=\"foot-name mine-color \">"+value.displayName+"</span>";
	            		_html+="<\/li>"
	            	}); 
	            	$("#mainMenu").html(_html);
	            	$("#mainMenu li:first").click();
	            }else{
	                 jalert.show(data.msg);
	            }
	        });
	}
	
	function loadSecondMenu(id,url,obj){
		alert(id+"==="+url+"=="+obj);
	}

    $('.shop').on('touchstart',function(){

        $.ajax({
            url:"index.html",
            cache:true,
            async : true,
            success:function(html){
                $(".tab-shop").html(html);
            }
        });
        $('.foot-one').attr('src','../resource/img/merchant/shop-ico-green.png');
        $('.foot-two').attr('src','../resource/img/merchant/assistant-line.png');
        $('.foot-three').attr('src','../resource/img/merchant/mine-line.png');
        $('.shop span').addClass('mine-small-bg');
        $('.assistant span').removeClass('mine-small-bg');

    });


    $('.assistant').on('touchstart',function(){
        $.ajax({
            url:"assistant.html",
            cache:true,
            async : true,
            success:function(html){
                $(".tab-shop").html(html);
            }
        });

        $('.foot-one').attr('src','../resource/img/merchant/shop-line.png');
        $('.foot-two').attr('src','../resource/img/merchant/assistant.png');
        $('.foot-three').attr('src','../resource/img/merchant/mine-line.png');
        $('.shop span').removeClass('mine-small-bg');
        $('.assistant span').addClass('mine-small-bg');
        $('.mine span').removeClass('mine-small-bg');

    });




    $('.mine').on('touchstart',function(){
        $.ajax({
            url:"mine.html",
            cache:true,
            async : true,
            success:function(html){
                $(".tab-shop").html(html);
            }
        })
        $('.foot-three').attr('src','img/mine.png');
        $('.mine span').addClass('mine-small-bg');
        $('.foot-two').attr('src','img/assistant-line.png');
        $('.assistant span').removeClass('mine-small-bg');

    });
</script>


<script>
    window.addEventListener( "load", function() {
        FastClick.attach( document.body );
    }, false );
</script>