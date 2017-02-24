<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>         
<html>
<head>
    <meta charset="UTF-8">
    <title>实名认证</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/realnameauth.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.select.js"></script>
    <style>
          .js_time{
             border:1px solid #EEE;
             /*box-shadow:0 0 5px #EEE;*/
          }
          .js_time span{
             padding:2px 5px;
          }
    </style>
</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
<input value="${storeNo}" id="storeNo" name="storeNo" type="hidden">
     <div class="container">
         <section class="f1">
             <div class="common-mg common-pd">
                 <ul class="clearfix">
                     <li>
                         <p>
                             <img src="../resource/img/chuangjianshanghu.png">
                         </p>
                         <p>
                             <span>创建商户</span>
                         </p>
                     </li>
                     <li>
                      <span>
                          <p>
                              <img src="../resource/img/jibenziliao--zhengchang.png">
                          </p>
                          <p>
                              <span>基本资料</span>
                          </p>
                      </span>
                     </li>
                     <li>
                      <span>
                          <p>
                              <img src="../resource/img/jiesuanxinxi_zhihui.png">
                          </p>
                          <p>
                              <span>结算信息</span>
                          </p>
                      </span>
                     </li>
                     <li>
                      <span>
                          <p>
                              <img src="../resource/img/shenghezhuangtai_zhihu.png">
                          </p>
                          <p>
                              <span>审核状态</span>
                          </p>
                      </span>
                     </li>
                 </ul>
             </div>
         </section>
         <section class="f2">
              <div class="common-br">
                  <div class="info">
                       <span class="name">
                           商户信息
                       </span>
                  </div>
              </div>
              <div class="common-br">
                   <div class="row-label clearfix">
                       <label>费率</label>
                       <span class="sh-right" id="_rate">0.38</span>
                   </div>
              </div>
             <div class="common-br">
                 <div class="row-label clearfix">
                     <label>结算时间</label>
                     <div class="sh-right">
                          <div class="js_time">
                              <span id="_selectRate" data-id="T0">T0</span>
                          </div>
                     </div>
                 </div>
             </div>
             <div class="common-br">
                 <div class="row-label-store-address">
                     <label for="_province">
                                               商户地址
                     </label>
                     <div class="select-pro-city">
                         <ul class="clearfix">
                             <li>
	                             <span id="_province">
	                                                                 请选择省份
	                             </span>
                             </li>
                             <li>
		                         <span id="_city">
		                                                           请选择城市
		                         </span>
		                     </li>
                         </ul>
                     </div>
                 </div>
                 <div class="row-label-address">
                     <label for="_address">
                                                      详细地址
                     </label>
                     <span >
                         <input id="_address" type="text" placeholder="请输入详细地址">
                     </span>
                 </div>
             </div>
             <div class="common-br">
                 <div class="row-label-hytype">
                     <label for="_businessType">行业类型</label>
                 </div>
                 <div class="row-label-hytype-select">
                     <ul>
                        <li>
	                        <span id="first">请选择</span>
                        </li>
                        <li>
	                        <span id="two">请选择</span>
                        </li>
                        <li>
	                        <span id="three">请选择</span>
                         </li>
                     </ul>
                 </div>
             </div>

         </section>
         <section class="f3">
             <div class="common-br">
                 <div class="info">
                       <span class="name">
                          营业执照
                       </span>
                 </div>
             </div>
             <div class="p-group">
                 <p >
                     <label class="span-label" for="_name">法人姓名</label>
                     <span class="span-result"><input type="text" id="_name" placeholder="请输入法人姓名"></span>
                 </p>
                 <p >
                     <label class="span-label" for="_idCard">身份证号</label>
                     <span class="span-result"><input type="text" id="_idCard" placeholder="请输入法人身份证号"></span>
                 </p>
             </div>
         </section>
         <section class="f4" id="_submit">
             <div class="next">下一步</div>
         </section>
     </div>
</body>
</html>
<script>
    var auth = $("#auth").val();
    var storeNo = $("#storeNo").val();
    $(function(){
        load();
    });

    function load(){
        $("#_submit").on("touchend",function(){
            _submit();
        });
        //选择省份
        //_selectCity("_province","0");
        $("#_province").parent("li").on("click",function(){
            jselect.operateObj.defaultsel = $(this).children("span").attr("data-id");
            _selectCity("_province",0,function(){
        		$("#_city").text("请选择城市").removeAttr("data-id");
        	});
        	jselect.show();
        });
        //选择城市
        $("#_city").parent("li").on("click",function(){//第三选择
        	if($("#_province").attr("data-id") == undefined){
        		return false;
        	}
        	jselect.operateObj.defaultsel = $(this).children("span").attr("data-id");
        	_selectCity("_city",$("#_province").attr("data-id"));
        	jselect.show();
        });
        
        //经营类型
        //_selectBusinessType("first",0);//默认加载
        $("#first").parent("li").on("click",function(){//第一选择
        	jselect.operateObj.defaultsel = $(this).children("span").attr("data-id");
        	_selectBusinessType("first",0,function(){
        		$("#two").text("请选择").removeAttr("data-id");
            	$("#three").text("请选择").removeAttr("data-id");
        	});
        	jselect.show();
        });
        
        $("#two").parent("li").on("click",function(){//第二选择
        	if($("#first").attr("data-id") == undefined){
        		return false;
        	}
        	jselect.operateObj.defaultsel = $(this).children("span").attr("data-id");
        	_selectBusinessType("two",$("#first").attr("data-id"),function(){
        		$("#three").text("请选择").removeAttr("data-id");
        	});
        	jselect.show();
        });
        $("#three").parent("li").on("click",function(){//第三选择
        	if($("#two").attr("data-id") == undefined){
        		return false;
        	}
        	jselect.operateObj.defaultsel = $(this).children("span").attr("data-id");
        	_selectBusinessType("three",$("#two").attr("data-id"));
        	jselect.show();
        });
    };
     
    //选择行业类型
    function _selectBusinessType(_id,btpid,fun){
    	$.post("../getListByParentId",{id:btpid},function(result){
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
 			   
 		   }
 	   });
    }
    //费率
    $("#_selectRate").on("click",function(){//第一选择
        	jselect.operateObj.defaultsel = $(this).attr("data-id");
        	jselect.operateObj.curObj = $(this);
        	jselect.init();
        	jselect.add({
				  msg:'T0',
				  id:'T0',
				  exec:function(){
					  $("#_rate").text(0.39);
				  }
			  }).add({
				  msg:'T1',
				  id:'T1',
				  exec:function(){
					  $("#_rate").text(0.40);
				  }
			  });
        	jselect.show();
        });

    function _selectCity(_id,cityCode,fun){
        $.post("../getCityCode",{"cityCode":cityCode},function(data){
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
            }else{
                jalert.show("出错了");
            }
        });
    }

    function _submit(){
        var _rate = $.trim($("#_rate").text());
        var _selectRate = $.trim($("#_selectRate").attr("data-id"));
        var _city = $.trim($("#_city").attr("data-id"));
        var _address = $.trim($("#_address").val());
        var _three = $.trim($("#three").attr("data-id"));
        var _name = $.trim($("#_name").val());
        var _idCard = $.trim($("#_idCard").val());
        if(_rate=="" || _selectRate==""||_city==""||_address==""||_three==""||_name==""||_idCard==""){
            jalert.show("请正确填写");
            return;
        }
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(_idCard) === false)
        {
            jalert.show("身份证输入不合法");
            return  false;
        }

        var jsonData = {"rate":_rate,"settlementType":_selectRate,"cityCode":_city,"address":_address,"businessType":_businessType,"name":_name,"idCard":_idCard,"auth":auth,"storeNo":storeNo};
        $.post("./settlement",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                jalert.show(obj.msg);
            }
        });
    }
</script>