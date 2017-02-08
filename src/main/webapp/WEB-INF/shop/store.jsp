<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>店铺</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/store.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">

    </style>
</head>
<body>
    <div class="container">
        <section class="f1">
             <div class="store-header">
                 <p>
                     <img id="_storeImg"  onerror="this.src='../resource/img/app/header.png'" class="header">
                 </p>
                 <p id="_storeName">

                 </p>
                 <p id="_userAccount">

                 </p>
             </div>
        </section>
        <section class="f2">
              <div class="list-group">
                  <p class="clearfix common-mg yu-e ">
                      <span class="span-label">余额</span>
                      <span class="span-result common-jt">
                           <span>￥0.00</span>
                      </span>
                  </p>
                  <p class="clearfix common-mg qy" id="signInfo">
                      <span class="span-label">签约</span>
                      <span class="span-result common-jt">
                          <span>&nbsp;</span>
                      </span>
                  </p>
              </div>
        </section>
        <section class="f2">
            <div class="list-group">
                <p class="clearfix common-mg set" id="setting">
                    <span class="span-label">设置</span>
                    <span class="span-result common-jt">
                        <span>&nbsp;</span>
                    </span>
                </p>
                <p class="clearfix common-mg about" id="about">
                    <span class="span-label">关于</span>
                    <span class="span-result common-jt">
                        <span>&nbsp;</span>
                    </span>
                </p>
            </div>
        </section>
        <section class="f2">
            <div class="list-group">
                <p class="clearfix common-mg kfMan" id="mySale">
                    <span class="span-label">我的客服经理</span>
                    <span class="span-result common-jt">
                        <span>&nbsp;</span>
                    </span>
                </p>
                <p class="clearfix common-mg kfHot">
                    <span class="span-label">客服热线</span>
                    <span class="span-phone">400-826-7710</span>
                </p>
            </div>
        </section>
    </div>
    <footer>
        <div class="footer">
            <ul class="clearfix">
                <li id="_index">
                    <p>
                        <img src="../resource/img/app/jingying-zhihui.png">
                    </p>
                    <p>
                        <span>经营</span>
                    </p>
                </li>
                <li id="_myMember">
                    <p>
                        <img src="../resource/img/app/huiyuan-zhihui.png">
                    </p>
                    <p>
                        <span>会员</span>
                    </p>
                </li>
                <li id="_storeLink">
                    <p>
                        <img src="../resource/img/app/dianpu.png">
                    </p>
                    <p>
                        <span>店铺</span>
                    </p>
                </li>
            </ul>
        </div>
    </footer>
</body>
</html>
<script>
    var auth = "${auth}";
    $(function(){
        load();
    });
    function load() {
        //签约
        $("#signInfo").on("touchend", function () {
            window.open("./signInfo?auth="+auth);
        });
        //设置
        $("#setting").on("touchend", function () {
            window.open("./setting?auth="+auth);
        });
        //关于
        $("#about").on("touchend", function () {
            jalert("关于");
        });
        //我的客服经理
        $("#mySale").on("touchend", function () {
            jalert("我的客服经理");
        });
        //店铺
        $("#_storeLink").on("touchend",function(){
            location.href = "./toStore?auth="+auth;
        });

        //我的会员
        $("#_myMember").on("touchend",function(){
            jalert("我的会员");
        });
        //经营
        $("#_index").on("touchend",function(){
            location.href = "./toIndex?auth="+auth;
        });
        initStore();
    }

    function initStore(){
        $.post("./getStoreInfo",{"auth":auth},function(obj){
            if(obj.code == 0){
                $("#_storeImg").attr("img",obj.data.store.shopLogo);
                $("#_storeName").text(obj.data.store.name);
                $("#_userAccount").text(obj.data.loginUser.account);
            }
        })
    }
</script>