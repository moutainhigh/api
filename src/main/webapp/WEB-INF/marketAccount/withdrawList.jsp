<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>提现明细</title>
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
        .balance_detail .itemList{
            background: #FFF;
        }
        .itemList .item:not(:last-child){
            border-bottom:1px solid #EEE;
        }
        .item .item_detail{
            margin:0 1rem;
            padding:.6rem 0;
            font-size:.85rem;
            position: relative;

        }
        .item_detail .money{
            position: absolute;
            right:0;
            top:35%;
            color: #fc324a;
        }
        .content p:first-child{
            padding-bottom:.25rem;
            font-weight:600;
            color: #777;
        }
        .content p:last-child{
            font-size:.7rem;
            color: #999;
        }
    </style>
</head>
<body>
  <div class="container">
      <section class="balance_detail">
          <div class="itemList">
              <div class="item">
                  <div class="item_detail">
                      <div class="content">
                          <p>到账</p>
                          <p>2017-02-27 16:13:13</p>
                      </div>
                      <div class="money">
                          <span>+100.00</span>
                      </div>
                  </div>
              </div>
              <div class="item">
                  <div class="item_detail">
                      <div class="content">
                          <p>提现</p>
                          <p>2017-02-27 16:13:13</p>
                      </div>
                      <div class="money">
                          <span>+10000.00</span>
                      </div>
                  </div>
              </div>
              <div class="item">
                  <div class="item_detail">
                      <div class="content">
                          <p>提现</p>
                          <p>2017-02-27 16:13:13</p>
                      </div>
                      <div class="money">
                          <span>+10000.00</span>
                      </div>
                  </div>
              </div>
          </div>

      </section>
  </div>
</body>
</html>