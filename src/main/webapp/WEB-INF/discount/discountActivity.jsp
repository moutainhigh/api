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
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">
        nav{
            position: fixed;
            top: -1px;
            left: 0;
            width: 100%;
            z-index: 9998;
            border-color:#666;
            -webkit-box-shadow:0 0 10px #666;
        }
        .container{
            margin:55px 0;
        }
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
        .tag ul li{
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
        .details{
            margin-top: 10px;
            background: #fc324a;
            text-align: center;
            padding: 10px 0;
            color:#FFF;
        }
        .update,.find-data{
            padding: 2px 10px;
            border: 1px solid #fc324a;
            border-radius: 5px;
            color: #fc324a;
        }
        .delete{
            padding: 2px 15px;
            border: 1px solid #666;
            border-radius: 5px;
            color: #666;
        }
        .stop{
            padding: 2px 10px;
            border: 1px solid #666;
            border-radius: 5px;
            color: #666;
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
        .f4 {
            background-color: #fc324a;
            text-align: center;
            color:#FFF;
        }
        .add{
            padding:10px 0;
            background: url(../resource/img/app/add.png) no-repeat 35% center;
            background-size: 18px;
        }
        .f4{
            border-color:#fc324a;
            -webkit-box-shadow:0 0 10px #fc324a;
        }

    </style>
</head>

<body>
<nav>
    <div class="tap">
        <ul>
            <li class="tap-active">未开始</li>
            <li>进行中</li>
            <li>已结束</li>
        </ul>
    </div>
</nav>
<div class="container">
    <section>
        <div class="content">
            <div class="no-activity">暂无活动，请创建</div>
            <div class="content-detail">
                <div class="tag show">
                    <ul>
                        <li>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>消费满30元享受9折优惠</span>
                                    <span></span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016/12/20 - 2016/12/30</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
                        <li>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>消费满30元享受9折优惠</span>
                                    <span></span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
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
                                    <span>未开始</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
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
                                    <span>未开始</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
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
                                    <span>未开始</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
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
                                    <span>未开始</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="update">修改活动</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>
                        </li>
                    </ul>
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
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="find-data">查看数据</span>
                                    <span class="stop">停止活动</span>
                                </div>
                            </div>

                        </li>
                    </ul>
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
                                    <span>适用门店</span>
                                    <span>全部门店</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span>发布时间</span>
                                    <span class="date">2016-12-20 12:00 - 2016-12-30 12:23</span>
                                </div>
                            </div>
                            <div class="item-row">
                                <div class="row clearfix">
                                    <span class="find-data">查看数据</span>
                                    <span class="delete">删除</span>
                                </div>
                            </div>

                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
</div>
<footer>
    <section class="f4" id="add">
        <div class="add">添加活动</div>
    </section>
</footer>

</body>
</html>
<script type="text/javascript">
    var auth="${auth}";
    var _tag = 0;
    var _objLi;
    var lokered = false;
    var pageNo = 1;

    $(function(){
        //添加活动
        $("#add").on("click",function(){
            location.href = "./reduceSetting?auth="+auth;
        });

        $(".tap ul li").on("click",function(){
            $(".tap ul li").removeClass("tap-active");
            $(this).addClass("tap-active");
            var i = $(".tap ul li").index($(this));
            $(".content-detail .tag").hide()
            $($(".content-detail .tag")[i]).show();
            _objLi = $(".tag ul")[i];
//                $(_objLi).html("");
            searchDiscount(i,pageNo,this);
        });
        _objLi = $(".tag ul")[0];
//            $(_objLi).html("");
        searchDiscount(0,pageNo,$(".tap ul li")[0]);
    });

    function searchDiscount(tag,pageNo,objLi){
        _tag = tag;
        _objLi = objLi;
        $.post("./getDiscountPage",{"auth":auth,"status":_tag,"pageNo":pageNo,"pageSize":10},function(obj){
            if(obj.code == 0){
                var _html = "";
                for(var i=0;i<obj.data.length;i++){
                    var bean = obj.data[i];
                    _html += "<li>";
                    _html += "<div class=\"item-row\">";
                    _html += "<div class=\"row clearfix\">";
                    _html += "<span>"+bean.name+"</span>";
                    _html += "<span></span>";
                    _html += "</div>";
                    _html += "</div>";
                    _html += "<div class=\"item-row\">";
                    _html += "<div class=\"row clearfix\">";
                    _html += "<span>发布时间</span>";
                    _html += "<span class=\"date\">2016/12/20 - 2016/12/30</span>";
                    _html += "</div>";
                    _html += "</div>";
                    _html += "<div class=\"item-row\">";
                    _html += "<div class=\"row clearfix\">";
                    _html += "<span class=\"update\">修改活动</span>";
                    _html += "<span class=\"delete\">删除</span>";
                    _html += " </div>";
                    _html += " </div>";
                    _html += " </li>";
                }
                $(_objLi).append(_html);
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