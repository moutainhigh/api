<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/index.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>

</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
   <div class="container">
          <section class="f1 clearfix">
                <div class="flow">
                    <p>
                        <img src="../resource/img/app/chakanliushui.png">
                    </p>
                    <p>
                        <span>查看流水</span>
                    </p>
                </div>
                <div class="withdraw">
                    <p>
                       <img src="../resource/img/app/tixian.png">
                    </p>
                    <p>
                        <span>提现</span>
                    </p>
                </div>
          </section>
          <section class="f2">
               <div class="common-mg">
                 <div class="member">
                    会员营销
                 </div>
                 <div class="marketing">
                     <ul class="clearfix">
                         <li id="discount1">
                             <p>
                                <img src="../resource/img/app/lijianmaidan.png">
                             </p>
                             <p>
                                <span>立减买单</span>
                             </p>
                         </li>
                         <li id="discount2">
                             <p>
                                 <img src="../resource/img/app/huiyuanjidian.png">
                             </p>
                             <p>
                             <span>会员集点</span>
                             </p>
                         </li>
                         <li id="discount3">
                             <p>
                                 <img src="../resource/img/app/zhulihongabao.png">
                             </p>
                             <p>
                                 <span>助力红包</span>
                             </p>
                         </li>
                     </ul>
                 </div>
               </div>
          </section>
          <section class="f3">
              <div class="common-mg">
                 <div class="today">
                     今日经营数据
                 </div>
                 <div class="data">
                     <ul class="clearfix">
                         <li>
                             <p id="_sum">0.00</p>
                             <p>今日交易 (元)</p>
                         </li>
                         <li>
                             <p id="_count">0</p>
                             <p>交易笔数 (笔)</p>
                         </li>
                         <li>
                             <p id="_countPersion">0</p>
                             <p>光临客人 (人)</p>
                         </li>
                     </ul>
                 </div>
              </div>
          </section>
          <section class="f4">
              <div class="common-mg">
                 <div class="store">
                     店铺管理
                 </div>
                 <div class="manager">
                     <ul class="clearfix">
                         <li id="storeManager">
                             <p>
                                 <img src="../resource/img/app/guanlimendian.png">
                             </p>
                             <p>
                                 <span>管理门店</span>
                             </p>
                         </li>
                         <li id="memberManager">
                             <p>
                                 <img src="../resource/img/app/guanlidianyuan.png">
                             </p>
                             <p>
                                 <span>管理店员</span>
                             </p>
                         </li>
                     </ul>
                 </div>
              </div>
          </section>
   </div>
   <footer>
        <div class="footer">
            <ul class="clearfix">
                <li id="_index">
                    <p>
                        <img src="../resource/img/app/jingying.png">
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
                        <img src="../resource/img/app/dianpu-zhihui.png">
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
    var auth = $("#auth").val();
    $(function(){
        load();
    });
    function load(){
        //流水
        $(".flow").on("touchend",function(){
            window.open("./toTransactionDetails?auth="+auth+"&storeNo=-1");
        });
        //提现
        $(".withdraw").on("click",function(){
//             window.open("https://www.mszxyh.com/wapserver/outer/index.html?Page=relogin&ChannelId=mszx02279");
             $.get("./toBank",{auth:auth},function(result){
            	 if(result.code == 0){
            	     window.location.href=result.msg;
            	 }else{
            	     jalert.show(result.msg);
            	 }
             });
        });

        //店铺
        $("#_storeLink").on("touchend",function(){
            location.href = "./toStore?auth="+auth;
        });

        //我的会员
        $("#_myMember").on("touchend",function(){
//             jalert.show("我的会员");
               location.href="./toMemberData?auth="+auth;
        });
        //经营
        $("#_index").on("touchend",function(){
            location.href = "./toIndex?auth="+auth;
        });
        //优惠立减
        $("#discount1").on("touchend",function(){
           location.href = "../discount/discountActivity?auth="+auth;
        });
        //会员集点
        $("#discount2").on("touchend",function(){
            jalert.show("暂末开通");
        });
        //助力红包
        $("#discount3").on("touchend",function(){
            jalert.show("暂末开通");
        });
        //管理门店
        $("#storeManager").on("touchend",function(){
            jalert.show("暂末开通");
        });
        //管理店员
        $("#memberManager").on("touchend",function(){
//             jalert.show("暂末开通");
             location.href="./toManageStaffPage?auth="+auth;
        });

        $.post("./countDeal",{"auth":auth},function(obj){
            if(obj.code == 0){
                $("#_countPersion").text(obj.data.countPersion);
                $("#_sum").text(obj.data.sum);
                $("#_count").text(obj.data.count);
            }
        });

    }
</script>