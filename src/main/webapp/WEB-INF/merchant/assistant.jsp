<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
</head>
<body style="padding-bottom: 2.6rem;">
            <div class="top" style=" width: 100%; height: 5px;background:#EEE ">
            </div>
                            <!--轮播图start-->
	            <div class="swiper-container">
	                <div class="swiper-wrapper">
	                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-one.png"/> </div>
	                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-two.png"/></div>
	                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-three.png"/></div>
	
	                </div>
	                 <!--Add Pagination -->
	                <div class="swiper-pagination"></div>
	
	
	            </div>
	            <!-- 轮播图结束-->
            
            <div class="middle" style="overflow-y: auto;overflow-x:hidden;  width: 100%;">
                <div class="shop-manager" id="_indexModule">
                </div>
            </div>
</body>
</html>

<script>
(function(win,doc){
    function change(){
    	var rem = doc.documentElement.clientWidth*50/375;
        $('.middle').css('height',$(window).height()-$('.top').height()-1.2*rem-$(".swiper-container").height()-5);
    }
    change();
    win.addEventListener('resize',change,false);
})(window,document)

$(function(){
	loadIndexModule(31);
})
function loadIndexModule(id){
	 $.post("../module/getById",{"id":id,"auth":_auth},function(data){
           if(data.code == 0){
           	$.each(data.data,function(index,value){
           		var _html ="";
	            	 _html = "<h1><span class=\"shop-tit\"></span><i class=\"shop-title\">"+value.title.displayName+"<\/i><\/h1>";
	            	 $.each(value.module,function(idx,module){
	            		if(idx%3 == 0){
	            			_html+=" <div class=\"shop-icon clearfix\">";
	            			if(idx >0){
	            				_html+="<\/div>";
	            			}
	            		}
	            		_html+="<div class=\"shop-con shop-border fl\">";
	            		_html+=" <p><img src=\"../resource/img/merchant/"+module.iconUrl+".png\"\/> <\/p>";
	            		_html+="<h2>"+module.displayName+"<\/h2>";
	            		_html+="<\/div>";
	            	})
	            	if(value.module%3 != 0){
	            		_html+="<\/div>";
	            	}
	            	$("#"+value.title.iconUrl).html(_html+_html+_html);
           	}); 
           	
           }else{
                jalert.show(data.msg);
           }
       });
}
var swiper = new Swiper('.swiper-container', {
    pagination: '.swiper-pagination',
    nextButton: '.swiper-button-next',
    prevButton: '.swiper-button-prev',
    slidesPerView: 1,
    paginationClickable: true,
    spaceBetween:0,
    loop: true,
    autoplay:1,
    speed:2000

});
</script>


