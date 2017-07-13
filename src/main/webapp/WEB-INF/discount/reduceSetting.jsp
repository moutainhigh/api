<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>立减功能设置</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <link rel="stylesheet" href="../resource/mobiscroll/css/mobiscroll.custom-3.0.0-beta2.min.css">
    <script src="../resource/mobiscroll/js/mobiscroll.custom-3.0.0-beta2.min.js"></script>
    <script src="../resource/js/reduceSet.js"></script>
    <style>
        .container{
            margin-bottom:44px;
        }
        input[type=number]{
            border: 1px solid #EEE;
            -webkit-appearance:none;
            border-radius: 0;
        }
        .f1,.f2{
            background-color: #FFF;
        }
        .f1 .wraper:not(:last-child),
        .f2 .wraper:not(:last-child){
            border-bottom: 1px solid #EEE;
        }
        .d-group{
            margin:0 20px;
            font-size: .7em;
        }
        .d-group .row{
            padding:10px 0;
            border-bottom: 1px solid #EEE;
            display: -webkit-flex;
            color:#666;
        }
        .d-group .row:last-child{
            border-bottom: 0;
        }
        .d-result .aname{
            border: none;
            line-height: 2.2;
            width:100%;
            text-align: center;
            background: #EEE;
            font-size: .9em;
        }
        :-moz-placeholder { /* Mozilla Firefox 4 to 18 */
            color: #999; opacity:1;
        }

        ::-moz-placeholder { /* Mozilla Firefox 19+ */
            color: #999;opacity:1;
        }

        input:-ms-input-placeholder{
            color: #999;opacity:1;
        }

        input::-webkit-input-placeholder{
            color: #999;opacity:1;
        }
        .d-label{
            width:4em;
            line-height: 2;
        }
        .d-result{
            -webkit-box-flex: 1;
            -webkit-flex: 1;
            margin-left:8px;
        }
        .select-store{
            line-height: 2;
            text-align: center;
            background: #EEE;
            color: #999;
        }
        .start-end-time ul{
            list-style:none;
            line-height: 2;
        }
        .start-end-time ul li{
            width:50%;
            text-align: center;
            position: relative;
        }
        .start-end-time ul li:first-child{
            float:left;
        }
        .start-end-time ul li:first-child span input{
            left:0;
        }
        .start-end-time ul li input{
            width: 90% !important;
            font-size: .9em;
            line-height: 2.2;
            text-align: center;
            background: #EEE;
            position: absolute;
            top: 0;
            border:0;
        }
        .start-end-time ul li:last-child{
            float:right;
        }
        .start-end-time ul li:last-child span input{
            right:0;
        }

        .inside{
            margin:0 20px;
        }
        .inside span{
            padding:15px 0;
            display: block;
            color:#666;
            font-size:.8em;
        }
        .f1 .inside span:first-child{
            float: left;
        }
        .f1 .inside span:last-child{
            float: right;
        }
        .f1 .wraper:last-child .inside span:last-child{
            font-size: .7em;
            color:#111;
        }
        .f1 .wraper:last-child .inside span:last-child p{
            line-height:2;
        }
        .f2,.f3{
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
            border:1px solid #EEE;
        }
        .fm{
            margin-left:32px;
        }
        .f3{
            margin:10px 25px;
            font-size:.9em;
        }
        .f3,.f4 {
            background-color: #fc324a;
            text-align: center;
            color:#FFF;
        }
        .addRule,.confirm{
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
            top:9px;
            left:-67px;
        }

        .addRule em:after{
            height:15px;
            width:2px;
            top:3px;
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
        .wwt-select-store-wrapper{
            display: none;
        }
        .wwt-shape{
            position: fixed;
            top: 0px;
            left: 0px;
            width: 100%;
            height: 100%;
            background: rgb(0, 0, 0);
            z-index: 888;
            opacity: 0.2;
        }
        
        .wwt-select-store-true{
            position: absolute;
            left: 10%;
            top: -250px;
            width: 80%;
            background: #FFF;
            border-radius: 5px;
            opacity: 1;
            z-index: 10000;
            box-shadow: 0 0 5px rgba(0,0,0,.3);
            /*transform:translate(0,0);*/
            /*-webkit-transform:translate(0,0);*/
            -webkit-transition: top 0.5s ease-in-out;
            transition: top 0.5s ease-in-out;
        }
        .wwt-select-store-header{
            text-align: center;
            padding: .5em;
            border-bottom: 1px solid #EEE;
            font-size: .8em;
            font-weight: bold;
            color: #666;
        }

        .wwt-storelist{
            max-height: 7.5em;
            overflow-y: scroll;
            overflow-x: hidden;
        }
        .wwt-storelist::-webkit-scrollbar{
            width:1px;
            height:5px;
            background-color:#fc324a;
        }
        .wwt-storelist::-webkit-scrollbar-track{
            -webkit-box-shadow: inset 0 0 6px rgba(252,50,74,.3);
            border-radius: 10px;
            background-color: #fc324a;
        }
        .wwt-storelist::-webkit-scrollbar-thumb{
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 6px rgba(252,50,74,.3);
            background-color: #fc324a;
        }
        .wwt-store-btn div{
            text-align: center;
            font-size: .8em;
            border-top:1px solid #EEE;
            padding: .65em;
        }
        .wwt-store-btn span{
            width: 50%;
            padding: 2px 10px 1px;
            border-radius: 5px;
            margin: 1em;
        }
        .wwt-store-reset{
            background: #EEE;
            color: #999;
        }
        .wwt-store-confirm{
            background: #fc324a;
            color: #FFF;
        }
        .wwt-storelist{
            font-size:.8em;
            padding:0 .5em;
            color: #666;
        }
        .wwt-storelist p {
            padding:.35em 0;
            border-bottom:1px solid #EEE;
        }

        .wwt-defaultStore{
            background: url(../resource/img/app/default.png) no-repeat right;
            background-size: 8%;
        }
        .wwt-selectStore{
            background: url(../resource/img/app/select.png) no-repeat right;
            background-size: 8%;
        }
    </style>
<script>
    $(function(){
       $(".wwt-storelist").scroll(function(){
           if($(this).scrollTop() > 0){
               $(".wwt-select-store-header").css("-webkit-box-shadow","0 0 10px #666");
           }else{
               $(".wwt-select-store-header").css("-webkit-box-shadow","none");
           }
       })
    })
</script>

</head>
<body>
<input type="hidden" id="auth" value="${auth}">
    <div class="container">
          <section class="f1">
              <div class="d-group">
                  <div class="row">
                      <div class="d-label">活动名称</div>
                      <div class="d-result">
                          <input type="text" placeholder="请输入活动名称" name="aname" class="aname" id="aname">
                      </div>
                  </div>
                  <div class="row">
                      <div class="d-label">适用门店</div>
                      <div class="d-result select-store">
                            <span>请选择门店</span>
                      </div>
                  </div>
                  <div class="row">
                      <div class="d-label">活动时间</div>
                      <div class="d-result start-end-time">
                          <ul class="clearfix">
                              <li>
                                  <span>
                                      <input id="startTime" type="text" placeholder="开始时间" name="startTime" readonly>
                                  </span>
                              </li>
                              <li>
                                  <span>
                                      <input id="endTime" type="text" placeholder="结束时间" name="endTime" readonly>
                                  </span>
                              </li>
                          </ul>
                      </div>
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
                            <input type="checkbox" class="radio no" readonly>
                            <span class="fm">满</span>
                            <input type="text" value="XXX" readonly disabled>
                            <span>元，立减</span>
                            <input type="text" value="XX" readonly disabled>
                            <span>元</span>
                        </p>
                        <p class="sj">
                              <input type="checkbox" class="radio no" readonly>
                              <span class="fm">满</span>
                              <input type="text" value="XXX" readonly disabled>
                              <span>元，随机减</span>
                              <input type="text" value="XX" readonly disabled>
                              <span>至</span>
                              <input type="text" value="XX" readonly disabled>
                              <span>元</span>
                        </p>
                        <p class="zk">
                              <input type="checkbox" class="radio no" readonly>
                              <span class="fm">满</span>
                              <input type="text" value="XXX" readonly disabled>
                              <span>元，</span>
                              <input type="text" value="XX" readonly disabled>
                              <span>折</span>
                        </p>
                  </div>
          </section>

          <section class="f3">
                <div class="addRule" id="add">
                    <em></em>
                    <span>添加新规则</span>
                </div>
          </section>
          <section class="f2">
              <div class="wraper" id="newRules">
                          <!-- 新加规则
                  <p>
                      <input type="checkbox" class="delete">
                      <span class="fm">满</span>
                      <input type="text">
                      <span>元，</span>
                      <input type="text">
                      <span>折</span>
                  </p>
                  -->
              </div>
          </section>


    </div>
    <div class="wwt-select-store-wrapper">
        <div class="wwt-shape"></div>
        <div class="wwt-select-store-true">
            <div class="wwt-select-store-header">
                <span>选择门店</span>
            </div>
            <div class="wwt-storelist">
                <p class="wwt-defaultStore" data-id="-1" id="_all">
                    <span>全部门店</span>
                </p>
                 <c:forEach items="${shopList}" var="store">
                      <p class="wwt-defaultStore" data-id="${store.storeNo}">
		                    <span>${store.name}</span>
		                </p>
                  </c:forEach>
            </div>
            <div class="wwt-store-btn">
                <div>
                    <span class="wwt-store-reset">重置</span>
                    <span class="wwt-store-confirm">确定</span>
                </div>
            </div>
        </div>
    </div>
    <footer>
        <section class="f4" id="confirm">
            <div class="confirm">确认</div>
        </section>
    </footer>
</body>
</html>
