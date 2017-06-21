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
    <title>门店管理</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/store-manager.css"/>
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
</head>
<body>
    <input type="hidden" id="auth" value="${auth }">
    <section>
        <nav>
            <h3 class="">
                <span class="fl"><a href="./index?auth=${auth }"><img src="../resource/img/app/store/le-symbol.png"></a></span>
                        门店管理
            </h3>
            <p class="clearfix">
                <span class="fl store-number">
                    <img src="../resource/img/app/store/store-number.png"/>
                </span>
                <span class="fl">门店数</span>
                <span class="fr">${storeStaList.size() }个</span>
            </p>
        </nav>
        <div class="middle">
            <ul>
                    <c:forEach items="${storeStaList }" var="storeSta" varStatus="status">
						   
								  <li class="clearfix" onclick="toUserList('${storeSta.storeNo}')">
                                        <div class="fl">
					                        <h3 class="middle-label ">
					                            <strong class="company-name">${storeSta.name }</strong>
					                            <span class="company-label-one">
					                               <c:choose>
						                                <c:when test="${storeSta.parentNo == 0 }">总店</c:when>
						                                <c:otherwise>分店</c:otherwise>
					                               </c:choose>
					                            </span>
					                            <span class="company-label-two">
					                                 <c:choose>
						                                 <c:when test="${storeSta.status == 1 }">营业中</c:when>
						                                 <c:otherwise>已停业</c:otherwise>
					                                 </c:choose>
                                                </span>
					                        </h3>
					                        <p class="">交易笔数:${storeSta.transCount }</p>
					                        <p class="">交易流水:￥${storeSta.transSumMoney }</p>
					                    </div>
					                    <p class="fr store-detail">
					                        <img src="../resource/img/app/store/le-symbol-gray.png"/>
					                    </p>
					                </li>  
						    
					</c:forEach>
            </ul>
        </div>
        <div class="btn">
            <button>添加门店</button>
        </div>
    </section>
<script>
    var _auth = $("#auth").val();
    $('button').on('click',function(){
        jalert.show("请从pc端添加门店");
    });
    function toUserList(storeNo){
//     	jalert.show(storeNo);
        location.href = "./userList?storeNo="+storeNo+"&auth="+_auth;
    }
</script>
</body>
</html>