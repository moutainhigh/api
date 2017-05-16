<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
  <div class="tab-assitant">
        <div class="top">
            <p class="top-tit">助手</p>
        </div>
        <div class="middle">
            <!--轮播图start-->
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-one.png"/> </div>
                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-two.png"/></div>
                    <div class="swiper-slide"><img src="../resource/img/merchant/banner-three.png"/></div>

                </div>
                 <!--Add Pagination -->
                <div class="swiper-pagination"></div>


            </div>
            <!-- 轮播图结束-->
            <div class="shop-manager assistant-tit">
                <h1><span class="shop-tit"></span><i class="shop-title">会员营销</i></h1>
                <div class="shop-icon clearfix">
                    <div class="shop-con shop-border fl">
                        <p><img src="../resource/img/merchant/money-bag.png"/> </p>
                        <h2>立减买单</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="../resource/img/merchant/vip-point.png"/> </p>
                        <h2 class="">会员集点</h2>
                    </div>
                    <div class="shop-con fl">
                        <p><img src="../resource/img/merchant/red-bag.png"/> </p>
                        <h2>助力红包</h2>
                    </div>
                </div>
            </div>
            <!--会员营销结束-->
            <div class="shop-manager assistant-tit">
                <h1><span class="shop-tit"></span><i class="shop-title">经营分析</i></h1>
                <div class="shop-icon clearfix">
                    <div class="shop-con shop-border fl">
                        <p><img src="../resource/img/merchant/data-scale.png"/> </p>
                        <h2>数据统计</h2>
                    </div>
                    <div class="shop-con shop-border fl">
                        <p><img src="../resource/img/merchant/vip-manager.png"/> </p>
                        <h2>会员管理</h2>
                    </div>

                </div>
            </div>
            <!--经营分析结束-->
        </div>
    </div>
    <script type="text/javascript">
<!--
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
//-->
</script>
