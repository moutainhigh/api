<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link rel="stylesheet" href="css/base.css"/>
    <link rel="stylesheet" href="css/index.css"/>
    <link rel="stylesheet" href="css/swiper.min.css"/>
    <link rel="stylesheet" href="css/assistant.css"/>
    <link rel="stylesheet" href="css/mine.css"/>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*50/375+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>
</head>
<body style="padding-bottom: 3.6rem;">
<div class="item">
    <!--小店start-->
   <!-- <div class="tab-shop">
        <div class="top">
            <p class="top-tit">首页</p>
        </div>
        <div class="middle">
            <div class="receivables clearfix">
                <div class="today-money fl">
                    <h3>今日应收金额:</h3>
                    <p>0.00元</p>
                </div>
                <div class="today-money fr">
                    <h3>今日实收金额:</h3>
                    <p>0.00元</p>
                </div>
            </div>
            <div class="trade clearfix">
                <div class="trade-number trade-border fl">
                    <h2>交易笔数</h2>
                    <p>0</p>
                </div>
                <div class="trade-number trade-border  fl">
                    <h2>交易笔数</h2>
                    <p>0</p>
                </div>
                <div class="trade-number  fl">
                    <h2>交易笔数</h2>
                    <p>0</p>
                </div>
            </div>
            <div class="shop-manager">
                <h1><span class="shop-tit"></span><i class="shop-title">店铺管理</i></h1>
                <div class="shop-icon clearfix">
                    <div class="shop-con shop-border fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                    <div class="shop-con fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                </div>
                <div class="shop-icon clearfix shop-border-no">
                    <div class="shop-con shop-border fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                    <div class="shop-con fl">
                        <p><img src="img/shop-manager.png"/> </p>
                        <h2>管理门店</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>-->
    <!--小店end-->
    <!--助手start-->
  <div class="tab-assitant">
        <div class="top">
            <p class="top-tit">助手</p>
        </div>
        <div class="middle">
            <!--轮播图start-->
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide"><img src="img/banner/banner-one.png"/> </div>
                    <div class="swiper-slide"><img src="img/banner/banner-two.png"/></div>
                    <div class="swiper-slide"><img src="img/banner/banner-three.png"/></div>

                </div>
                 <!--Add Pagination -->
                <div class="swiper-pagination"></div>


            </div>
            <!-- 轮播图结束-->
            <div class="shop-manager assistant-tit">
                <h1><span class="shop-tit"></span><i class="shop-title">会员营销</i></h1>
                <div class="shop-icon clearfix">
                    <div class="shop-con shop-border fl">
                        <p><img src="img/money-bag.png"/> </p>
                        <h2>立减买单</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="img/vip-point.png"/> </p>
                        <h2 class="">会员集点</h2>
                    </div>
                    <div class="shop-con fl">
                        <p><img src="img/red-bag.png"/> </p>
                        <h2>助力红包</h2>
                    </div>
                </div>
            </div>
            <!--会员营销结束-->
            <div class="shop-manager assistant-tit">
                <h1><span class="shop-tit"></span><i class="shop-title">经营分析</i></h1>
                <div class="shop-icon clearfix">
                    <div class="shop-con shop-border fl">
                        <p><img src="img/data-scale.png"/> </p>
                        <h2>数据统计</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="img/vip-manager.png"/> </p>
                        <h2>会员管理</h2>
                    </div>

                </div>
            </div>
            <!--经营分析结束-->
        </div>
    </div>
    <!--助手end-->
    <!--我的start-->
    <!-- <div class="tab-mine">
         <div class="top">
             <p class="top-tit">店铺</p>
         </div>
         <div class="mine-title">
             <p><img src="img/mine-tit.png"/> </p>
             <h3>智慧上街测试门店</h3>
             <h4>12345678</h4>
         </div>
         &lt;!&ndash;以背景条位分割线，可重复的内容start&ndash;&gt;
         <ul class="mine-list">
             <li class="clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-list-tit fl">营销账户</span><strong class="fr">￥0.0<a>></a></strong>
             </li>
             <li class="mine-list-no clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-list-tit fl">签约</span><a class="mine-sign fr">></a>
             </li>

         </ul>
         <p class="mine-bg"></p>
         &lt;!&ndash;以背景条位分割线，可重复的内容end&ndash;&gt;
         &lt;!&ndash;以背景条位分割线，可重复的内容start&ndash;&gt;
         <ul class="mine-list">
             <li class="clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-list-tit fl">设置</span><a class="mine-sign fr">></a>
             </li>
             <li class="mine-list-no clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-list-tit fl">关于</span><a class="mine-sign fr">></a>
             </li>

         </ul>
         <p class="mine-bg"></p>
         &lt;!&ndash;以背景条位分割线，可重复的内容end&ndash;&gt;
         &lt;!&ndash;以背景条位分割线，可重复的内容start&ndash;&gt;
         <ul class="mine-list">
             <li class="clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-manager fl">客服经理</span><a class=" fr">></a>
             </li>
             <li class="mine-list-no clearfix">
                 <span class="fl"><img src="img/account.png"/> </span><span class="mine-list-tit fl">客服热线</span><a class=" fr">></a>
             </li>

         </ul>
         &lt;!&ndash;以背景条位分割线，可重复的内容end&ndash;&gt;
     </div>-->
    <!--我的end-->

</div>
</body>
</html>
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/swiper.min.js"></script>
<script>
    var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
        slidesPerView: 1,
        paginationClickable: true,
        spaceBetween:0,
        loop: true,
        autoplay:1,
        speed:2000

    });
</script>