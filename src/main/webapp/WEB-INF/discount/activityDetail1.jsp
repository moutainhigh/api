<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>活动详情</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>
        .container{
            margin-bottom:44px;
        }
        .f,.f1,.f2{
            background-color: #FFF;
        }
        .f1 .wraper:not(:last-child),
        .f2 .wraper:not(:last-child){
            border-bottom: 1px solid #EEE;
        }
        .all-store{
            background: url(../resource/img/app/gengduo.png) no-repeat right center/12%;
        }
        .all-store p {
            margin-right:20px;
            color:#333;
        }
        .inside{
            margin:0 20px;
        }
        .inside span{
            padding:15px 0;
            display: block;
            color:#666;
            font-size:.9em;
        }
        .f .inside span:first-child,
        .f1 .inside span:first-child{
            float: left;
        }
        .f .inside span:last-child,
        .f1 .inside span:last-child{
            float: right;
        }
        .date{
            font-size: .7em !important;
            color:#111 !important;
        }
        .f1 .wraper:last-child .inside span:last-child p{
            line-height:1.6;
        }
        .f1,.f2{
            margin-top:10px;
        }
        .f2 .wraper p{
            font-size:.75em;
            color:#333;
            margin:0 20px;
        }
        .f2 .wraper p:last-child{
            padding:0 0 15px;
        }
        .f2 .wraper p input{
            width:50px;
            line-height: 1.4;
        }
        .f2 .wraper p input[type=checkbox]{
            -webkit-appearance:none;
            position: relative;
            width:20px;
            height:20px;
            display: block;
            border-radius: 0;
        }
        .f2 .wraper p input[type=text]{
            -webkit-appearance:none;
            border-radius: 0;
            border:1px solid #CCC;
            color:#fc324a;
            text-align: center;
        }
        .fm{
            margin-left:22px;
        }

        .f4 {
            background-color: #fc324a;
            text-align: center;
            color:#FFF;
        }
        .confirm{
            padding:10px 0;
            /*font-weight: 100;*/
        }

        .addRule em{
            height:20px;
            width:20px;
            position:relative;
        }

        .addRule em:before, .addRule em:after{
            content:'';
            height:2px;
            width:15px;
            display:block;
            background:#FFF;
            position:absolute;
            top:11px;
            left:-67px;
        }

        .addRule em:after{
            height:15px;
            width:2px;
            top:4px;
            left:-60px;
        }
        .sel{
            content:'';
            width:20px;
            height:20px;
            background-color: #fc324a;
            display: block;
            position: absolute;
            top:21px;
            left:0;
        }
        .no{
            content:'';
            width:20px;
            height:20px;
            border:1px solid #fc324a;
            background-color: #FFF;
            display: block;
            position: absolute;
            top:20px;
            left:0;
        }
        .default{
            content:'';
            width:20px;
            height:20px;
            border:1px solid #CCC;
            background-color: #FFF;
            display: block;
            position: absolute;
            top:20px;
            left:0;
        }
        .delete{
            content: '';
            width:20px !important;
            height:20px !important;
            background: url(../resource/img/app/chexiao.png) no-repeat center;
            background-size:100% auto;
            display: block;
            position: absolute;
            top: 20px;
            left: 0;
        }
        .end{
           color:#DDD !important;
        }

    </style>

</head>
<body>
<div class="container">
    <section class="f">
        <div class="wraper">
            <div class="inside clearfix">
                <span>固定立减优惠</span>
                <span class="end">已结束</span>
            </div>
        </div>
    </section>

    <section class="f2">
        <div class="wraper">
            <div class="inside">
                <span>优惠规则:</span>
            </div>
        </div>
        <div class="wraper">
            <p class="lj">
                <input type="checkbox" class="radio no">
                <span class="fm">满</span>
                <input type="text" value="100" readonly>
                <span>元，立减</span>
                <input type="text" value="10" readonly>
                <span>元</span>
            </p>
            <p class="lj">
                <input type="checkbox" class="radio no">
                <span class="fm">满</span>
                <input type="text" value="100" readonly>
                <span>元，立减</span>
                <input type="text" value="10" readonly>
                <span>元</span>
            </p>
            <p class="lj">
                <input type="checkbox" class="radio no">
                <span class="fm">满</span>
                <input type="text" value="100" readonly>
                <span>元，立减</span>
                <input type="text" value="10" readonly  >
                <span>元</span>
            </p>
        </div>
    </section>

    <section class="f1">
        <div class="wraper">
            <div class="inside clearfix">
                <span>适用门店</span>
                <span>
                    <p>全部门店</p>
                 </span>
            </div>
        </div>
        <div class="wraper">
            <div class="inside clearfix">
                <span>活动时间</span>
                <span class="date">
                              <p>2016-12-20 12:00 - 2016-12-30 12:23</p>
                          </span>
            </div>
        </div>
    </section>

    <section class="f1">
        <div class="wraper">
            <div class="inside clearfix">
                <span>会员人数</span>
                <span class="all-store">
                    <p>100人</p>
                 </span>
            </div>
        </div>
        <div class="wraper">
            <div class="inside clearfix">
                <span>消费金额</span>
                <span>
                              <p>10000元</p>
                          </span>
            </div>
        </div>
        <div class="wraper">
            <div class="inside clearfix">
                <span>折扣金额</span>
                <span >
                              100元
                          </span>
            </div>
        </div>
    </section>


</div>
<footer>
    <section class="f4" id="confirm">
        <div class="confirm">确认</div>
    </section>
</footer>
</body>
</html>
<script>
    $(function(){
        $("#confirm").on("click",function(){
            alert('确定');
        })
    })
</script>