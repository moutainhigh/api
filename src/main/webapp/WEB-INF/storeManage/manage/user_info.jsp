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
    <title>员工信息</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/store-manager.css"/>
    <link rel="stylesheet" href="../resource/css/app/people-list.css"/>
    <link rel="stylesheet" href="../resource/css/app/add-people.css"/>
    <link rel="stylesheet" href="../resource/css/app/worker-info.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/375+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document);

    </script>
    <style type="text/css">
       .add-content input{
          border:0 !important;
       }
    </style>
</head>
<body>
<section>
    <input type="hidden" id="auth" value="${auth }">
    <input type="hidden" id="accountId" value="${map['storeAccount'].id }">
    <input type="hidden" id="storeNo" value="${map['storeNo'] }">
    <nav>
        <h3  class="">
            <span class="fl"><a href="./userList?storeNo=${map['storeNo'] }&auth=${auth }"><img src="../resource/img/app/store/le-symbol.png"></a></span>
                       员工信息
        </h3>
    </nav>
    <div class="middle-add">
        <div class="add-box">
            <p class="add-input">
               <span class="add-name letter-spacing">账&emsp;&emsp;号</span>
               <strong class="add-content">
                  <input type="text" value="${map['storeAccount'].account }" disabled/> 
               </strong>
           </p>
            <p class="add-input">
                <span class="add-name">姓&emsp;&emsp;名</span>
                <strong class="add-content">
                  <input type="text" value="${map['storeAccount'].name }" disabled/> 
                </strong>
            </p>
            <p class="add-input">
                <span class="add-name">手&ensp;机&ensp;号</span>
                <strong class="add-content">
                   <input type="text" value="${map['storeAccount'].mobile }" disabled/> 
                </strong>
            </p>
        </div>
        <div class="middle-bg"></div>
        <div class="middle-check">
            <h2 class="work-tit">
                <span class="worker-img">
                    <img src="../resource/img/app/store/work-status.png"/>
                </span>
                <span class="worker-title">员工状态</span>
            </h2>
            <p class="middle-worker">
                
                <strong>
                    <label>
	                        <c:choose>
				                <c:when test="${map['storeAccount'].status == 1 }">
				                    <input type="checkbox" name="status" id="status"/>
				                    <span>
							                                     离职
						            </span>
				                </c:when>
				                <c:otherwise>
				                    <input type="checkbox" name="status" id="status"/>
				                    <span>
				                                                上岗
						            </span>
				                </c:otherwise>
			                </c:choose>
                    </label>
                </strong>
                <strong>
                    <label>
	                    <input type="checkbox" name="valid" value="0" id="valid"/>
	                    <span>删除</span>
                    </label>
                </strong>
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
                <c:forEach items="${map['roleList'] }" var="role">
                    <strong>
	                    <label>
		                    <input type="checkbox" value="${role.id }" name="role" <c:if test="${role.flag == 1 }">checked</c:if>/>
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
                <span class="worker-title">权限</span>
            </h2>
            <p class="middle-worker">
                <c:forEach items="${map['notifyList'] }" var="notify">
	                <strong>
	                    <label>
		                    <input type="checkbox" value="${notify.id }" name="notify" <c:if test="${notify.flag == 1 }">checked</c:if>/>
		                    <span>${notify.name }</span>
	                    </label>
	                </strong>
                </c:forEach>
            </p>
        </div>
    </div>

    <div class="btn">
        <button id = "modify">修改</button>
    </div>
</section>
</body>
</html>
<script>
  $(function(){
	  var _auth = $("#auth").val();
	  var _storeNo = $("#storeNo").val();
	  $("#modify").click(function(){
		  var roleIds = [],notifyIds = [];
		  var role = $("input[name='role']:checked");
		  for(var i = 0,len = role.length;i< len;i++){
			  roleIds.push($(role[i]).val());
		  }
		  var notify = $("input[name='notify']:checked");
		  for(var i = 0,len = notify.length;i<len;i++){
			  notifyIds.push($(notify[i]).val());
		  }
		  var data = {
				  auth:_auth,
				  storeNo:_storeNo,
				  accountId:$("#accountId").val(),
				  status:$("#status").is(":checked")?0:1,
				  valid:$("#valid").is(":checked")?0:1,
				  roleIds:roleIds.toString(),
				  notifyIds:notifyIds.toString()
		  };
		  $.post("./modifyUser",data,function(result){
			 if(result.code == 0){
				 jalert.show(result.msg);
				 location.href = "./userList?storeNo="+$("#storeNo").val()+"&auth="+$("#auth").val();
			 }else{
				 jalert.show(result.msg);
			 }
		  });
	  });
	  
  });
</script>