<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>提现</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">
        .f1{
            background: #FFF;
        }
        .f1 .row:not(:last-child){
            border-bottom:1px solid #EEE;
        }
        .row .input_row{
            margin:0 1rem;
            padding:.75rem 0;
            display: flex;
            font-size: .85rem;
        }
        .input_row label{
            width:4rem;
            color: #666;
        }
        .input_row .input_text{
            flex:1;
            position: relative;
        }
        .input_text input{
            width:100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            font-size:.7rem;
        }
        #all_money{
          margin-left:1rem;
        }
        .input_text i{
            font-style: normal;
		    font-size: .8rem;
		    position: absolute;
		    top: 0;
		    left: 0;
        }
        .input_text span{
            position: absolute;
            right:0;
            padding:3px .7rem;
            background: #fc324a;
            color: #FFF;
            font-size:.7rem;
            top:-2px;
            border-radius: 5px;
        }
        .f2{
            margin-top: .9rem;
            text-align: center;
            background: #fc324a;
            color: #FFF;
        }
        .f2 .btn{
            padding:.6rem 0;
        }
        .warm_tips{
            margin-top:.9rem;
            padding-left:1rem;
        }
        .warm_tips .title{
            font-size: .8rem;
            padding-bottom: .5rem;
            font-weight: bold;
            opacity: .7;
        }
        .warm_tips .content{
            font-size: .7rem;
            color: #555;
        }
        .warm_tips .content p{
            padding-bottom:.25rem;
        }
    </style>
</head>
<body>
<input type="hidden" id="auth" value="${auth}">
<div class="container">
    <section class="f1">
        <div class="row">
            <div class="input_row">
                <label>提取现金</label>
                <div class="input_text">
                    <input type="text" placeholder="可用余额￥ ${price}" autofocus id="input_money">
                    <span id="all_withdraw">全部提现</span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="input_row">
                <label>真实姓名</label>
                <div class="input_text">
                    <input type="text" placeholder="请输入真实姓名" id="realname">
                </div>
            </div>
        </div>
    </section>

    <section class="f2" id="apply_withdraw">
        <div class="btn">
            <span>申请提现</span>
        </div>
    </section>

    <section class="warm_tips">
          <div class="title">温馨提示</div>
          <div class="content">
               <p>1、本次最大金额可提现10000.00元</p>
               <p>2、当天支持多次提现</p>
          </div>
    </section>
</div>
</body>
</html>

<script>
    $(function(){
    	var auth = $("#auth").val(),all_money=${price};
        $("#apply_withdraw").on("click",function(){
        	   var input_money = $("#input_money").val(),
        	       realname = $("#realname").val();
//         	   console.log($("#input_money"));
//         	   console.log(input_money);
        	   if(input_money == ""){
        		   jalert.show('请输入提现金额');
        		   return false;
        	   }
        	   if(realname == ""){
        		   jalert.show('请输入真实姓名');
        		   return false;
        	   }
        	   if(!/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/.test(input_money) ){
        		    jalert.show('有不合法字符');
        		    return false;
        	   }
        	   if(parseFloat(all_money) == 0){
        		   jalert.show('没有可用余额');
        		   return false;
        	   }
        	   if(parseFloat(input_money) > parseFloat(all_money)){
        		   jalert.show('提现金额不能大于可用余额');
        		   return false;
        	   }
        	   
               $.post("./withDrawWx",{
            	   auth:auth,
            	   amount:input_money,
            	   realname:realname
               },function(result){
            	   if(result.code == 0){
            		    jalert.show('提现成功');
            		    location.href="./toAccountBalancePage?auth="+auth;
            	   }else if(result.code == 2 || result.code == 3 || result.code == 5){
            		   jalert.show(result.msg);
            	   }else{
            		   jalert.show('提现失败');
            	   }
               });
        });

        $("#all_withdraw").on("click",function(){
               if(all_money*100 > 0){
                  $("#input_money").val(all_money);
               }else{
            	   jalert.show('没有可用余额');
            	   return;
               }
        });
        
    });
</script>