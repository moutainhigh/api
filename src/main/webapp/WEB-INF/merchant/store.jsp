<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
</head>
<body style="padding-bottom: 2.6rem;">
<input id="_storeId" value="${storeId}" type="hidden"/>
<input id="_type" value="${type}" type="hidden"/>
            <div class="top" style=" width: 100%; height: 0rem; ">
            </div>
            <div class="middle" style="overflow-y: auto; width: 100%;">
                <div class="receivables clearfix">
                    <div class="today-money fl">
                        <h3>今日应收</h3>
                        <p>${map['orderSta'].pm }</p>
                    </div>
                    <div class="today-money fr" style="background: #6ebcce;">
                        <h3>今日实收</h3>
                        <p>${map['orderSta'].am - map['refundSta'].refundMoney }</p>
                    </div>
                </div>
                <div class="trade clearfix">
                    <div class="trade-number trade-border fl">
                        <h2>减免金额</h2>
                        <p>${map['orderSta'].orgDiscount + map['orderSta'].storeDiscount }</p>
                    </div>
                    <div class="trade-number trade-border  fl">
                        <h2>交易笔数</h2>
                        <p>${map['orderSta'].count }</p>
                    </div>
                    <div class="trade-number trade-border  fl">
                        <h2>退款金额</h2>
                        <p>${map['refundSta'].refundMoney }</p>
                    </div>
                    <div class="trade-number  fl">
                        <h2>退款笔数</h2>
                        <p>${map['refundSta'].refundCount }</p>
                    </div>
                </div>
                <div class="shop-manager" id="_indexModule">
                </div>
            </div>
</body>
</html>

<script>
(function(win,doc){
    function change(){
    	var rem = doc.documentElement.clientWidth*50/375;
        $('.middle').css('height',$(window).height()-$('.top').height()-1.2*rem-5);
    }
    change();
    win.addEventListener('resize',change,false);
})(window,document)

$(function(){
	loadIndexModule($("#_storeId").val());
})
function loadIndexModule(id){
	 $.post("../module/getById",{"id":id,"auth":_auth},function(data){
		if(data.code == 0){
			var _html ="";
           	$.each(data.data,function(index,value){
				_html += "<h1><span class=\"shop-tit\"></span><i class=\"shop-title\">"+value.title.displayName+"<\/i><\/h1>";
	            $.each(value.module,function(idx,module){
            		if(idx%3 == 0){
            			_html+=" <div class=\"shop-icon clearfix\">";
            			if(idx >0){
            				_html+="<\/div>";
            			}
            		}
            		_html+="<div class=\"shop-con shop-border fl\" onclick=openWindows("+module.type+",'"+module.url+"')>";
            		_html+=" <p><img src=\"../resource/img/merchant/"+module.iconUrl+".png\"\/> <\/p>";
            		_html+="<h2>"+module.displayName+"<\/h2>";
            		_html+="<\/div>";
            	})
            	if(value.module%3 != 0){
            		_html+="<\/div>";
            	}
           	}); 
           	$("#_indexModule").html(_html);
		}else{
            jalert.show(data.msg);
		}
	});
}


</script>


