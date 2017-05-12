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
</body>
</html>

<script>
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
	            	$("#"+value.title.iconUrl).html(_html);
           	}); 
           	
           }else{
                jalert.show(data.msg);
           }
       });
}
</script>


