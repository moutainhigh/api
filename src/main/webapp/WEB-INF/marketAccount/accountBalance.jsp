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
            padding-bottom:.25rem;
            font-weight:600;
            color: #777;
        }
        .content p:last-child{
            font-size:.7rem;
            color: #999;
        }
        .header_fixed{
             position: fixed;
			 top: 0;
			 left: 0;
			 width: 100%;
			 z-index: 10000;
			 box-shadow: 0 1px 15px #cdc1c1;
        }
        .header_fixed > .balance_detail{
             background:#EEE;
        }
        .money_list{
            margin-top:13.4rem;
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
	                <div class="to_wechat_packet" id="withdraw">
	                    <span>提现至微信钱包</span>
	                </div>
	          </section>
	          <section class="balance_detail">
	                <div class="title">
	                         <span>余额明细</span>
	                </div>
	          </section>
          </div>
          <section class="balance_detail money_list">
                <div class="itemList" id="itemList">
<!--                      <div class="item"> -->
<!--                             <div class="item_detail"> -->
<!--                                   <div class="content"> -->
<!--                                          <p>返现</p> -->
<!--                                          <p>2017-02-27 16:13:13</p> -->
<!--                                   </div> -->
<!--                                   <div class="money"> -->
<!--                                       <span>+10000.00</span>元 -->
<!--                                   </div> -->
<!--                             </div> -->
<!--                      </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>返现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
<!--                     <div class="item"> -->
<!--                         <div class="item_detail"> -->
<!--                             <div class="content"> -->
<!--                                 <p>提现</p> -->
<!--                                 <p>2017-02-27 16:13:13</p> -->
<!--                             </div> -->
<!--                             <div class="money"> -->
<!--                                 <span>+10000.00</span>元 -->
<!--                             </div> -->
<!--                         </div> -->
<!--                     </div> -->
                </div>

          </section>

   </div>
</body>
</html>
<script>
    $(function(){
    	var auth = $("#auth").val();
        $("#withdraw").on("click",function(){
//             jalert.show('提现至微信钱包');
             location.href="./toWithDrawWxWallet?auth="+auth;
        });
        
//         $.post("./getAccountBalance",{
//         	auth:auth
//         },function(result){
//         	console.log(result);
//         	if(result.code == 0){
//         		$("#balance").text(result.data);
//         	}else{
//         		jalert.show('error');
//         	}
//         });
        var page = 1,pageSize = 10,isUseData = true;
        load(page);
        function load(page){
        	$.post("./getListByStoreNo",{
        		auth:auth,
        		page:page,
        		pageSize:pageSize
        	},function(result){
        		console.log(result);
        		if(result.code == 0){
        			var items = result.data,
        			    len = items.length;
        			if(len == 0){
        				jalert.show('暂无数据了');
        				return;
        			}
        			if(len < pageSize){
        				isUseData = false;
        			}
        			
        			var s = "";
        			for(var i =0;i<len;i++){
        				if(items[i].type == 1){
            				items[i].type = '提现';
            			}else if(items[i].type == 2){
            				items[i].type = '活动返现';
            			}
        				s += '<div class="item">'
                        +'<div class="item_detail">'
                        +'<div class="content">'
                        +'  <p>'+items[i].type+'</p>'
                        +'  <p>'+new Date(items[i].ctime*1000).Format("yyyy-MM-dd hh:mm")+'</p>'
                        +'</div>'
                        +'<div class="money">'
                        +'    <span>'+items[i].price+'</span>元'
                        +'</div>'
                        +'</div>'
                        +'</div>';
        			}
        			
        			$("#itemList").append(s);
        		}else{
        			jalert.show('error');
        		}
        	});
        }
        
        
        window.onscroll = pageQuery;
        function pageQuery(){
                if(document.body.scrollTop >= document.body.scrollHeight - window.innerHeight-30){
                	if(isUseData){
                       load(++page);
                	}
                }
        }
    });
</script>