<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>选择门店</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style type="text/css">
        .shops{
            background-color:#FFF;
        }
        ul{
            list-style: none;
        }
        ul,li{
            float:none;
            line-height:2em;
            border-bottom:1px solid #DDD;
        }
        li{
            padding: 10px 0;
        }
        li:last-child{
            border:none;
        }
        li span{
            margin-left:20px;
        }
        .right{
            float:right;
            margin-right:20px;
        }
        .f {
            background-color: #fc324a;
            text-align: center;
            color:#FFF;
        }
        .confirm{
            padding:10px 0;
        }
    </style>
</head>
<body>
   <div class="container">
       <section>
           <div class="shops">
               <ul>
                   <c:forEach items="${shopList}" var="store">
                       <li data-id="${store.storeNo}">
                           <span>${store.name}</span>
                       </li>
                   </c:forEach>
                   <li data-id = "9"><span>全部门店</span><span class="right" >√</span></li>
                   <li data-id = "2"><span>门店1</span></li>
                   <li data-id = "3"><span>门店2</span></li>
                   <li data-id = "4"><span>门店3</span></li>
                   <li data-id = "5"><span>门店4</span></li>
               </ul>
           </div>
       </section>

   </div>

   <footer>
       <section class="f" id="confirm">
           <div class="confirm">确认</div>
       </section>
   </footer>
</body>
</html>
<script>
    $(function(){
        load();
    });
    function load(){
        //
        $("#confirm").on("click",function(){
            var shops = $(".shops").find("li");
            var storeNos = "";
            for(var i= 0,len = shops.length;i<len;i++){
                var _shopValue = $(shops[i]).attr("data-val");
                if(_shopValue){
                    storeNos += _shopValue+",";
                }
            }
            alert(storeNos);
        });
        //点击选择门店事件
        $(".shops").find("li").on("click",function(e){
            if($(this).children().length <=1){
                $(this).append("<span class=\"right\" >√</span>");
                $(this).attr("data-val",$(this).attr("data-id"));
                e.stopPropagation();
            }else{
                $(this).children()[1].remove();
                $(this).attr("data-val","");
                e.stopPropagation();
            }
        });
    }
</script>