<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>提现详情</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style>
        .result,.detail{
            background: #FFF;
        }
        .result .content{
            text-align: center;
        }
        .content .clock{
            padding:1rem 0;
        }
        .content img{
            width:4rem;
            height:4rem;
        }
        .content .tips{
            padding-bottom:1rem;
        }

        .detail{
            margin-top:.5rem;
        }
        .itemlist{
            font-size:.85rem;
        }
        .common {
            margin:0 1rem;
            border-bottom:1px solid #EEE;
            padding:.5rem 0;
        }
        .common .label{
            float: left;
        }
        .common .item_result{
            float: right;
        }


        .btn{
            margin-top: .5rem;
            text-align: center;
            background: #fc324a;
            color: #FFF;
        }
        .btn .complete{
            padding:.6rem 0;
        }
    </style>
</head>
<body>
    <div class="container">
         <section class="result">
               <div class="content">
                    <div class="clock">
                            <img src="../../image/biao.png"/>
                    </div>
                    <div class="tips">
                        <span>提现申请已提交</span>
                    </div>
               </div>
          </section>

        <section class="detail">
            <div class="itemlist">
                  <div class="rowitem">
                      <div class="common clearfix">
                          <div class="label">
                              <span>微信</span>
                          </div>
                          <div class="item_result">
                              <span>微信钱包</span>
                          </div>
                      </div>
                  </div>
                  <div class="rowitem">
                        <div class="common clearfix">
                            <div class="label">
                                <span>提现金额</span>
                            </div>
                            <div class="item_result">
                                <span>￥100.00</span>
                            </div>
                        </div>
                  </div>
            </div>
        </section>

        <section class="btn" id="complete">
             <div class="complete">
                    <span>完成</span>
             </div>
        </section>
    </div>
</body>
</html>

<script>
    $(function(){
        $("#complete").on("click",function(){
            jalert.show('完成');
        })
    })
</script>