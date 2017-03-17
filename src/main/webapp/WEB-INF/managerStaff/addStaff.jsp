<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>添加员工</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">
        .f{
            background: #FFF;
        }
        .f .row:last-child .input_row{
            border-bottom:none;
        }
        .f:not(:first-child){
            margin-top:.5rem;
        }
        .row .input_row{
            margin:0 1rem;
            padding:.75rem 0;
            display: flex;
            font-size: .85rem;
            border-bottom:1px solid #EEE;
        }
        .input_row > label{
            width:4.3rem;
            color: #666;
        }
        .input_row .input_text{
            flex:1;
            position: relative;
        }
        .input_text input[type=text]{
            width:100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            font-size:.7rem;
        }

        .input_text > label{
            position: absolute;
            left:0;
            top:.15rem;
            font-size: .7rem;
        }
        .input_text > label:last-child {
            left:50%;
        }
        .f1{
            margin-top: .9rem;
            text-align: center;
            background: #fc324a;
            color: #FFF;
        }
        .f1 .btn{
            padding:.6rem 0;
        }
    </style>
</head>
<body>
   <input type="hidden" id="auth" value="${auth}">
   <div class="container">
       <section class="f">
           <div class="row">
               <div class="input_row">
                   <label>员工姓名</label>
                   <div class="input_text">
                       <input type="text" placeholder="真实姓名" autofocus id="name">
                   </div>
               </div>
           </div>
           <div class="row">
               <div class="input_row">
                   <label>员工手机</label>
                   <div class="input_text">
                       <input type="text" placeholder="请填写该员工的手机号码" id="mobile">
                   </div>
               </div>
           </div>
       </section>

       <section class="f">
           <div class="row">
               <div class="input_row">
                   <label>用户名</label>
                   <div class="input_text">
                       <input type="text" placeholder="请输入用户名" id="account">
                   </div>
               </div>
           </div>
           <div class="row">
               <div class="input_row">
                   <label>登录密码</label>
                   <div class="input_text">
                       <input type="text" placeholder="请输入登录密码" id="password">
                   </div>
               </div>
           </div>
       </section>

       <section class="f">
<!--            <div class="row"> -->
<!--                <div class="input_row"> -->
<!--                    <label>退款权限</label> -->
<!--                    <div class="input_text"> -->
<!--                        <label><input type="radio" name="radio" checked>可退款</label> -->
<!--                        <label><input type="radio" name="radio">不可退款</label> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
           <div class="row">
               <div class="input_row">
                   <label>员工角色</label>
                   <div class="input_text">
                       <input type="text" value="收银员" readonly id="roleId" data-value="6">
                   </div>
               </div>
           </div>
       </section>

       <section class="f1" id="add">
           <div class="btn">
               <span>确认添加</span>
           </div>
       </section>
   </div>
</body>
</html>
<script>
  $(function(){
	   var auth = $("#auth").val();
       $("#add").on("click",function(){
//            jalert.show('确认添加');
               var data = {};
               if($("#name").val() == '' || $("#mobile").val() == '' ||
            		   $("#account").val() == '' || $("#password").val() == ''){
            	   jalert.show('请填写完整');
            	   return false;
               }
               //验证账号是否已经被注册
               $.ajax({
            	   url:'./isExistAccount',
            	   method:'post',
            	   async:false,
            	   data:{
            		   account:$("#account").val(),
            		   auth:auth
            	   },
            	   success:function(result){
            		   if(result.code == 0){
		            		   $.post("./saveStoreAccount",{
		         	            	  name:$("#name").val(),
		         	            	  mobile:$("#mobile").val(),
		         	            	  account:$("#account").val(),
		         	            	  password:$("#password").val(),
		         	            	  roleId:$("#roleId").attr("data-value"),
		         	            	  auth:auth
		         	           },function(result){
		         	            	  if(result.code == 0){
		         	            		  jalert.show('添加成功');
		         	            		  location.href="./toManageStaffPage?auth="+auth;
		         	            	  }
		         	           });
            		   }else if(result.code == 2){
            			   jalert.show(result.msg);
            		   }else{
            			   jalert.show(result.msg);
            		   }
            	   }
               });
	           
       });
  });


</script>