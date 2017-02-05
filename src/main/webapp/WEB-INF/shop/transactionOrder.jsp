<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>交易详单</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/transactionOrder.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <style>

    </style>
</head>
<body>
    <div class="container">
        <section class="pay">
            <div class="pay-flag">
                <p>实收金额</p>
                <p>
                    <span>￥</span><span>${order.actualChargeAmount}</span>
                </p>
            </div>
        </section>
        <section class="pay f23-size">
            <div class="pay-detail">
                <p class="clearfix">
                    <span class="span-label">应付金额</span>
                    <span class="span-result">￥${order.planChargeAmount}</span>
                </p>
                <p class="clearfix">
                    <span class="span-label">减免</span>
                    <span class="span-result">￥${order.planChargeAmount - order.actualChargeAmount}</span>
                </p>
            </div>
        </section>
        <section class="pay f23-size">
            <div class="pay-detail">
                <p class="clearfix">
                    <span class="span-label">交易时间</span>
                    <span class="span-result" id="_ctime"></span>
                </p>
               <!-- <p class="clearfix">
                    <span class="span-label">支付码</span>
                    <span class="span-result">0879</span>
                </p>-->
                <p class="clearfix">
                    <span class="span-label">付款方式</span>
                    <span class="span-result" id="payMethod"></span>
                </p>
                <p class="clearfix">
                    <span class="span-label">当前状态</span>
                    <span class="span-result" id="payStatus"></span>
                </p>
                <p class="clearfix">
                    <span class="span-label">收款门店</span>
                    <span class="span-result">${order.storeName}</span>
                </p>
            </div>
        </section>
        <section class="pay f4-size">
            <div class="pay-member-info clearfix">
                <span>付款人信息</span>
                <span><img src="../resource/img/app/hongjiantou.png" class="hjt"></span>
            </div>
            <div class="pay-detail" style="padding-top: 0;">
                <p class="clearfix">
                    <span class="span-label">昵称</span>
                    <span class="span-result" id="userName"></span>
                </p>
                <!--<p class="clearfix">
                    <span class="span-label">到店次数</span>
                    <span class="span-result">8次</span>
                </p>
                <p class="clearfix">
                    <span class="span-label">消费总额</span>
                    <span class="span-result">￥200.3</span>
                </p>-->
            </div>
        </section>

    </div>
    <footer>
        <div class="footer" ontouchend="window.close()">
            <span>关闭</span>
        </div>

    </footer>
</body>
</html>
<script>
    var auth = "${auth}";
    $(function(){
        load();
    })

    function load(){
        var time = ${order.ctime*1000};
        var status = ${order.status};
        var payMethod = ${order.payMethod}==1?"微信支付":"支付宝支付";
        $("#_ctime").text(new Date(time).Format("yyyy-MM-dd hh:mm:ss"));
        $("#payMethod").text(payMethod);
        if(status == 1){
            $("#payStatus").text("支付成功");
        }else if(status == 2){
            $("#payStatus").text("支付失败");
        }else if(status == 3){
            $("#payStatus").text("有退款");
        }else{
            $("#payStatus").text("支付中");
        }
        loadUserInfo(${order.userId});

    }

    function loadUserInfo(userId){
        $.post("./searchUserInfo",{"userId":userId,"auth":auth},function(obj){
            if(obj.code == 0){
                $("#userName").text(obj.data.nickName);
            }
        })
    }

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
</script>