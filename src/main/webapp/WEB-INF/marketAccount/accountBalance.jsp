<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>账户余额</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script type="text/javascript" src="../resource/js/Dutil.js"></script>
    <style type="text/css">
          .balance{
              background: #FFF;
              margin:0 auto;
              text-align: center;
          }
          .money_logo{
              padding-top:.8rem;
          }
          .my_balance_title,
          .my_money ,
          .to_wechat_packet{
               padding-top:.5rem;
          }
          .money_logo span{
              width: 4rem;
              height: 4rem;
              background: #14d212;
              margin: 0 auto;
              color: #EEE;
              border-radius: 50%;
              font-size: 3rem;
              display: block;
              line-height: 4.3rem;
              font-weight: bold;
          }
         .my_balance_title{
             color:#444;
         }
         .my_money{
             font-size:1.2rem;
         }
        .my_money #balance{
            font-size:1.1rem;
        }
        .to_wechat_packet{
            padding-bottom: .8rem;
        }
        .to_wechat_packet span{
            font-size: .8rem;
            padding: 4px 2rem;
            background: #fc324a;
            border-radius: 5px;
            color: #FFF;
        }
        .balance_detail .title{
            font-size:.9rem;
            margin-left:1rem;
            padding:.75rem 0;
            font-weight:600;
            color:#666;
        }
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
            padding-bottom: .25rem;
            font-weight: 600;
            color: #777;
        }
        .content p:last-child{
            font-size: .7rem;
            color: #999;
        }
        .header_fixed{
             position: fixed;
			 top: 0;
			 left: 0;
			 width: 100%;
			 z-index: 10000;
			 box-shadow: 0 1px 5px #cdc1c1;
        }
        .header_fixed > .balance_detail{
             background:#EEE;
        }
        .money_list{
            margin-top:14.5rem;
        }
        
        /*选项卡*/
        .on{
/*             border-bottom:2px solid #ccc; */
            background:#fff;
        }
        .balance_detail div{
            font-size:0.7rem;
        }
        #money-detail1, money-detail2{
            display: none;
        }
        input{
            border:0;
            width:50%;
            padding:1rem;
            background:#f0f0f0;
            line-height:0.4rem;
            text-align: center;
            font-size:0.8rem;
        }

        /*解决input变圆的问题*/
        input, textarea {
           -webkit-appearance: none;
           -webkit-border-radius: 0;
       }
    </style>
</head>
<body>
  <input type="hidden" id="auth" value="${auth}">
   <div class="container">
          <div class="header_fixed">
	          <section class="balance">
	                <div class="money_logo">
	                      <span>￥</span>
	                </div>
	                <div class="my_balance_title">
	                    我的余额
	                </div>
	                <div class="my_money">
	                   <span id="balance">${price}</span>元
	                </div>
	                <div class="to_wechat_packet" >
	                    <span id="withdraw">提现至微信钱包</span>
	                </div>
	          </section>
	          <section class="balance_detail" id="balance_tbn">
	                <input type="button" value="余额" class="on" /><input type="button" value="提现" />
	          </section>
          </div>
          <section class="balance_detail money_list" id="balance_detail">
                <!--余额详细内容-->
                <h3 class="itemList" id="money-detail1" style="display: block;">
                    
                </h3>
              <!--余额详细内容end-->
              <!--提现详细内容-->
                <h3 class="itemList" id="money-detail2" style="display: none;"   >

                </h3>
              <!--提现详细内容end-->
                     

          </section>

   </div>
</body>
</html>
<script>
    $(function(){
    	var auth = $("#auth").val();
        $("#withdraw").on("click",function(){
             location.href="./toWithDrawWxWallet?auth="+auth;
        });
//         var oBox=document.getElementById("balance_tbn");
//         var aBtn=oBox.getElementsByTagName("input");
//         var oD = document.getElementById("balance_detail");
//         var aDiv=oD.getElementsByTagName("h3");
//         for(var i=0;i<aBtn.length;i++){
//             aBtn[i].index=i;
//             aBtn[i].onclick=function(){
//                 for(var i=0;i<aBtn.length;i++){
//                     aBtn[i].className='';
//                     aDiv[i].style.display='none';
//                 }
//                 this.className='on';
//                 aDiv[this.index].style.display='block';
//                 if(i == 0){
//                 	if(page_income == 1){
//                 		load(page_income,type_income);
//                 	}
//                 }else if(i == 1){
//                 	if(page_pay == 1){
//                 		load(page_pay,type_pay);
//                 	}
//                 }
//             };
//         }
         var inputs = $("#balance_tbn").find("input");
         inputs.on("click",function(){
        	 $(this).siblings("input").removeClass("on");
        	 var index = inputs.index($(this));
        	 $($("h3")[index]).show().siblings().hide();
        	 $(this).addClass("on");
        	 if(index == 0){
        		 if(page_income == 1){
             		load(page_income,type_income);
             	}
        	 }else{
        		 if(page_pay == 1){
             		load(page_pay,type_pay);
             	}
        	 }
         });
        
        //page_income余额默认页
        //page_pay 提现默认页
        //type_income 余额类型1
        //type_pay 提现类型2
        //isUseData_income 默认可以加载余额
        //isUseData_pay 默认可以加载提现
        var page_income = 1,page_pay = 1,
        type_income = 1,type_pay = 2,
        pageSize = 10,isUseData_income = true,
        isUseData_pay = true;
        
        load(page_income,type_income);//默认type=1收入 type=2支出
        function load(page,type){
        	$.post("./getListByStoreNo",{
        		auth:auth,
        		type:type,
        		page:page,
        		pageSize:pageSize
        	},function(result){
        		console.log(result);
        		if(result.code == 0){
        			var items = result.data,
        			    len = items.length;
        			var s = "";
        			for(var i =0;i<len;i++){
        				if(items[i].type == 1){
            				items[i].type = '收入';
            			}else if(items[i].type == 2){
            				items[i].type = '提现';
            			}
        				s += '<div class="item">'
                        +'<div class="item_detail">'
                        +'<div class="content">'
                        +'  <p>'+items[i].type+'</p>'
                        +'  <p>'+new Date(items[i].ctime*1000).Format("yyyy-MM-dd hh:mm")+'</p>'
                        +'</div>'
                        +'<div class="money">'
                        +'    <p>'+items[i].price+'元</p>'
                        +'</div>'
                        +'</div>'
                        +'</div>';
        			}
        			
	        			
	        			if(type == type_income){
	        				if(len < pageSize){
	        					isUseData_income = false;
		        			}
	        				page_income++;
	        				$("#money-detail1").append(s);
        				}else{
        					if(len < pageSize){
        					    isUseData_pay = false;
        					}
        					page_pay++;
        					$("#money-detail2").append(s);
        				}
        		}else{
        			jalert.show('error');
        		}
        	});
        }
        
        window.onscroll = pageQuery;
        function pageQuery(){
                if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
                	var inputs = $("#balance_tbn").find("input");
                	for(var i = 0,len = inputs.length ;i < len; i++){
                		var cla = $(inputs[i]).attr("class");
                		if(cla.indexOf("on") != -1){
                			if(i == 0){
                				if(isUseData_income){
                                    load(page_income,type_income);
                             	}
                			}else{
                				if(isUseData_pay){
                                    load(page_pay,type_pay);
                             	}
                			}
                		}
                	}
                	
                }
        }
    });
</script>