<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>修改员工</title>
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
                      <input type="text" placeholder="真实姓名"  value="${StoreAccount.name }" id="name">
                  </div>
              </div>
          </div>
          <div class="row">
              <div class="input_row">
                  <label>员工手机</label>
                  <div class="input_text">
                      <input type="text" placeholder="请填写该员工的手机号码" value="${StoreAccount.mobile }" id="mobile">
                  </div>
              </div>
          </div>
      </section>

      <section class="f">
          <div class="row">
              <div class="input_row">
                  <label>用户名</label>
                  <div class="input_text">
                      <input type="text" placeholder="请输入用户名" value="${StoreAccount.account }" id="account" readonly>
                  </div>
              </div>
          </div>
          <div class="row">
              <div class="input_row">
                  <label>登录密码</label>
                  <div class="input_text">
                      <input type="text" placeholder="请输入登录密码" value="" id="password" autofocus>
                  </div>
              </div>
          </div>
      </section>

<!--       <section class="f"> -->
<!--           <div class="row"> -->
<!--               <div class="input_row"> -->
<!--                   <label>退款权限</label> -->
<!--                   <div class="input_text"> -->
<!--                       <label><input type="radio" name="radio" checked>可退款</label> -->
<!--                       <label><input type="radio" name="radio">不可退款</label> -->
<!--                   </div> -->
<!--               </div> -->
<!--           </div> -->
<!--       </section> -->

      <section class="f1" id="modify">
          <div class="btn">
              <span>确认修改</span>
          </div>
      </section>
  </div>
</body>
</html>

<script>
    $(function () {
    	var id = ${StoreAccount.id},auth = $("#auth").val();
    	
         $("#modify").on("click",function(){
        	 var name = $("#name").val(),
        	     mobile = $("#mobile").val(),
        	     account = $("#account").val(),
        	     password = $("#password").val();
        	 if(name == '' || mobile == '' || account == '' || password == ''){
        		 jalert.show('请填写完整');
        		 return false;
        	 }
        	 $.post("./updateStoreAccount",{
        		 auth:auth,
        		 id:id,
        		 name:name,
        		 mobile:mobile,
        		 account:account,
        		 password:password
        	 },function(result){
        		 if(result.code == 2){
        			 jalert.show(result.msg);
        		 }else if(result.code == 0){
        			 jalert.show('修改成功');
        			 location.href = "./toStaffDetailPage?auth="+auth+"&id="+id;
        		 }else{
        			 jalert.show('error');
        		 }
        	 });
        	 
         });
    });
</script>