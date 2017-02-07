<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <title>优惠活动页面</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style type="text/css">
        .tap{
            background:#FFF;
            padding:12px 0;
            font-size:18px;
        }
        .tap ul{
            margin:0 20px;
        }
        .tap ul li{
            width:33.3%;
            text-align:center;
            color:#BBB;
            float:left;
        }
        section .content{
            color:#666;
        }
        .no-activity{
            display:none;
        }
        .content-detail{
            display:block;
            font-size: .9em;
        }

        .tag{
            display:none;
        }
        .tag ul{
            background: #FFF;
        }

        .tag li .item-row {
            padding:10px 0;
            border-bottom:1px solid #e9e9e9;
        }
        .tag ul .item-row:last-child{
            border:0;
        }
        .tag li .item-row .row{
            margin:0 20px;
        }
        .tag li .item-row .row span:first-child{
            float:left;
        }
        .tag li .item-row .row span:last-child{
            float:right;
        }
        .date{
            font-size: .8em;
            color:#000;
            line-height: 1.4;
        }
        .show{
            display:block;
        }
        .content-detail li{
            margin-top:10px;
        }
        ul,li{
            list-style:none;
        }
        .update,.details{
            margin-top: 10px;
            background: #fc324a;
            text-align: center;
            padding: 10px 0;
            color:#FFF;
        }
        .walking{
            margin-top: 10px;
            background: #fc324a;
            color:#FFF;
        }
        .walking span{
            display: inline-block;
            width: 48.5%;
            text-align: center;
            padding: 10px;
        }
        .walking span:first-child{
            border-right:1px solid #BBB;
        }
        .tap-active{
            color:#fc324a !important;
        }
        /*.shop-name:after,*/
        ul:after,.clearfix:after {
            content: " ";
            clear:both;
            display: block;
            visibility: hidden;
            height:0;
            line-height: 0px;
        }
       .going{
           color:#fc324a !important;
       }

    </style>
    <script type="text/javascript">
        var auth="${auth}";
        var _tag = 0;
        var _objLi;
        var lokered = false;
        var pageNo = 1;
        $(function(){
            $(".tap ul li").on("click",function(){
                $(".tap ul li").removeClass("tap-active");
                $(this).addClass("tap-active");
                var i = $(".tap ul li").index($(this));
                $(".content-detail .tag").hide()
                $($(".content-detail .tag")[i]).show();
                _objLi = $(".tag ul")[0];
                $(_objLi).html("");
                searchDiscount(i,pageNo,this);
            });
        });

        function searchDiscount(tag,pageNo,objLi){
            _tag = tag;
            _objLi = objLi;
            $.post("./getDiscountPage",{"auth":auth,"status":_tag,"pageNo":pageNo,"pageSize":10},function(obj){
                if(obj.code == 0){
                    var _html = "";
                    for(var i=0;i<obj.data.length;i++){
                        var bean = obj.data[i];
                        _html += "<li>"
                        _html += "<div class=\"item-row\">";
                        _html += "<div class=\"row clearfix\">";
                        _html += "<span>"+bean.name+"</span>";
                        _html += "<span></span>";
                        _html += "</div>";
                        _html += "</div>";
                        _html += "<div class=\"item-row\">";
                        _html += "<div class=\"row clearfix\">";
                        _html += "<span>"+bean.content+"</span>";
                        _html += "<span></span>";
                        _html += "</div>";
                        _html += "</div>";
                        _html += "<div class=\"item-row\">";
                        _html += "<div class=\"row clearfix\">";
                        _html += "<span>发布时间</span>";
                        _html += "<span class=\"date\">"+startTime+" - "+endTime+"</span>";
                        _html += "</div>";
                        _html += "</div>";
                        _html += "</li>";
                    }
                    $(objLi).append(_html);
                    lokered = false;
                }
            });
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

        window.onscroll = pageQuery;
        function pageQuery(){
            if(pageNo <= 1000){
                if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
                    if(lokered){
                        return;
                    }
                    lokered = true;
                    searchDiscount(_tag,++pageNo,_objLi);
                }
            }
        }
    </script>
</head>

<body>
<div class="container">
    <section>
        <div class="tap">
            <ul>
                <li class="tap-active">未开始</li>
                <li>进行中</li>
                <li>已结束</li>
            </ul>
        </div>
        <div class="content">
            <div class="no-activity">暂无活动，请创建</div>
            <div class="content-detail">
                <div class="tag show">
                    <ul>
                        <li>
                        </li>
                    </ul>
                    <div class="update">
                        <span id="updateActivity">修改活动</span>
                    </div>
                </div>
                <div class="tag">
                    <ul>
                        <li>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>黄焖鸡米饭</span>
                                    <span></span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>消费满30元享受9折优惠</span>
                                    <span class="going">进行中</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>

                        </li>
                    </ul>
                    <div class="walking">
                        <span>修改活动</span>
                        <span>停止活动</span>
                    </div>
                </div>
                <div class="tag">
                    <ul>
                        <li>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>黄焖鸡米饭</span>
                                    <span></span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>消费满30元享受9折优惠</span>
                                    <span class="end">已结束</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>

                        </li>
                    </ul>
                    <div class="details">
                        <span>活动详情</span>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>

</body>
</html>
