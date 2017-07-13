<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会员数据</title>
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
        .f1{
            background-color: #fc324a;
            color: #FFF;
            font-size: .9rem;
        }
        .f1 > .common-pd{
             padding:1.5rem 0;
        }
        .f1 > .common-pd ul{
            list-style: none;
        }
        .f1 > .common-pd ul li{
            float:left;
            width:49%;
            text-align: center;
        }
        .f1 > .common-pd ul li:first-child{
            border-right:1px solid #fc9a9a;
        }
        .f1 > .common-pd ul li p:first-child{
            margin-bottom:.75rem;
        }
        .f > .title{
            padding:.5rem 1rem;
            font-size: .8rem;
        }
        .f > .itemlist{
            background: #FFF;
        }
        .f > .itemlist > .item{
            border-bottom:1px solid #EEE;
            padding: .7rem 0;
            color:#666;
            font-size: .75rem;
        }
        .item > .common-pd{
            padding:0 1rem;
        }
        .item > .inner_item > .item-left{
            float:left;
        }
        .item > .inner_item > .item-left > .icon{
            padding:.25rem .5rem;
            background: #fc324a;
            color:#FFF;
            text-align: center;
        }
        .item > .inner_item > .item-left > .text{
            margin-left:.5rem;
        }
        .item > .inner_item > .item-right{
            float:right;
            background: url(../resource/img/app/gengduo.png) no-repeat right center;
            background-size: 20%;
        }
        .item > .inner_item > .item-right span{
            margin-right:1rem;
            color:#333;
        }
        .mei{
            padding: .25rem .315rem !important;
        }
        .money{
            padding:.25rem .07rem !important;
        }
        .footer ul li:nth-child(2){
            color:#fc324a;
        }
        .footer ul li:first-child img{
            width:30px;
        }
        .footer ul li:nth-child(2) img{
            width:21px;
        }
        .footer ul li:last-child img{
            width:22px;
        }
        .footer ul li{
            list-style: none;
            text-align: center;
            width:33.3%;
        }
        li{
            float: left;
        }
        .footer{
            font-size:.6em;
            color:#000
        }
        .footer ul{
            padding:10px 0;
        }
    </style>
</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
  <div class="container">
      <section  class="f1">
           <div class="common-pd">
               <ul class="clearfix">
                   <li>
                       <p>今日新增会员(人)</p>
                       <p>${data.data.todayCount }</p>
                   </li>
                   <li>
                       <p>总会员(人)</p>
                       <p>${data.data.sumCount }</p>
                   </li>
               </ul>
           </div>
      </section>
      <section class="f">
          <div class="title">
              <span>最近</span>
          </div>
          <div class="itemlist">
              <div class="item" data-type="day" data-number="1">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">3</span>
                          <span class="text">3天购买过</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.late1 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="day" data-number="2">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">7</span>
                          <span class="text">7天购买过</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.late2 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="day" data-number="3">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon mei">没</span>
                          <span class="text">7天以上没来过</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.late3 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="day" data-number="4">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon mei">没</span>
                          <span class="text">30天以上没来过</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.late4 }人</span>
                      </div>
                  </div>
              </div>

          </div>

      </section>
      <section class="f">
          <div class="title">
              <span>金额</span>
          </div>
          <div class="itemlist">
              <div class="item" data-type="money" data-number="1">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon money">100</span>
                          <span class="text">0-100元以内</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.money1 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="money" data-number="2">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon money">300</span>
                          <span class="text">100-300元以内</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.money2 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="money" data-number="3">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon mei">￥</span>
                          <span class="text">300元以上</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.money3 }人</span>
                      </div>
                  </div>
              </div>
          </div>

      </section>
      <section class="f">
          <div class="title">
              <span>频次</span>
          </div>
          <div class="itemlist">
              <div class="item" data-type="time" data-number="1">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">1</span>
                          <span class="text">1次</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.time1 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="time" data-number="2">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">2</span>
                          <span class="text">2-4次</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.time2 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="time" data-number="3">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">5</span>
                          <span class="text">5-8次</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.time3 }人</span>
                      </div>
                  </div>
              </div>
              <div class="item" data-type="time" data-number="4">
                  <div class="common-pd clearfix inner_item">
                      <div class="item-left">
                          <span class="icon">9</span>
                          <span class="text">9次以上</span>
                      </div>
                      <div class="item-right">
                          <span>${data.data.time4 }人</span>
                      </div>
                  </div>
              </div>
          </div>

      </section>
  </div>
</body>
</html>
<script>
  $(function(){
	  var auth = $("#auth").val();
	  $(".item").on("click",function(){
// 		  jalert.show($(this).text());
          location.href="../shop/toMdPage?auth="+auth+"&type="+$(this).attr("data-type")+"&number="+$(this).attr("data-number");
	  });
  });
</script>