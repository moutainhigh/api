<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>员工列表</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/store-manager.css"/>
    <link rel="stylesheet" href="../resource/css/app/people-list.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
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
            <span class="fl"><a href="./store?auth=${auth }"><img src="../resource/img/app/store/le-symbol.png"></a></span>
            员工列表
        </h3>
    </nav>
    <div class="middle">
        <ul>
            <c:forEach items="${storeAccountList }" var="account" varStatus="status">
					<li class="clearfix" onclick="toUserInfo('${account.id}')">
		                <div class="fl">
		                    <h3 class="middle-label ">
		                       <strong class="people-img">
		                           <img src="${account.headImg }" onerror="javascript:this.src='../resource/img/merchant/mine-gray.png'"/>
		                       </strong>
		                    </h3>
		                </div>
		                <div class="fl" style="margin-left:.3rem;">
		                    <p class="people-label">
		                        <strong class="people-name">${account.name }</strong>
		                        <c:forEach items="${account.roleList }" var="role">
				                        <span class="people-label-one">${role.name }</span>
		                        </c:forEach>
		                        <span class="people-label-three">
		                        <c:choose>
		                          <c:when test="${account.status == 1 }">在职</c:when>
		                          <c:otherwise>离职</c:otherwise>
		                        </c:choose>
		                        </span>
		                    </p>
		                    <p class="people-number">账号:${account.account }</p>
		                </div>
		                <p class="fr store-detail">
		                    <img src="../resource/img/app/store/le-symbol-gray.png"/>
		                </p>
		            </li>
		   </c:forEach>
        </ul>
    </div>
    <div class="btn">
        <button id="addUser">添加</button>
    </div>
</section>
</body>
</html>
<script type="text/javascript">
 var _auth = $("#auth").val();
 var _storeNo = $("#storeNo").val();
 function toUserInfo(id){
	 location.href = "./userInfo?id="+id+"&storeNo="+_storeNo+"&auth="+_auth;
 }
 $(function(){
	 $("#addUser").on("click",function(){
		location.href="./addUserPage?storeNo="+_storeNo+"&auth="+_auth; 
	 });
	 
 });
</script>
