<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>创建商户</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/base.css"/>
    <link rel="stylesheet" href="../resource/css/mchAdd/new.css"/>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/320+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>


</head>
<body>
	<input value="${auth}" id="auth" name="auth" type="hidden">
    <div class="item">
        <div class="create-title">
            <div class="create-icon">
                <ul class="clearfix create-icon-lis">
                    <li class="fl create-right">
                        <a href="">
                            <i class="create-box"><img src="../resource/img/mchAdd/chuangjnianshanghu.png" class="tit-icon"/></i><span class="tit-arrow-pos create-box2"><img src="../resource/img/mchAdd/zhihujiantou.png" id="tit-arrow1"/></span>
                            <p class="font-white">创建商户</p>
                        </a>
                    </li>
                    <li class="fl create-right">

                            <i class="create-box3"><img src="../resource/img/mchAdd/jibencailiaoi-2.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow2-2"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow "/></span>
                            <p class="font-gray">基本资料</p>

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
        <div class="create-register">
            <ul>
                <li class="create-register-padd register-border clearfix register-align"><span class="fl register-le" >店&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</span><input type="text" id="storeName" placeholder="请输入店名" class="create-name fr register-ri"/> </li>
                <li class="create-register-padd2 register-border2 clearfix register-align"><span class="fl register-le">登录账号:</span><input type="text" id="storeAccount" placeholder="请输入账号" class=" fr register-ri new-pos"/> </li>
               <li class="create-register-padd clearfix register-align"><span class="fl register-le" >编&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号:</span><input type="number" id="storeNo" placeholder="请输入编号" class="create-name creat-name-pos create-name2 fr register-ri new-pos2" /> </li>
            </ul>
        </div>
        <p class="create-next-box"><input id="_submit" type="button" value="下一步" class="create-next"/></p>
    </div>
</body>
</html>
<script>
 var auth = $("#auth").val();
 $(function(){
     load();
 });

 function load(){
     $("#_submit").on("click",function(){
         _submit();
     });
 };

 function _submit(){
     var storeName = $.trim($("#storeName").val());
     var storeAccount = $.trim($("#storeAccount").val());
     var storeNo = $.trim($("#storeNo").val());

     if(storeName == "" || storeAccount == "" || storeNo==""){
         jalert.show("不能有空值");
         return;
     }
     if(/^[a-zA-Z]*$/.test(storeName)){
     	jalert.show('店名不能是纯字母');
     	return false;
     }
     if(/^[0-9]*$/.test(storeName)){
     	jalert.show("店名不能是纯数字");
     	return false;
     }
     var realLength = 0, len = storeName.length, charCode = -1;
     for (var i = 0; i < len; i++) {
         charCode = storeName.charCodeAt(i);
         if (charCode >= 0 && charCode <= 128) realLength += 1;
         else realLength += 2;
     }
     if(realLength<8){
         jalert.show("商家名称不能少于8个字符");
         return false;
     }
     $("#_submit").css("background","#d3d3d3");
     $("#_submit").unbind("click");
     
     var jsonData = {"storeName":storeName,"storeAccount":storeAccount,"storeNo":storeNo,"auth":auth};
     $.post("./mchAdd",jsonData,function(obj){
         if(obj.code == 0){
             location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
         }else{
             jalert.show(obj.msg);
             $("#_submit").css("background","#fc324a");
             $("#_submit").on("click",function(){
                 _submit();
             });
         }
     });
 }       
 </script>

