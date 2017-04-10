<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>打印设置</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/printSetting.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.select.js"></script>
    <style>
         input, textarea {
            -webkit-appearance: none;
            -webkit-border-radius: 0;
        }
        #secretKey{
          width:100%;
          display:block;
          border:1px solid #DDD;
          line-height:1.5;
        }
    </style>
</head>
<body>
   <input type="hidden" name="auth" id="auth" value="${auth}">
   <input type="hidden" name="id" id="id">
   <div class="container">
       <section class="f">
            <div class="p-group">
                <p>
                    <span class="span-label">打印联数</span>
                    <span class="span-result"><input type="tel" id="number"></span>
                </p>
                <p >
                    <span class="span-label">设&ensp;备&ensp;号</span>
                    <span class="span-result"><input type="tel" id="deviceId"></span>
                </p>
                <p id="updateHiddenSk">
                    <span class="span-label">设备密钥</span>
                    <span class="span-result"><span id="secretKey">请选择</span></span>
                </p>
            </div>

       </section>

       <section class="f1">
             <div class="submit" id="submit">提交</div>
       </section>

   </div>
</body>
</html>

<script>
 $(function(){
	 //1、验证是否存在打印机2、打印机设备id  是否已经存在 3、添加修改
	 //提交
	 var auth = $("#auth").val();
	 $("#submit").on("click",function(){
		 if("修改"==$.trim($(this).text()) && $("#updateHiddenSk").css("display") == "none"){
			 $("#number").removeAttr("readonly");
			 $("#deviceId").removeAttr("readonly");
			 $("#updateHiddenSk").show();
			 $(this).text("确定");
			 return false;
		 }
		 var number = $.trim($("#number").val()),
		     deviceId = $.trim($("#deviceId").val()),
		     secretKey = $.trim($("#secretKey").attr("data-id"));
		 if(number == ""){
			 jalert.show("打印联数不能为空");
			 return false;
		 }
		 if("" == deviceId){
			 jalert.show("设备号不能为空");
			 return false;
		 }
		 if("" == secretKey){
			 jalert.show("请选择设备秘钥");
			 return false;
		 }
		 var id = $.trim($("#id").val());
		 var data = {
				 auth:auth,
				 number:number,
				 deviceId:deviceId,
				 secretKey:secretKey	 
		 };
		 var url = "";
		 if(id == ''){
			 url = "../printer/add";
		 }else{
			 data.id = id;
			 url = "../printer/update";
		 }
		 $.post(url,data,function(result){
			 console.log(result);
			 if(result.code == 0){
				 jalert.show(result.msg);
				 window.location.reload();
			 }else{
				 jalert.show(result.msg);
			 }
			 
		 });
		 
	 });
	 
	 //加载打印机设置
	 $.post("../printer/getStorePrinter",{
		     auth:auth
		 },function(result){
			console.log(result);
			if(result.code == 0){
				var sp = result.data;
				$("#id").val(sp.id);
				$("#number").val(sp.number).attr("readonly","readonly");
				$("#deviceId").val(sp.deviceId).attr("readonly","readonly");
				$("#secretKey").attr("data-id",sp.secretKey).text(sp.name);
				$("#updateHiddenSk").hide();
				$("#submit").text('修改');
			}else if(result.code == 1){
				jalert.show("加载打印机设置失败");
			}
		 });
	 
	 
	 //选择秘钥
	 $("#secretKey").on("click",function(){
		 jselect.operateObj.defaultsel = $(this).attr("data-id");
		 jselect.operateObj.curObj = $(this);
		 $.post("../printer/getSecertKeyList",{
			auth:auth 
		 },function(result){
			 if(result.code == 0){
	 			   var list = result.data;
	 			   jselect.init();
	 			   for(var i =0;i<list.length;i++){
	 				  jselect.add({
	 					  msg:list[i].name,
	 					  id:list[i].secretKey
	 				  });
	 			   }
	 			  jselect.show();
	 		   }else{
	 			  jalert.show("error");
	 		   }
		 });
	 });
 });

</script>