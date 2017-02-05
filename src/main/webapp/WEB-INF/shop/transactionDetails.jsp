<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
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
    <style type="text/css">
    </style>
</head>
<body>
   <div class="container">
       <section class="f1" id="_searchParam">
           <div class="common-mg pd">
               <div class="tran-month common-pd">
                   <span class="tm">交易当月</span>
                   <span class="m">当月</span>
               </div>
           </div>
       </section>
       <section class="f2">
           <div class="common-mg">
              <div class="common-pd">
                 <span>累计交易:</span><span class="color-read sum" id="totalPrice">0</span>元
              </div>
           </div>
       </section>
       <section class="f3">
           <!--<div class="clearfix ui-border-bottom">
                   <div class="date-day common-pd">
                       <span>2016-07-15</span><span>周五</span>
                   </div>
                   <div class="number-money common-pd">
                        <span>共<span class="color-read">6</span>笔</span><span class="color-read">￥6.00</span>
                   </div>
           </div> -->
        <ul id="_context">
           <!-- <li class="ui-border-bottom">
               <div class="common-mg">
                   <span class="info">
                       <em>1.00</em>
                   </span>
                   <span class="item" >
                       <img src="../resource/img/app/weixinzhifu.png">
                       <div class="cont">
                           <p class="desc"><span>收款码收款</span></p>
                           <p class="time">21:02:56</p>
                       </div>
                   </span>
               </div>
            </li>
            <li class="ui-border-bottom">
                <div class="common-mg">
                   <span class="info">
                       <em>1.00</em>
                   </span>
                    <span class="item" >
                       <img src="../resource/img/app/zhifubao.png">
                       <div class="cont">
                           <p class="desc">
                               <span>收款码收款</span>
                               <span>立减</span>
                               <span><del>￥2.00</del></span>
                           </p>
                           <p class="time">21:02:56</p>
                       </div>
                   </span>
                </div>
            </li>-->


           </ul>
       </section>
       <section class="f4">
           <div class="common-mg">

           </div>
       </section>

   </div>
</body>
</html>
<script>
    var auth = "${auth}";
    var payMethod = "${payMethod}";
    var startTime = "${startTime}";
    var endTime = "${endTime}";
    var status = "${status}";
    var storeNo = "${storeNo}";
    var pageNo = 1;
    var lokered = false;
    var totalPageNo = 0;

    $(function(){
        load();
    })
    function load(){
        //查询条件
        $("#_searchParam").on("touchend",function(){
            location.href = "./toPaystyle?auth="+auth;
        });
        search(pageNo);
    }

    function search(pageNo){
        var json = {"auth":auth,"pageNo":pageNo,"pageSize":10,"payMethod":payMethod,"startTime":startTime,"endTime":endTime,"status":status,"storeNo":storeNo};
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
                    html+="<li class=\"ui-border-bottom\" onclick=\"_detail("+beanObj[i].id+")\">";
                    html+="<div class=\"common-mg\">";
                    html+="<span class=\"info\">";
                    html+="<em>"+beanObj[i].actualChargeAmount+"</em>";
                    html+="</span>";
                    html+="<span class=\"item\" >";
                    if(beanObj[i].payMethod == 1){
                        html+="<img src=\"../resource/img/app/weixinzhifu.png\">";
                    }else{
                        html+="<img src=\"../resource/img/app/zhifubao.png\">";
                    }
                    html+="<div class=\"cont\">";
                    html+="<p class=\"desc\">";
                    html+="<span>收款码收款</span>";
                    if(beanObj[i].planChargeAmount - beanObj[i].actualChargeAmount > 0){
                        html+="<span>立减</span>";
                        html+="<span><del>￥"+parseFloat(beanObj[i].planChargeAmount - beanObj[i].actualChargeAmount).toFixed(2)+"</del></span>";
                    }
                    html+="</p>";
                    html+="<p class=\"time\">"+new Date(beanObj[i].ctime*1000).Format("yyyy-MM-dd")+"</p>";
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

    window.onscroll = pageQuery;
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