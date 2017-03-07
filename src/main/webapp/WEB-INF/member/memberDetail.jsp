<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>会员数据详情</title>
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

        .member-list{
            padding-top:1px;
            font-size:.7rem;
        }
        .member-li{
            line-height:2.4;
            margin-bottom: .25rem;
            background-color:#fff;
        }
        .member-li .head-img{
            width:20%;
            float:left;
            position: absolute;
        }
        .head-img img{
            width:2.75rem;
            height:2.75rem;
            border-radius:50%;
            top: 7px;
            position: absolute;
        }
        .member-li .member-detail{
            width:80%;
            float:right;
            font-weight: bold;
            opacity: .5;
        }
        .member-detail div .nick{
            float:left;
        }
        .member-detail div .count,
        .member-detail div .spendDetail{
            display: inline-block;
        }
        .member-detail div .spendDetail{
            margin-left:.75rem;
        }
        .member-detail div .time{
            float:right;
            color:#666;
            font-size: .9em;
        }
        .member-detail div:after,.member-li:after{
            content: " ";
            clear:both;
            display: block;
            visibility: hidden;
            height:0;
            line-height: 0;
        }
        .common-mg{
            margin:0 1rem;
        }
        .header-image-pd{
            position: relative;
        }
        i{
            font-style: normal;
        }
        .span{
            margin-right: 1rem;
        }
    </style>
</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
<input value="${type}" id="type" name="type" type="hidden">
<input value="${number}" id="number" name="number" type="hidden">
<div class="container">
    <section>
        <div class="member-list">
<!--             <div class="member-li"> -->
<!--                 <div class="common-mg"> -->
<!--                     <div class="head-img"> -->
<!--                         <div class="header-image-pd"> -->
<!--                             <img src="../resource/img/cat.png" alt=""> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="member-detail"> -->
<!--                         <div> -->
<!--                             <div class="nick">小花猫</div> -->
<!--                             <div class="time">1天前</div> -->
<!--                         </div> -->
<!--                         <div> -->
<!--                             <div class="count">到店5次</div> -->
<!--                             <div class="spendDetail"> -->
<!--                                 <span class="span">共消费<i>100</i>元</span> -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                 </div> -->
<!--             </div> -->
        </div>
    </section>
</div>
</body>
</html>

<script>
 $(function(){
	 $.post("./toMemberDetail",{
		 auth:$("#auth").val(),
		 type:$("#type").val(),
		 number:$("#number").val()
	 },function(result){
		 console.log(result);
		 if(result.code == 0){
			 var obj = result.data,len = obj.length;
			 if(len == 0){
				 jalert.show('暂时无数据!');
			 }else{
				 for(var i =0;i<len;i++){
					 if(obj[i].nick == null){
						 obj[i].nick = '支付宝用户';
					 }
					 var s = '<div class="member-li">'
			                +'<div class="common-mg">'
			                +'<div class="head-img">'
			                +' <div class="header-image-pd">'
			                +'       <img src="'+obj[i].headImg+'" alt="找不见了" onerror="javascript:this.src=\'../resource/img/cat.png\'">'
			                +'    </div>'
			                +' </div>'
			                +'  <div class="member-detail">'
			                +'     <div>'
			                +'       <div class="nick">'+obj[i].nick+'</div>'
			                +'       <div class="time">'+timeshow(obj[i].time)+'</div>'
			                +'    </div>'
			                +'   <div>'
			                +'       <div class="count">到店'+obj[i].count+'次</div>'
			                +'       <div class="spendDetail">'
			                +'           <span class="span">共消费<i>'+obj[i].sum+'</i>元</span>'
			                +'       </div>'
			                +'    </div>'
			                +'</div>'
			                +'</div>'
			                +'</div>';
			                
			                $(".member-list").append(s);
				 }
			 }
		 }else{
			 jalert.show('出错了额');
		 }
	 });
	 
 });

 
 function timeshow(time){
	 var _ct = new Date().getTime();
	 var _timeDif = _ct/1000 - time ;
// 	 console.log(_timeDif);
	 //lm = 60s lh = 3600s ld = 86400
	 var hour = 3600,day = 86400;
	 if(_timeDif < day ){
		 if(_timeDif<hour){
			 return Math.floor(_timeDif/60)+"分钟以前";
		 }else if(_timeDif < (2*hour)){
			 return "1小时以前";
		 }else if(_timeDif < (3*hour)){
			 return "2小时以前";
		 }else if(_timeDif < (4*hour)){
			 return "3小时以前";
		 }else if(_timeDif < (5*hour)){
			 return "4小时以前";
		 }else if(_timeDif < (6*hour)){
			 return "5小时以前";
		 }else if(_timeDif < (7*hour)){
			 return "6小时以前";
		 }else if(_timeDif < (8*hour)){
			 return "7小时以前";
		 }else if(_timeDif < (9*hour)){
			 return "8小时以前";
		 }else if(_timeDif < (10*hour)){
			 return "9小时以前";
		 }else if(_timeDif < (11*hour)){
			 return "10小时以前";
		 }else if(_timeDif < (12*hour)){
			 return "11小时以前";
		 }else if(_timeDif < (13*hour)){
			 return "12小时以前";
		 }else if(_timeDif < (14*hour)){
			 return "13小时以前";
		 }else if(_timeDif < (15*hour)){
			 return "14小时以前";
		 }else if(_timeDif < (16*hour)){
			 return "15小时以前";
		 }else if(_timeDif < (17*hour)){
			 return "16小时以前";
		 }else if(_timeDif < (18*hour)){
			 return "17小时以前";
		 }else if(_timeDif < (19*hour)){
			 return "18小时以前";
		 }else if(_timeDif < (20*hour)){
			 return "19小时以前";
		 }else if(_timeDif < (21*hour)){
			 return "20小时以前";
		 }else if(_timeDif < (22*hour)){
			 return "21小时以前";
		 }else if(_timeDif < (23*hour)){
			 return "22小时以前";
		 }else if(_timeDif < (24*hour)){
			 return "23小时以前";
		 }
	 }else if(_timeDif < (2*day)){
		 return "1天以前";
	 }else if(_timeDif < (3*day)){
		 return "2天以前";
	 }else if(_timeDif < (4*day)){
		 return "3天以前";
	 }else if(_timeDif < (5*day)){
		 return "4天以前";
	 }else if(_timeDif < (6*day)){
		 return "5天以前";
	 }else if(_timeDif < (7*day)){
		 return "6天以前";
	 }else if(_timeDif < (8*day)){
		 return "7天以前";
	 }else if(_timeDif < (9*day)){
		 return "8天以前";
	 }else if(_timeDif < (10*day)){
		 return "9天以前";
	 }else if(_timeDif < (11*day)){
		 return "10天以前";
	 }else if(_timeDif < (12*day)){
		 return "11天以前";
	 }else if(_timeDif < (13*day)){
		 return "12天以前";
	 }else if(_timeDif < (14*day)){
		 return "13天以前";
	 }else if(_timeDif < (15*day)){
		 return "14天以前";
	 }else if(_timeDif < (16*day)){
		 return "15天以前";
	 }else if(_timeDif < (17*day)){
		 return "16天以前";
	 }else if(_timeDif < (18*day)){
		 return "17天以前";
	 }else if(_timeDif < (19*day)){
		 return "18天以前";
	 }else if(_timeDif < (20*day)){
		 return "19天以前";
	 }else if(_timeDif < (21*day)){
		 return "20天以前";
	 }else if(_timeDif < (22*day)){
		 return "21天以前";
	 }else if(_timeDif < (23*day)){
		 return "22天以前";
	 }else if(_timeDif < (24*day)){
		 return "23天以前";
	 }else if(_timeDif < (25*day)){
		 return "24天以前";
	 }else if(_timeDif < (26*day)){
		 return "25天以前";
	 }else if(_timeDif < (27*day)){
		 return "26天以前";
	 }else if(_timeDif < (28*day)){
		 return "27天以前";
	 }else if(_timeDif < (29*day)){
		 return "28天以前";
	 }else if(_timeDif < (30*day)){
		 return "29天以前";
	 }else{
		 return "1月以前";
	 }
 }
</script>