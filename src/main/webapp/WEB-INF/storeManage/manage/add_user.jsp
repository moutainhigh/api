<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>添加员工</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/store-manager.css"/>
    <link rel="stylesheet" href="../resource/css/app/people-list.css"/>
    <link rel="stylesheet" href="../resource/css/app/add-people.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/375+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)

    </script>
</head>
<body>
<section>
    <input type="hidden" id="auth" value="${auth }">
    <input type="hidden" id="storeNo" value="${storeNo }">
    <nav>
        <h3 class="">
            <span class="fl"><a href="./userList?storeNo=${storeNo }&auth=${auth }"><img src="../resource/img/app/store/le-symbol.png"></a></span>
            添加员工
        </h3>
    </nav>
    <div class="middle-add">
        <div class="add-box">
            <p class="add-input">
               <label>
	               <span class="add-name letter-spacing">账&emsp;&emsp;号</span>
	               <strong class="add-content"><input type="text" placeholder="账号" id="account"/> </strong>
               </label>
           </p>
            <p class="add-input">
                <label>
	                <span class="add-name">密&emsp;&emsp;码</span>
	                <strong class="add-content"><input type="passWord" placeholder="请输入密码" id="password"/> </strong>
                </label>
            </p>
            <p class="add-input">
                <label>
	                <span class="add-name">确认密码</span>
	                <strong class="add-content"><input type="passWord" placeholder="请确认密码" id="re_password"/> </strong>
                </label>
            </p>
            <p class="add-input">
                <label>
	                <span class="add-name">姓&emsp;&emsp;名</span>
	                <strong class="add-content"><input type="text" placeholder="姓名" id="name"/> </strong>
                </label>
            </p>
            <p class="add-input">
                <label>
	                <span class="add-name">手&ensp;机&ensp;号</span>
	                <strong class="add-content"><input type="tel" placeholder="手机号" id="mobile"/> </strong>
                </label>
            </p>
        </div>
        <div class="middle-bg"></div>
        <div class="middle-check">
            <h2 class="work-tit">
                <span class="worker-img">
                    <img src="../resource/img/app/store/worker.png"/>
                </span>
                <span class="worker-title">职务</span>
            </h2>
            <p class="middle-worker">
                <c:forEach items="${roleList }" var="role">
	                <strong>
	                    <label>
		                    <input type="checkbox" value="${role.id }" name="role"/>
		                    <span>${role.name }</span>
	                    </label>
	                </strong>
                </c:forEach>
            </p>
        </div>
        <div class="middle-bg"></div>
        <div class="middle-check">
            <h2 class="work-tit">
                <span class="worker-img">
                    <img src="../resource/img/app/store/jurisdiction.png"/>
                </span>
                <span class="worker-title">通知</span>
            </h2>
            <p class="middle-worker">
                <c:forEach items="${notifyList }" var="notify">
	                <strong>
	                    <label>
		                    <input type="checkbox" name="notify" value="${notify.id }"/>
		                    <span>${notify.name }</span>
	                    </label>
	                </strong>
                </c:forEach>
            </p>
        </div>
    </div>

    <div class="btn">
        <button id="add">添加</button>
    </div>
</section>
</body>
</html>
<script>
var _auth = $("#auth").val();
  $("#add").click(function(){
	  var roleIds = [], notifyIds = [];
	  var role = $("input[name='role']:checked");
	  var notify = $("input[name='notify']:checked");
	  for(var i = 0,len = role.length;i <len;i++){
		  roleIds.push($(role[i]).val());
	  }
	  for(var i = 0,len = notify.length;i <len;i++){
		  notifyIds.push($(notify[i]).val());
	  }
	  var data = {
			auth:_auth,
			account:$("#account").val(),
			password:$("#password").val(),
			name:$("#name").val(),
			mobile:$("#mobile").val(),
			roleIds:roleIds.toString(),
			notifyIds:notifyIds.toString(),
			storeNo:$("#storeNo").val()
	  };
	  if(data.account == ""){
		  jalert.show("账号不能为空");
		  return;
	  }
	  if(data.password == ""){
		  jalert.show("密码不能为空");
		  return;
	  }
	  if($("#re_password").val() == ""){
		  jalert.show("重复密码不能为空");
		  return;
	  }
	  if(data.password != $("#re_password").val()){
		  jalert.show("俩次输入的密码不同");
		  return;
	  }
	  if(data.name == ""){
		  jalert.show("姓名不能为空");
		  return;
	  }
	  if(data.mobile == ""){
		  jalert.show("手机号不能为空");
		  return;
	  }
	  $.post("./addUser",data,function(result){
		 console.log(result);
		 if(result.code == 0){
			 jalert.show(result.msg);
			 location.href = "./userList?storeNo="+$("#storeNo").val()+"&auth="+$("#auth").val();
		 }else{
			 jalert.show(result.msg);
		 }
	  });
  });
</script>