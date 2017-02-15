<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>交易明细</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">

    <link href="../resource/css/manager/transactionDetails.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <link rel="stylesheet" href="../resource/mobiscroll/css/mobiscroll.custom-3.0.0-beta2.min.css">
    <script src="../resource/mobiscroll/js/mobiscroll.custom-3.0.0-beta2.min.js"></script>
    <style type="text/css">
        .color-read{
            color:#fc324a;
        }
        nav{
            position: fixed;
            top: -1px;
            left: 0;
            width: 100%;
            z-index: 9998;
            border-color:#666;

        }
        .f1{
            z-index: 1000;
            position: relative;
        }
        .f2 .all-trancation{
            float:left;
        }
        .f2 .today-trancation{
            float:right;
        }
        .transaction-find{
            position: relative;
            font-size: .95em;
        }
        .more{
            position: absolute;
            right:0;
            width: .5em;
            top: 18%;
            /*transform: rotate(90deg);*/
        }
        .wwt-find-tj{
            position: absolute;
            width:100%;
            background: #FFF;
            font-size: .8em;
            -webkit-box-shadow: 0 2px 5px #666;
            display: none;
        }
        .con-select{
            background: #fc324a;
            color: #FFF;
        }
        .con-noselect{
            background: #EEE;
            color: #666;
        }
        .wwt-find-row,
        .con-jj{
            padding:1em 0 0;
        }
        .wwt-find-row .wwt-find-type{
            width:25%;
            float:left;
            color:#333;
        }
        .wwt-find-row .wwt-find-con{
            width:75%;
            float: right;
            color:#666;
        }
        .wwt-find-con ul{
              list-style: none;
        }
        .wwt-find-con ul li{
            width:33.3%;
            float:left;
            text-align:center;

        }
        .con-time ul li{
            width:50% !important;
        }
        .con-time ul li span{
            padding: 1px 0 !important;
            border-radius: 0 !important;;
        }
        .wwt-find-con ul li span{
            border-radius: 4px;
            padding: 1px 5px;
        }
        .select-store{
            text-align: center;
            background: #EEE;
            position: relative;
        }
        .select-store-default{
            line-height: 1.3;
        }
        .select-store-select{
            position: absolute;
            width:100%;
            background: #EEE;
            display: none;
        }
        .select-store-select ul{
            list-style: none;
        }
        .select-store-select ul li{
            float: none !important;
            width:auto !important;
            padding:5px 0 0;
        }
        .select-store-select ul li span{
            border-bottom: 2px solid #F5F5F5;
            /* padding: 1px 3.5em;*/
        }

        .select-store-select ul li:last-child span{
            border:none;
        }

        .tj-end{
            margin-bottom: 1em;
        }
        .reset-find{
            margin-top: 1em;
        }
        .reset-find ul li {
            width:50% !important;
        }
        .reset-find ul li span{
            padding: 1px 1.2em !important;
            border-radius: 0 !important;
        }
        .f3{
            margin-top:.5em;
        }
        .showContent{
            margin-top:0 !important;
        }
        .selectWeChat{
            background: #12b810;
            color:#FFF;
        }
        .selectAli{
            background: #0095d2;
            color:#FFF;
        }
        .con-time span{
            background: #EEE;
        }
        .con-time input{
            width:90%;
            background: #EEE;
            font-size:.9em;
            text-align: center;
            color:#666;
        }

        :-moz-placeholder { /* Mozilla Firefox 4 to 18 */
            color: #666; opacity:1;
        }

        ::-moz-placeholder { /* Mozilla Firefox 19+ */
            color: #666;opacity:1;
        }

        input:-ms-input-placeholder{
            color: #666;opacity:1;
        }

        input::-webkit-input-placeholder{
            color: #666;opacity:1;
        }
        .inputSpanSelect{
            background: #fc324a !important;
        }
        .inputSelect{
            background: #fc324a !important;
            color:#FFF !important;
        }
    </style>
    <script>
        $(function(){
            //jalert.show($(window).width());
            //jalert.show($(window).innerWidth());
            //jalert.show($(window).clientWidth)
            //jalert.show($("body").css("font-size"));
            //动态的判断nav显示.
            $(".find-content").css({
                "margin-top":$("nav").height()
            });
            //下滑高亮
            /*$(window).scroll(function() {
                //console.log($(this).scrollTop());
                if($(this).scrollTop() >$("nav").height()){//超出nav显示高亮
                    $("nav").css("-webkit-box-shadow","0 0 10px #666");
                }else{
                    $("nav").css("-webkit-box-shadow","none");
                }
            });*/
            window.onscroll = function(){
                if(document.body.scrollTop > 0){
                    if($(".wwt-find-tj").css("display") == 'none'){
                        $("nav").css("-webkit-box-shadow","0 0 10px #666");
                    }
                }else{
                    $("nav").css("-webkit-box-shadow","none");
                }
                pageQuery();
            };
            //点击交易查询  弹出查询框
            //slideDown slideUp
             $("#showSelect").on("touchend",function(){

                 //console.log($(".wwt-find-tj").css("display") );
                 displayResult($(this));
             });

            function displayResult(obj){
                $(".more").css("transform") == 'none'?$(".more").css({
                    'transform':'rotate(90deg)'
                }):$(".more").css("transform","none");
                if(!$("#shape")[0]){
                    $("<div>").css({
                        'position':'fixed',
                        'top':0,
                        'left':0,
                        'width':'100%',
                        'height':'100%',
                        'background':'#EEE',
                        'z-index':888,
                        'opacity':.6
                    }).attr('id','shape').appendTo($("body"));
                }else{
                    $("#shape").fadeToggle("slow","linear");
                }
                obj.parent(".f1").find(".wwt-find-tj").css({
                    "top":$("nav").height()
                });
                obj.parent(".f1").find(".wwt-find-tj").css("display") == 'none'?
                    obj.parent(".f1").find(".wwt-find-tj").fadeIn("slow","linear"):
                    obj.parent(".f1").find(".wwt-find-tj").fadeOut("slow","linear");
            }
             //选择门店
            $(".select-store-default").on("click",function(){
                        $(".select-store-select").css("top",$(this).height()).fadeToggle("slow","linear");
            });
            //点击选择门店事件
            $(".select-store-select").find("li").on("click",function(e){
                $(".select-store-default").text($(this).text()).attr("data-val",$(this).attr("data-id"));
                e.stopPropagation();
                $(".select-store-select").fadeOut("slow","linear");
            });
             //支付类型
            $("#payType").find("li").on("click",function(){
                $("#payType").find("li").children('span').removeClass('con-select').addClass('con-noselect').removeClass('selectWeChat').removeClass('selectAli');
                var index = $("#payType").find("li").index($(this));
                //jalert.show(index);
                if(index == 0){//全部类型
                    $(this).children('span').removeClass('con-noselect').addClass('con-select');
                }else if(index == 1){//微信
                    $(this).children('span').removeClass('con-noselect').addClass('selectWeChat');
                }else{//支付宝
                    $(this).children('span').removeClass('con-noselect').addClass('selectAli');
                }
                //$(this).children('span').removeClass('con-noselect').addClass('con-select');
                //jalert.show($(this).text());
            });
            //交易时间
            $("#transactionDate").find(".true-date").find("li").on("click",function(){
                $("#transactionDate").find(".true-date").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                $(this).children('span').removeClass('con-noselect').addClass('con-select');
                $('#endDate').removeClass("inputSelect").val("").parent("span").removeClass("inputSpanSelect");
                $("#startDate").removeClass("inputSelect").val("").parent("span").removeClass("inputSpanSelect");
                jalert.show($(this).text());
            })
            //交易状态
            $("#status").find("li").on("click",function(){
                $("#status").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                $(this).children('span').removeClass('con-noselect').addClass('con-select');
                jalert.show($(this).text());
            })
            //重置
            $("#reset").on("click",function(){
                $("#payType").find("li").children('span').removeClass('con-select').removeClass('selectWeChat').removeClass('selectAli').addClass('con-noselect');
                $("#transactionDate").find(".true-date").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                $("#status").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                //console.log($("#transactionDate").find(".true-date").find("li:first-child").find("span").html());
                $("#payType").find("li:first-child").children('span').removeClass('con-noselect').addClass('con-select');
                $($("#transactionDate").find(".true-date")[0]).find("li:first-child").find("span").removeClass('con-noselect').addClass('con-select');
                $("#status").find("li:first-child").children('span').removeClass('con-noselect').addClass('con-select');
                $('#endDate').removeClass("inputSelect").val("").parent("span").removeClass("inputSpanSelect");
                $("#startDate").removeClass("inputSelect").val("").parent("span").removeClass("inputSpanSelect");
                jalert.show('重置了')
            });
            //查询
            $("#search").on("click",function(){
                displayResult($("#showSelect"));
                jalert.show('查询');
                //支付类型-s
                var payTypes = $("#payType").find("li"),
                    paySelect = 0;//默认全部
                for(var i= 0,len = payTypes.length;i<len;i++){
                    var _selectPayType = $(payTypes[i]).children("span").attr("class");
                    if(_selectPayType == 'con-select' || _selectPayType == 'selectWeChat' || _selectPayType == 'selectAli'){
                        if(i == 1){//微信
                            paySelect = 1;
                        }else if(i == 2){//支付宝
                            paySelect = 2;
                        }
                        break;
                    }
                }
                payMethod = paySelect;
                //支付类型-e
                //交易时间-s
                if($("#startDate").attr("class").indexOf('inputSelect') > -1  || $("#endDate").attr("class").indexOf('inputSelect') > -1){//优先开始时间判断
                    if($("#startDate").attr("class").indexOf('inputSelect') > -1){
                        startTime = $("#startDate").val();
                    }
                    if($("#endDate").attr("class").indexOf('inputSelect') > -1){
                        endTime = $("#endDate").val();
                    }
                }else{
                    var transactionDates = $("#transactionDate").find(".true-date").find("li");
                    var trueDate = 1;//默认1为今日
                    for(var i = 0;i < transactionDates.length;i++){
                        if($(transactionDates[i]).children('span').attr("class") == 'con-select'){
                            trueDate = i+1;
                            break;
                        }
                    }
                    if(trueDate == 1){//今日
                        var dayTime = new Date(); //上月日期
                        startTime = dayTime.Format("yyyy/MM/dd");
                        dayTime.setDate(dayTime.getDate()+1);
                        endTime = dayTime.Format("yyyy/MM/dd");
                    }else if(trueDate == 2){//昨日
                        var dayTime = new Date(); //上月日期
                        endTime = dayTime.Format("yyyy/MM/dd");
                        dayTime.setDate(dayTime.getDate()-1);
                        startTime = dayTime.Format("yyyy/MM/dd");
                    }else if(trueDate == 3){//本周
                        var dayTime = new Date(); //上月日期
                        dayTime.setDate(dayTime.getDate()-dayTime.getDay()+1);
                        startTime = dayTime.Format("yyyy/MM/dd");
                    }else if(trueDate == 4){//上周
                        var dayTime = new Date(); //上月日期
                        dayTime.setDate(dayTime.getDate()-dayTime.getDay()+1);
                        endTime = dayTime.Format("yyyy/MM/dd");
                        dayTime.setDate(dayTime.getDate()-7);
                        startTime = dayTime.Format("yyyy/MM/dd");
                    }else if(trueDate == 5){//本月
                        var dayTime = new Date(); //上月日期
                        dayTime.setDate(1);
                        startTime = dayTime.Format("yyyy/MM/dd");
                    }else if(trueDate == 6){//上月
                        var dayTime = new Date(); //上月日期
                        dayTime.setDate(1);
                        endTime = dayTime.Format("yyyy/MM/dd");
                        dayTime.setMonth(dayTime.getMonth()-1);
                        startTime = dayTime.Format("yyyy/MM/dd");
                    }
                }
                //交易时间-e
                //交易状态-s
                var status = $("#status").find("li"),selectStatus = 0;//默认0全部
                for(var i = 0,len = status.length;i < len;i++){
                    if($(status[i]).children('span').attr('class') == 'con-select'){
                        selectStatus = i;
                        break;
                    }
                }
                selectStatus = selectStatus==2?3:selectStatus;
                orderStatus = selectStatus;
                //1：已收款2：已退款
                //交易状态-e
                //所属门店-s
                storeNo = $("#store").attr("data-val");
                $("#_context").html("");
                $("#totalPrice").text(0);
                search(1);
            })

            //开始时间和结束时间
            var option = {
                theme: 'ios',
                lang: 'zh',
                display: 'bottom',
                max: new Date(2030, 1, 1)

            };
            //开始时间
            $('#startDate').mobiscroll().date($.extend({
                onBeforeShow:function(event, inst){
                    $('#startDate').addClass("inputSelect").parent("span").addClass("inputSpanSelect");
                },
                onClose:function(){
                    if($('#startDate').val() == ''){
                       $('#startDate').removeClass("inputSelect").parent("span").removeClass("inputSpanSelect");
                    }else{
                        $("#transactionDate").find(".true-date").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                        $('#startDate').addClass("inputSelect").parent("span").addClass("inputSpanSelect");
                    }
                }
            },option));
            //结束时间
            $('#endDate').mobiscroll().date($.extend({
                onBeforeShow:function(event, inst){
                    $('#endDate').addClass("inputSelect").parent("span").addClass("inputSpanSelect");
                },
                onClose:function(){
                    if($('#endDate').val() == ''){
                        $('#endDate').removeClass("inputSelect").parent("span").removeClass("inputSpanSelect");
                    }else{
                        $("#transactionDate").find(".true-date").find("li").children('span').removeClass('con-select').addClass('con-noselect');
                        $('#endDate').addClass("inputSelect").parent("span").addClass("inputSpanSelect");
                    }
                }
            },option));
        })
    </script>
</head>
<body>
<div class="container">
       <!-- 导航查询 -->
       <nav>
           <section class="f1">
               <div class="pd common-mg" id="showSelect">
                   <div class="tran-month common-pd">
                        <div class="transaction-find">
                                交易查询
                               <img src="../resource/img/app/gengduo.png" class="more">
                        </div>
                   </div>
               </div>
               <div class="wwt-find-tj">
                     <div class="wwt-find-row common-mg clearfix" id="payType">
                           <div class="wwt-find-type">支付类型</div>
                           <div class="wwt-find-con">
                               <ul class="clearfix" >
                                   <li>
                                       <span class="con-select">全部类型</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;微信&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;支付宝&nbsp;&nbsp;</span>
                                   </li>
                               </ul>
                           </div>
                     </div>
                     <div class="wwt-find-row common-mg clearfix" id="transactionDate">
                           <div class="wwt-find-type">交易时间</div>
                           <div class="wwt-find-con">
                               <ul class="clearfix true-date">
                                   <li>
                                       <span class="con-select">&nbsp;&nbsp;&nbsp;&nbsp;今日&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;昨日&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;本周&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                               </ul>
                           </div>
                           <div class="wwt-find-con con-jj">
                               <ul class="clearfix true-date">
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;上周&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;本月&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;&nbsp;&nbsp;上月&nbsp;&nbsp;&nbsp;&nbsp;</span>
                                   </li>
                               </ul>
                           </div>
                           <div class="wwt-find-con con-jj con-time">
                                 <ul class="clearfix">
                                     <li>
                                         <span class="">
                                             <!--开始时间-->
                                             <input id="startDate" type="text" placeholder="开始时间">
                                         </span>
                                     </li>
                                     <li>
                                         <span class="">
                                             <!--结束时间-->
                                             <input id="endDate" type="text" placeholder="结束时间">
                                         </span>
                                     </li>
                                 </ul>
                           </div>
                     </div>
                     <div class="wwt-find-row common-mg clearfix" id="status">
                           <div class="wwt-find-type">交易状态</div>
                           <div class="wwt-find-con">
                               <ul class="clearfix">
                                   <li>
                                       <span class="con-select">全部状态</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;已收款&nbsp;&nbsp;</span>
                                   </li>
                                   <li>
                                       <span class="con-noselect">&nbsp;&nbsp;已退款&nbsp;&nbsp;</span>
                                   </li>
                               </ul>
                           </div>
                     </div>
                     <div class="wwt-find-row common-mg tj-end clearfix">
                           <div class="wwt-find-type">所属门店</div>
                           <div class="wwt-find-con">
                               <div class="select-store">
                                   <div class="select-store-default" id="store" data-val="-1">请选择门店</div>
                                   <div class="select-store-select">
                                       <ul>
                                           <c:forEach items="${storeList}" var="store">
                                               <li data-id="${store.storeNo}">
                                                   <span>${store.name}</span>
                                               </li>
                                           </c:forEach>
                                       </ul>
                                   </div>
                               </div>
                           </div>
                           <div class="wwt-find-con reset-find">
                             <ul class="clearfix">
                                 <li id="reset">
                                     <span class="con-noselect">重置</span>
                                 </li>
                                 <li id="search">
                                     <span class="con-select">查询</span>
                                 </li>
                             </ul>
                           </div>
                     </div>

               </div>
           </section>
       </nav>
       <!-- 内容 -->
       <div class="find-content">
           <section class="f2">
               <div class="common-mg">
                  <div class="common-pd clearfix">
                     <div class="all-trancation">
                         <span>累计交易:</span><span class="color-read sum" id="totalPrice">0</span>元
                     </div>
                      <!--<div class="today-trancation">
                          <span>今日交易:</span><span class="color-read sum">6.00</span>元
                      </div>-->
                  </div>
               </div>
           </section>
           <section class="f3 showContent">
               <ul id="_context">


               </ul>
           </section>
       </div>
   </div>
</body>
</html>
<script>
    var auth = "${auth}";
    var payMethod = "${payMethod}";
    var startTime = "${startTime}";
    var endTime = "${endTime}";
    var orderStatus = "${status}";
    var storeNo = "${storeNo}";
    var pageNo = 1;
    var lokered = false;
    var totalPageNo = 0;

    $(function(){
        load();
    })
    function load(){
        search(pageNo);
    }

    function search(pageNo){
        console.log(pageNo);
        var json = {"auth":auth,"pageNo":pageNo,"pageSize":10,"payMethod":payMethod,"startTime":startTime,"endTime":endTime,"status":orderStatus,"storeNo":storeNo};
        $.post("./transactionDetails",json,function(obj){
            if(obj.code == 0){
                var pageObj = obj.data;

                if(pageObj.total <=0){
                    return;
                }
                if(pageNo == 1){
                    $("#totalPrice").text(pageObj.totalPrice);
                    totalPageNo = pageObj.total / 10 +1;
                }

                var html = "";
                var beanObj = pageObj.list;
                for(var i=0;i<beanObj.length;i++){
                    html+="<li onclick=\"_detail("+beanObj[i].id+")\">";
                    html+="<div class=\"common-mg\">";
                    html+=" <span class=\"info refund\">";
                    html+="<em>"+beanObj[i].actualChargeAmount+"</em>";
                    if(beanObj[i].status == 3){
                        html+="<div>-<em class=\"small-lj\">"+beanObj[i].actualChargeAmount+"</em></div>";
                    }
                    html+="</span>";
                    if(beanObj[i].status == 3){
                        html+="<span class=\"item refund\" >";
                        if(beanObj[i].payMethod == 1){
                            html+="<img src=\"../resource/img/app/weixinzhifu-tuikuan.png\">";
                        }else{
                            html+="<img src=\"../resource/img/app/zhifubao-tuikuan.png\">";
                        }
                    }else{
                        html+="<span class=\"item normalpay\" >";
                        if(beanObj[i].payMethod == 1){
                            html+="<img src=\"../resource/img/app/weixinzhifu.png\">";
                        }else{
                            html+="<img src=\"../resource/img/app/zhifubao.png\">";
                        }
                    }
                    html+=" <div class=\"cont\">";
                    html+=" <p class=\"desc\">";
                    if(beanObj[i].status == 3){
                        html+=" <span>全部退款</span>";
                    }else{
                        html+="<span>收款码收款</span>";
                        if(beanObj[i].planChargeAmount - beanObj[i].actualChargeAmount > 0){
                            html+="<span>立减</span>";
                            html+="<span><del>￥"+parseFloat(beanObj[i].planChargeAmount - beanObj[i].actualChargeAmount).toFixed(2)+"</del></span>";
                        }
                    }
                    html+=" </p>";
                    html+=" <p class=\"time\">"+new Date(beanObj[i].ctime*1000).Format("yyyy-MM-dd")+"</p>";
                    html+="</div>";
                    html+="</span>";
                    html+="</div>";
                    html+="</li>";
                }
                $("#_context").append(html);
                lokered = false;
            }
        });
    }

    function _detail(_id){
        window.open("./transactionOrder?auth="+auth+"&id="+_id);
    }

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "y+": this.getYear(),
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

    function pageQuery(){
        if(pageNo <= 1000){
            if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
                if(lokered){
                    return;
                }
                lokered = true;
                search(++pageNo);
            }
        }
    }

</script>
