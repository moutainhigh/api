<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>提现助手</title>
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
        .box{
            margin:1rem;
            background: #FFF;
            box-shadow: 0 0 5px #CCC;
            border-radius: 5px;
        }
        .box .content{
            margin:.9rem;
        }
        .content .title{
            padding-top:.5rem;
        }
        .title p:first-child{
            font-size:.8rem;
            padding-bottom:.25rem;
            font-weight: 600;
            color: #666;
        }
        .title p:last-child{
            font-size:.65rem;
            color: #999;
        }
        .content .withdraw_money{
            padding:1rem 0 .5rem;
            text-align: center;
            border-bottom:1px solid #EEE;
        }
        .content .withdraw_detail{
            padding:.65rem 0;
            font-size:.8rem;
            border-bottom:1px solid #EEE;
        }
        .withdraw_detail .place p:not(:last-child){
            margin-bottom:.4rem;;
        }
        .place p{
            display: flex;
            width:100%;
        }
        .place p span:first-child{
            width:40%;
            font-weight: 600;
            color: #666;
        }
        .place p span:last-child{
            flex: 1;
            color: #999;
        }
        .watch_detail{
            font-size:.8rem;
            background: url(../../image/app/gengduo.png) no-repeat center right;
            background-size: 3%;
        }
        .watch_detail span{
            padding:.65rem 0;
            display: block;
        }
        </style>
</head>
<body>
    <div class="container">
        <section class="box">
             <div class="content">
                  <div class="title">
                        <p>零钱提现发起</p>
                        <p>2月28日 09:00</p>
                  </div>
                 <div class="withdraw_money">
                     <span>￥100.00</span>
                 </div>
                 <div class="withdraw_detail">
                     <div class="place">
                         <p>
                            <span>提现地点:</span>
                            <span>营销账户</span>
                         </p>
                         <p>
                             <span>提现时间:</span>
                             <span>2017-02-28 12:00:00</span>
                         </p>
                         <p>
                             <span>预计到账时间:</span>
                             <span>2017-02-28 14:00:00</span>
                         </p>
                     </div>
                 </div>
                 <div class="watch_detail">
                      <span>查看详情</span>
                 </div>
             </div>
        </section>
        <section class="box">
            <div class="content">
                <div class="title">
                    <p>零钱提现到账</p>
                    <p>2月28日 09:00</p>
                </div>
                <div class="withdraw_money">
                    <span>￥100.00</span>
                </div>
                <div class="withdraw_detail">
                    <div class="place">
                        <p>
                            <span>提现地点:</span>
                            <span>营销账户</span>
                        </p>
                        <p>
                            <span>提现时间:</span>
                            <span>2017-02-28 12:00:00</span>
                        </p>
                        <p>
                            <span>到账时间:</span>
                            <span>2017-02-28 14:00:00</span>
                        </p>
                    </div>
                </div>
                <div class="watch_detail">
                    <span>查看详情</span>
                </div>
            </div>
        </section>
    </div>
</body>
</html>