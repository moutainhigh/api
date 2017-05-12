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
		if(url == ""){
			jalert.show("您没有权限,请联系管理员开通!!");
			return;
		}
		$('.fl span').removeClass('mine-small-bg');
		$.each($("p.foot-button").find("img"),function(index,_img){
			var _imgUrl = $(_img).attr("src").replace("-green.png","-line.png");
			$(_img).attr("src",_imgUrl);
		});
		$(obj).find("span").addClass("mine-small-bg");
		var imgUrl = $(obj).find("img").attr("src").replace("-line.png","-green.png");;
		$(obj).find("img").attr("src",imgUrl);
		 if(url == "/api/merchant/store"){
			$(".tab-shop").html("");
			$(".tab-shop").load("./store?auth="+_auth);
		}
		if(id == "32"){
			$(".tab-shop").html("");
			$(".tab-shop").load("./assistant?auth="+_auth);
		}
		if(id == "33"){
			$(".tab-shop").html("");
			$(".tab-shop").load("./mine?auth="+_auth);
		} 
		
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