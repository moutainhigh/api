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
                $('.tab-shop').css('height',$(window).height()-$('.bottom').height());
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>
</head>
<body style="padding-bottom: 2.6rem;">
<input value="${auth}" id="auth" name="auth" type="hidden">
    <div class="item">
        <!--小店start-->
       <div class="tab-shop"  id="_storeShow">
        </div>
        <!--小店end-->
		 <!--助手start-->
       <div class="tab-shop"  id="_assistantShow">
        </div>
        <!--小店end--> <!--小店start-->
       <div class="tab-shop"  id="_mineShow" >
        </div>
        <!--我的end-->
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
	            		_html+="<li class=\"fl\" onclick=\"loadSecondMenu("+value.id+","+value.type+",'"+value.url+"',this)\">";
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
	
	function loadSecondMenu(id,type,url,obj){
		if(url == ""){
			jalert.show("您没有权限,请联系相关人员开通!!");
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
		$(".tab-shop").hide();
		 if(url == "/merchant/store"){
			 $("#_storeShow").html("");
			$("#_storeShow").show();
			$("#_storeShow").load("./store?id="+id+"&type="+type+"&auth="+_auth);
		}
		if(url == "/merchant/assistant"){
			$("#_assistantShow").html("");
			$("#_assistantShow").show();
			$("#_assistantShow").load("./assistant?id="+id+"&type="+type+"&auth="+_auth);
		}
		if(url == "/merchant/mine"){
			 $("#_mineShow").html("");
			$("#_mineShow").show();
			$("#_mineShow").load("./mine?id="+id+"&type="+type+"&auth="+_auth);
		} 
		
	}
	
	function openWindows(type,url){
		if(type == 99){
			jalert.show("正在建设中,请稍等一段时间!!");
			return;
		}
		if("" == url){
			jalert.show("您没有权限,请联系相关人员开通!!");
			return;
		}
		alert(url);
	}
</script>


<script>
    window.addEventListener( "load", function() {
        FastClick.attach( document.body );
    }, false );
</script>