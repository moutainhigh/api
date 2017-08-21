<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>基本资料</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/base.css"/>
    <link rel="stylesheet" href="../resource/css/mchAdd/mchInfo.css"/>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.select.js"></script>
    
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/320+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>
	<style type="text/css">
		.busi-address{font-size:0.6rem}
		.industry-type{font-size:0.6rem}
		.baseborder2{font-size:0.5rem;height:1.2rem}
		.baseborder{font-size:0.5rem;}
		.base-tab2{padding-top:0.5rem;}
	</style>
</head>
<body>
<div class="item">
	<input value="${auth}" id="auth" name="auth" type="hidden">
	<input value="${info.storeNo}" id="storeNo" name="storeNo" type="hidden">
    <div class="create-title">
        <div class="create-icon">
            <ul class="clearfix create-icon-lis">
                <li class="fl create-right">
                       <i class="create-box"><img src="../resource/img/mchAdd/chuangjnianshanghu.png" class="tit-icon"/></i><span class="tit-arrow-pos create-box2"><img src="../resource/img/mchAdd/zhihujiantou.png" id="tit-arrow1"/></span>
                       <p class="font-white">创建商户</p>
                </li>
                <li class="fl create-right">

                    <i class="create-box3"><img src="../resource/img/mchAdd/jibenziliao-1.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow2-2"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow "/></span>
                    <p class="font-white">基本资料</p>

                </li>
                <li class="fl create-right">
                    <i class="create-box3"><img src="../resource/img/mchAdd/jiesuanxinxi-2.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow3-3"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow"/></span>
                    <p class="font-gray">结算信息</p>
                </li>
                <li class="fl">
                    <i class="create-box4"><img src="../resource/img/mchAdd/shenhetongguo-2.png" class="tit-icon2"/></i>
                    <p class="font-gray">审核状态</p>
                </li>
            </ul>
        </div>
    </div>
    <!--公共头部结束-->
    <div class="base-con">
    		<div class="base-tab2 clearfix">
                <h3 class="fl busi-address">营业执照名称:</h3><div class="base-tab-le fl"><input type="text" id="_mchnt_name" placeholder="与营业执照上相同" value="${info.mchnt_name}"  class="baseborder2 add "/></div>
            </div>
        	<h3 class="fl busi-address">商户地址:</h3>
            <div class="base-tab clearfix">
                <input id="_province" type="text" placeholder="请输入省份" value="" data-id=""  readonly="true" class="baseborder province"/>
                <input id="_city" type="text" placeholder="请输入城市" value="" data-id="" readonly="true" class="baseborder city" />
                <input id="_county" type="text" placeholder="请输入区/县" value="" data-id="" readonly="true" class="baseborder province" />
                <input id="_street" type="text" placeholder="请输入街道" value="" data-id="" readonly="true" class="baseborder city" />
            </div>
            <div class="base-tab2 clearfix">
                <h3 class="fl busi-address">详细地址:</h3><div class="base-tab-le fl"><input type="text" id="_address" value="${info.address }" placeholder="请输入详细地址" class="baseborder2 add "/></div>
            </div>
            <!--行业类型-->
       		 <h3 class="industry-type ">经营类别:</h3>
            <div class="business-type">
				<input id="_first" type="text" placeholder="行业大类" value="" readonly="true" class="baseborder province"/>
                <input id="_second" type="text" placeholder="二级类目" value="" readonly="true" class="baseborder city" />
                <input id="_three" type="text" placeholder="类目" value="" readonly="true" class="baseborder province" />
            </div>
			<div class="base-tab2 clearfix">
                <h3 class="fl busi-address">法&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;人:</h3><div class="base-tab-le fl"><input type="text" id="_contactsPeople" placeholder="请输入法人姓名" value="${info.contact_person }"  class="baseborder2 add "/></div>
            </div>
            <div class="base-tab2 clearfix">
                <h3 class="fl busi-address">身份证号:</h3><div class="base-tab-le fl"><input type="text" id="_idCard" placeholder="请输入法人人身份证号" value="${info.certif_id }" class="baseborder2 add "/></div>
            </div>
            <div class="base-tab2 clearfix">
                <h3 class="fl busi-address">电&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;话:</h3><div class="base-tab-le fl"><input type="text" id="_phone" value="${info.contact_mobile }" placeholder="请输入联系人电话" class="baseborder2 add "/></div>
            </div>
            <div class="base-tab2 clearfix">
                <h3 class="fl busi-address">客服电话:</h3><div class="base-tab-le fl"><input type="text" id="_contact_phone" value="${info.contact_phone }" placeholder="请输入客服电话" class="baseborder2 add "/></div>
            </div>
            <div class="base-tab2 clearfix">
                <h3 class="fl busi-address">邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱:</h3><div class="base-tab-le fl"><input type="text" id="_email" value="${info.contact_email}" placeholder="请输入联系人邮箱" class="baseborder2 add "/></div>
            </div>
   		</div>
     		
        <!--下一步-->
        <div class="next-bg">
	        <div class="next-box">
	            <input id="_submit" type="button" value="下一步" class="industy-next"/>
	         </div>
        </div>
        
</div>

</body>
</html>
<script>

$(function(){
	load();
})

function load(){
	 $("#_submit").on("click",function(){
         _submit();
     });
	
	$("#_province").on("click",function(){
		_selectCity("_province",0,function(){backfill("_province")});
    });
    //选择城市
    $("#_city").on("click",function(){//第三选择
    	if($("#_province").attr("data-id") == undefined){
    		jalert.show("请选择省");
    		return false;
    	}
    	_selectCity("_city",$("#_province").attr("data-id"),function(){backfill("_city")});
    });
  //选择城市
    $("#_county").on("click",function(){//第三选择
    	if($("#_city").attr("data-id") == undefined){
    		jalert.show("请选择市");
    		return false;
    	}
    	_selectCity("_county",$("#_city").attr("data-id"),function(){backfill("_county")});
    });
   //选择城市
    $("#_street").on("click",function(){//第三选择
    	if($("#_county").attr("data-id") == undefined){
    		jalert.show("请选择区/县");
    		return false;
    	}
    	_selectCity("_street",$("#_county").attr("data-id"),function(){backfill("_street")});
    });
	
    $("#_first").on("click",function(){//第1选择
    	_selectBusinessType("_first",0,function(){backfill("_first")});
    });
    
    $("#_second").on("click",function(){//第2选择
    	if($("#_first").attr("data-id") == undefined){
    		jalert.show("请选择行业大类");
    		return false;
    	}
    	_selectBusinessType("_second",$("#_first").attr("data-id"),function(){backfill("_second")});
    });
    
    $("#_three").on("click",function(){//第3选择
    	if($("#_second").attr("data-id") == undefined){
    		jalert.show("请选择二级类目");
    		return false;
    	}
    	_selectBusinessType("_three",$("#_second").attr("data-id"),function(){backfill("_three")});
    });
    
}

function _submit(){
	 var _mchnt_name =  $.trim($("#_mchnt_name").val());
	 
	 var _province = $.trim($("#_province").attr("data-id"));
	 var _city = $.trim($("#_city").attr("data-id"));
	 var _county = $.trim($("#_county").attr("data-id"));
	 var _street = $.trim($("#_street").attr("data-id"));
     var _address = $.trim($("#_address").val());
     
     var _three = $.trim($("#_three").attr("data-id"));
     
     var contactsPeople = $.trim($("#_contactsPeople").val());
     var phone = $.trim($("#_phone").val());
     var contact_phone = $.trim($("#_contact_phone").val());
     var email = $.trim($("#_email").val());
     var idCard = $.trim($("#_idCard").val());

     if( _mchnt_name=="" ){
         jalert.show("请填写商家名称");
         return;
     }
     
     if( _province=="" || _city=="" || _county=="" || _address==""){
         jalert.show("请填写地址信息");
         return;
     }
     
     if( _three=="" ){
         jalert.show("请填写经营类别");
         return;
     }
     
     if( contactsPeople=="" || phone=="" || email=="" || idCard=="" || contact_phone == ""){
         jalert.show("请填写联系人信息");
         return;
     }
     var myreg = /^(1+\d{10})$/;
     if(!myreg.test(phone)) {
         jalert.show('请输入有效的电话号码！');
         return false;
     }
     
     var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
     if(reg.test(idCard) === false)
     {
         jalert.show("身份证输入不合法");
         return  false;
     }
     
     re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
     if(!re.test(email)){
         jalert.show("邮箱填写错误");
         return;
     }
     $("#_submit").css("background","#d3d3d3");
     $("#_submit").unbind("click");
     var _lat = 0;
     var _lon = 0;
     var auth = $("#auth").val();
     var storeNo= $("#storeNo").val();
     
    var jsonData = {"mchnt_name":_mchnt_name,
    				"province":_province,"city":_city,"county":_county,"street":_street,
    				"address":_address,"lat":_lat,"lon":_lon,
    				"business":_three,
    				"contact_person":contactsPeople,"contact_mobile":phone,"contact_phone":contact_phone,"contact_email":email,"certif_id":idCard,
    				"storeNo":storeNo,"auth":auth};
     $.post("./mchUpdate",jsonData,function(obj){
         if(obj.code == 0){
             location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
         }else{
        	 $("#_submit").css("background","#fc324a");
             $("#_submit").on("click",function(){
                 _submit();
             });
             jalert.show(obj.msg);
         }
     });
}

function backfill(_id){
	var _text = $("#"+_id).text();
	$("#"+_id).text("");
	$("#"+_id).val(_text);
}
function _selectCity(_id,cityCode,fun){
    $.post("../getCityCode",{"cityCode":cityCode},function(data){
    	jselect.operateObj.defaultsel = $(this).attr("data-id");
    	jselect.operateObj.curObj = $("#"+_id);
        if(data.code == 0){
        	jselect.init();
            $.each(data.data,function(n,obj) {
                jselect.add({
					  msg:obj.name,
					  id:obj.code,
					  exec:fun
				  });
            });
            if(_id == '_street'){
            	 jselect.add({
   				  msg:'其它',
   				  id:'-1',
   				  exec:fun
   			  });
            }
            jselect.show();
        }else{
            jalert.show("出错了");
        }
    });
}

//选择行业类型
function _selectBusinessType(_id,btpid,fun){
	$.post("../getListByParentId",{id:btpid},function(result){
		jselect.operateObj.defaultsel = $(this).attr("data-id");
		  jselect.operateObj.curObj = $("#"+_id);
		   if(result.code == 0){
			   var list = result.data;
			   jselect.init();
			   for(var i =0;i<list.length;i++){
				  jselect.add({
					  msg:list[i].name,
					  id:list[i].id,
					  exec:fun
				  });
			   }
			   jselect.show();
		   }
	   });
}

</script>