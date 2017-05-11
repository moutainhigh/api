<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
</head>
<body style="padding-bottom: 3.6rem;">
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
                <div class="shop-manager" id="_indexModule">
                </div>
            </div>
        </div>
</body>
</html>

<script>
	
	
	
	function loadSecondMenu(id,url,obj){
		if(url == ""){
			jalert.show("您没有权限,请联系管理员开通!!");
			//return;
		}
		$('.fl span').removeClass('mine-small-bg');
		$.each($("p.foot-button").find("img"),function(index,_img){
			var _imgUrl = $(_img).attr("src").replace("-green.png","-line.png");
			$(_img).attr("src",_imgUrl);
		});
		$(obj).find("span").addClass("mine-small-bg");
		var imgUrl = $(obj).find("img").attr("src").replace("-line.png","-green.png");;
		$(obj).find("img").attr("src",imgUrl);
		if(id == "31"){
			loadIndexModule(id);
		}
		if(id == "32"){
			$(".item").load("");
		}
		if(id == "33"){
			
		}
		
	}

	
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
		            	$("#"+value.title.iconUrl).html(_html);
	            	}); 
	            	
	            }else{
	                 jalert.show(data.msg);
	            }
	        });
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