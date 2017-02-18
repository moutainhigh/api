<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>查看数据</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <link rel="stylesheet" href="../resource/mobiscroll/css/mobiscroll.custom-3.0.0-beta2.min.css">
    <script src="../resource/mobiscroll/js/mobiscroll.custom-3.0.0-beta2.min.js"></script>
    <script src="../resource/js/Dutil.js"></script>
    <style>
        .f1{
            background: #fc324a;
            color:#FFF;
            font-size:1rem;
        }
        .common-mg{
            margin:0 1rem;
        }
        .common-pd{
            padding:1em 0;
        }
        .f1-sum ul{
            list-style: none;
        }
        .f1-sum ul li{
            float: left;
            width:33.3%;
            text-align: center;
        }
        .f1-sum ul li:not(:last-child){
            border-right: 1px solid #FFF;
            border-color: rgba(255,255,255,.4);
        }
        .f1-sum ul li p{
            padding:.7rem 0 0;
        }
        .f1-sum ul li p:last-child{
            padding-bottom:.5rem;
        }
        .f2,.f3,.f4{
            background: #FFF;
            font-size:1rem;
        }
        .f2{
            border-bottom:1px solid #EEE;
        }
        .f2-tj{
            position: relative;
        }
        .f2-tj .atime{
            float: left;
        }
        .f2-tj .date{
            position: absolute;
            top:27%;
            left:24%;
        }
        .f2-tj .date input{
            border:1px solid #EEE;
            border-radius: 5px;
            font-size: 1rem;
            line-height: 1.4;
            text-align: center;
            color:#666;
            -webkit-appearance: none;
            box-shadow: none;
        }
        .f2-tj .serach{
            float: right;
        }
        .f2-tj .serach span{
            padding:2px 8px;
            border:1px solid #fc324a;
            border-radius: 5px;
            border-color: rgba(252,50,74,.6);
        }
        .f3 {
            border-bottom:1px solid #EEE;
            margin-top:4px;
        }
        .show-date-sum{
            padding:1.2rem 0;
            position: relative;
        }
        .f3 .time-count em{
            font-style:normal;
            color:#fc324a;
        }
        .f3 .money{
            position: absolute;
            right:0;
            top:13%;
        }
        .f3 .money p{
            padding-bottom: .35rem;
        }
        .f3 .money p:last-child{
            font-size:.9rem;
            text-align: right;
            color:#fc324a;
        }

        .f4 ul{
             list-style: none;
        }
        .f4 ul li{
            position: relative;
        }
        .f4 .common-mg{
            border-bottom:1px solid #EEE;
        }
        .f4 li:last-child .common-mg{
            border:none;
        }
        .info{
            position: absolute;
            top:11%;
            right:20px;
        }
        .info p{
            text-align: right;
            color:#333;
        }
        .info p:last-child{
            font-size:.9rem;
            color:#fc324a;
        }
        .info em:before{
            content: "￥";
        }
        .item{
            display: block;
            padding: .5em 0;
            /*border-bottom: 1px solid #d6d6d6;*/
        }
        .item img{
            float:left;
            width:50px;
            height:50px;
        }
        .cont{
            margin-left: 4em;
        }
        .item p{
            font-size: 1em;
            padding-bottom: 8px;
            max-height: 45px;
            overflow: hidden;
            line-height: 1.2;
        }
        .normalpay{
            color: #333;
        }
        .refund{
            color:#999;
        }
        .refund p:last-child{
            color:#999 !important;
        }
        .item p:last-child{
            color:#333;
            font-size:14px;
        }
        .info em{
            display: block;
            font-size: 1em;
            line-height: 24px;
            white-space: nowrap;
            font-style: normal;
        }
        .info .small-lj{
            display: inline;
            font-size: .9em;
        }
        .refund em{
            color:#999 !important;
        }

        .cont .desc span{
            margin-right:5px;
        }
        .cont .desc span:nth-child(2){
            padding:1px 2px;
            color:#fc324a;
            border-radius: 5%;
            border:1px solid #fc324a;
            font-size:12px;
            opacity: .8;
        }
        .cont .desc span del{
            color:#fc324a;
            font-size:14px;
            opacity: .8;
        }

    </style>

</head>
<body>
   <div class="container">
       <section class="f1">
           <div class="f1-sum common-pd">
                 <ul class="clearfix">
                     <li>
                         <p>
                             <span>${countDiscount.countUser}&nbsp;&nbsp;(人)</span>
                         </p>
                         <p>
                             <span>活动总人数</span>
                         </p>
                     </li>
                     <li>
                         <p>
                             <span>${countDiscount.planChargeAmount}&nbsp;(元)</span>
                         </p>
                         <p>
                             <span>活动总流水</span>
                         </p>
                     </li>
                     <li>
                         <p>
                             <span id="totalDiscount"></span>
                         </p>
                         <p>
                             <span>减免总额度</span>
                         </p>
                     </li>
                 </ul>
           </div>
       </section>
       <section class="f2">
           <div class="f2-tj common-mg common-pd clearfix">
                <div class="atime">
                    <span>活动时间</span>
                </div>
                <div class="date">
                    <span>
                        <input type="text" id="date" value="" readonly>
                    </span>
                </div>
                <div class="serach" id="serach">
                    <span >
                        检索
                    </span>
                </div>
           </div>
       </section>
       <section class="f3">
             <div class="show-date-sum common-mg">
                 <div class="time-count">
                     <%--<span>2017-02-15</span>--%>
                     <span id="total">共<em>0</em>笔</span>
                 </div>
                 <div class="money">
                     <p>
                         <span id="actualCharge">实收:0.0</span>
                     </p>
                     <p>
                         <span id="discountPrice">减免:0.0</span>
                     </p>
                 </div>
             </div>
       </section>
       <section class="f4">
           <ul id="_discountOrder">


           </ul>
       </section>
   </div>
</body>
</html>
<script type="text/javascript">
    var startTime = ${discount.startTime}*1000;
    var endTime = ${discount.endTime}*1000;
    var time = "";
    var auth = "${auth}";
    var lokered = false;
    var pageNo = 1;
    var totalRecond = 0;
    var discountId = ${discount.id};
       $(function(){
           //获取活动的开始时间和结束时间
           /*$.post("",function(data){

           })*/
           $("#totalDiscount").html(parseFloat(${countDiscount.planChargeAmount-countDiscount.actualChargeAmount}).toFixed(2)+"&nbsp;&nbsp;(元)");
           if(startTime <= new Date().getTime() && new Date().getTime() <= endTime){
               $('#date').val(new Date().Format("yyyy.MM.dd"));//默认时间(如果当前时间在活动期间 ，显示当前时间。如果不在当前活动期间。默认显示活动开始时间)
           }else{
               $('#date').val(new Date(startTime).Format("yyyy.MM.dd"));//默认时间(如果当前时间在活动期间 ，显示当前时间。如果不在当前活动期间。默认显示活动开始时间)
           }
           //默认显示的查询时间

           var option = {
               theme: 'ios',
               lang: 'zh',
               display: 'bottom',
               dateFormat:'yyyy.mm.dd',
               min:new Date(startTime),//活动开始时间（ 这个插件显示的2017/03/16 多一个月。）
               max: new Date(endTime)//活动结束时间（ 这个也同理  ）
           };
           //
           $('#date').mobiscroll().date(option);

           //查询某一天的数据
           $("#serach").on("click",function(){
               time = $('#date').val();
               $("#_discountOrder").html("");
                search(pageNo);
           })
           time = $('#date').val();
           search(pageNo);
       })

      function search(pageNo){
          if(time == ""){
              jalert.show("请选择时间");
              return;
          }
          $.post("./orderPage",{"discountId":discountId,"time":time,"auth":auth,"pageNo":pageNo,"pageSize":10},function(obj){
                if(obj.code == 0){
                    if(obj.data){
                        totalRecond = obj.data.total;
                        $("#total").html("共<em>"+obj.data.total+"</em>笔");
                        $("#actualCharge").html("实收:"+obj.data.totalPrice);
                        $("#discountPrice").html("减免:"+parseFloat(obj.data.discountPrice).toFixed(2));
                    }

                    if(obj.data && obj.data.list){
                        html = "";
                        for(var i=0;i<obj.data.list.length;i++){
                            var bean = obj.data.list[i];
                            html += "<li>";
                            html += "<div class=\"common-mg\">";
                            html += "<span class=\"info\">";
                            html += "<p><em>"+bean.actualChargeAmount+"</em></p>";
                            html += "<p><del><em>"+parseFloat(bean.planChargeAmount-bean.actualChargeAmount).toFixed(2)+"</em></del></p>";
                            html += "</span>";
                            html += "<span class=\"item normalpay\">";
                            if(bean.payMethod == 2){
                                html += "<img src=\"..\/resource\/img\/app\/zhifubao.png\">";
                            }else if(bean.payMethod == 1){
                                html += "<img src=\"..\/resource\/img\/app\/weixinzhifu.png\">";
                            }
                            html += "<div class=\"cont\">";
                            html += "<p class=\"desc\">";
                            html += "<span>收款码收款</span>";
                            html += "<span>优惠</span>";
                            html += "</p>";
                            html += "<p class=\"time\">"+new Date(bean.ctime*1000).Format("hh:mm:ss")+"</p>";
                            html += "</div>";
                            html += "</span>";
                            html += "</div>";
                            html += "</li>";
                        }
                        $("#_discountOrder").append(html);
                        lokered = false;
                    }
                }else{
                    jalert.show(obj.meg);
                }
          })
      }

    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "y+": this.getYear(),
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    window.onscroll = pageQuery;
    function pageQuery(){
        if(pageNo <= 1000){
            if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
                if(lokered){
                    return;
                }
                lokered = true;
                search(++pageNo);
            }
        }
    }
</script>