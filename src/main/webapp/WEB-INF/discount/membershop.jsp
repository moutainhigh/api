<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>会员人数</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>

        .member-list{
            background-color:#ccc;
            padding-top:1px;
            font-size:.7em;
        }
        .member-li{
            line-height:2.4;
            margin-bottom: 5px;
            background-color:#fff;
        }
        .member-li .head-img{
            width:20%;
            float:left;
            position: absolute;
        }
        .head-img img{
            width:60px;
            height:60px;
            border-radius:50%;
            top: 2px;
            position: absolute;
        }
        .member-li .member-detail{
            width:80%;
            float:right;
            font-weight: bold;
            opacity: .5;
        }
        .member-detail div .nick,.member-detail div .count{
            float:left;
        }
        .member-detail div .time,.member-detail div .spendDetail{
            float:right;
        }
        .member-detail div:after,.member-li:after{
            content: " ";
            clear:both;
            display: block;
            visibility: hidden;
            height:0;
            line-height: 0;
        }
        .common-mg{
            margin:0 20px;
        }
        .header-image-pd{
            position: relative;
        }
       i{
           font-style: normal;
       }
        .red{
          color:#fc324a;
        }
        .span{
            margin-right: 20px;
        }
    </style>
</head>
<body>

   <div class="container">
       <section>
           <div class="member-list">
               <div class="member-li">
                   <div class="common-mg">
                       <div class="head-img">
                           <div class="header-image-pd">
                                  <img src="../../image/cat.png" alt="">
                           </div>
                       </div>
                       <div class="member-detail">
                           <div>
                               <div class="nick">小花猫</div>
                               <div class="time">1天前</div>
                           </div>
                           <div>
                               <div class="count">消费5次</div>
                               <div class="spendDetail">
                                   <span class="span">共消费<i>100</i>元</span>
                                   <span>优惠<i class="red">30</i>元</span>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>
               <div class="member-li">
                   <div class="common-mg">
                       <div class="head-img">
                           <div class="header-image-pd">
                               <img src="../../image/cat.png" alt="">
                           </div>
                       </div>
                       <div class="member-detail">
                           <div>
                               <div class="nick">小花猫</div>
                               <div class="time">1天前</div>
                           </div>
                           <div>
                               <div class="count">消费5次</div>
                               <div class="spendDetail">
                                   <span class="span">共消费<i>100</i>元</span>
                                   <span>优惠<i class="red">30</i>元</span>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>
               <div class="member-li">
                   <div class="common-mg">
                       <div class="head-img">
                           <div class="header-image-pd">
                               <img src="../../image/cat.png" alt="">
                           </div>
                       </div>
                       <div class="member-detail">
                           <div>
                               <div class="nick">小花猫</div>
                               <div class="time">1天前</div>
                           </div>
                           <div>
                               <div class="count">消费5次</div>
                               <div class="spendDetail">
                                   <span class="span">共消费<i>100</i>元</span>
                                   <span>优惠<i class="red">30</i>元</span>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>
               <div class="member-li">
                   <div class="common-mg">
                       <div class="head-img">
                           <div class="header-image-pd">
                               <img src="../../image/cat.png" alt="">
                           </div>
                       </div>
                       <div class="member-detail">
                           <div>
                               <div class="nick">小花猫</div>
                               <div class="time">1天前</div>
                           </div>
                           <div>
                               <div class="count">消费5次</div>
                               <div class="spendDetail">
                                   <span class="span">共消费<i>100</i>元</span>
                                   <span>优惠<i class="red">30</i>元</span>
                               </div>
                           </div>
                       </div>
                   </div>
               </div>
               <div class="member-li">
                   <div class="common-mg">
                       <div class="head-img">
                           <div class="header-image-pd">
                               <img src="../../image/cat.png" alt="">
                           </div>
                       </div>
                       <div class="member-detail">
                           <div>
                               <div class="nick">小花猫</div>
                               <div class="time">1天前</div>
                           </div>
                           <div>
                               <div class="count">消费5次</div>
                               <div class="spendDetail">
                                   <span class="span">共消费<i>100</i>元</span>
                                   <span>优惠<i class="red">30</i>元</span>
                               </div>
                           </div>
                       </div>
                   </div>
               </div><div class="member-li">
               <div class="common-mg">
                   <div class="head-img">
                       <div class="header-image-pd">
                           <img src="../../image/cat.png" alt="">
                       </div>
                   </div>
                   <div class="member-detail">
                       <div>
                           <div class="nick">小花猫</div>
                           <div class="time">1天前</div>
                       </div>
                       <div>
                           <div class="count">消费5次</div>
                           <div class="spendDetail">
                               <span class="span">共消费<i>100</i>元</span>
                               <span>优惠<i class="red">30</i>元</span>
                           </div>
                       </div>
                   </div>
               </div>
           </div><div class="member-li">
               <div class="common-mg">
                   <div class="head-img">
                       <div class="header-image-pd">
                           <img src="../../image/cat.png" alt="">
                       </div>
                   </div>
                   <div class="member-detail">
                       <div>
                           <div class="nick">小花猫</div>
                           <div class="time">1天前</div>
                       </div>
                       <div>
                           <div class="count">消费<i>5</i>次</div>
                           <div class="spendDetail">
                               <span class="span">共消费<i>100</i>元</span>
                               <span>优惠<i class="red">30</i>元</span>
                           </div>
                       </div>
                   </div>
               </div>
           </div>




           </div>
       </section>
   </div>
</body>
</html>