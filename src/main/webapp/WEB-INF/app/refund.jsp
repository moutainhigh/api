<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <meta charset="UTF-8">
    <title>退款</title>
    <meta charset="UTF-8">
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <style type="text/css">
    body{
	    font: 12px/1.5 Microsoft YaHei,tahoma,arial,Hiragino Sans GB,\\5b8b\4f53,sans-serif;
	    color: #666;
    }
            .content{
               font-size:1rem;
            }
		    .row{
		       padding:.45rem 0;
		       font-size:0.9rem;
		    }
		    fieldset {
		        border-color:#EEE !important;
		    }
		    legend{
		       color:#ff9649;
		    }
          .row label{
             width:25%;
             text-align:right;
             display:inline-block;
             color:#4a4949;
             vertical-align:middle;
          }
          .row .row_ipt{
             width:70%;
             text-align:left;
             display:inline-block;
          } 
          .row_ipt input{
             width:100%;
             padding:.25rem;
             border:1px solid #CCC;
             border-radius:5px;
             font-size:.75rem;
          }   
          .row_ipt input:focus{
             border:1px solid #ff9649;
          }
          .serach{
             text-align:center;
          }  
          #serach, #refund{
             padding:2px .95rem;
             background:#ff9649;
             color:#FFF;
             border-radius:5px;
             box-shadow: 0 0 5px #ff9649;
          }  
          
          .redund_detail{
             padding:.5rem;
          }  
          
          .refund_order{
             margin-top:1.35rem;
             background:#EEE;
             position:relative;
          }
          .refund_order .tips{
            position:absolute;
            top: -2.2rem;
            left: 2rem;
          }
          i{
		    width: 0;
		    height: 0;
		    border-left: 1rem solid transparent;
		    border-right: 1rem solid transparent;
		    border-bottom: 1rem solid #EEE;
            
          }
          .refund_order input{
             border:0;
          }
          img{
            width:2.75rem;
            height:2.75rem;
          }
          .details{
            line-height:1.325rem;
            vertical-align: top;
            font-size:.8rem;
          }
          .details div{
             border-bottom:1px solid #DDD;
             padding:0 10px;
          }
          .details del{
             font-size:.6rem;
             color:#bbb;
             margin-left:.5rem;
          }
          #status{
            margin-left:1rem;
            padding:1px .3rem;
            background:#ff9649;
            color:#EEE;
            border-radius:5px;
            font-size:0.6rem;
          }
          .color{
            color:#666;
          }
    </style>
    <script type="text/javascript">
    (function(win,doc){
        function change(){
            doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/375+'px';
        }
        change();
        win.addEventListener('resize',change,false);
    })(window,document);
    
    </script>
  </head>
  
  <body>
     <div class="content">
         <input type="hidden" id="storeNo" value="${storeNo }">
         <input type="hidden" id="userId" value="${userId }">
         <div class="refund_con">
             <fieldset>
			     <legend>退款条件</legend>
			     <div class="row">
			         <label>订单号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="orderId" placeholder="输入退款的订单号">
			         </div>
			     </div>   
			     <div class="row">
			         <label>交易号:</label>
			         <div class="row_ipt">
			             <input type="tel" id="transId" placeholder="输入退款的交易号">
			         </div>
			     </div>        
			     <div class="row serach">
			         <span id="serach">搜&nbsp;索</span>
			     </div>
			  </fieldset>
         </div>
	     
	     <div class="refund_order">
	             <div class="tips">
	               <i></i>
	             </div>
	             <div class="redund_detail">
	                <div class="row">
	                   <label>
	                       <img alt="" src="../resource/img/app/order/wechat-ico.png">
	                   </label>
	                   <div class="row_ipt details">
	                       <div><span class="color">￥2.0</span><del>￥1.0</del><span id="status">退款中</span></div>
	                       <div><span class="color">2017-10-10 10:16:15</span></div>
                       </div>
	                </div>
	                
	                <div class="row">
	                   <label>退款金额:</label>
	                   <div class="row_ipt">
				             <input type="number" id="ctime" value="1" >
				         </div>
	                </div>
	                <div class="row serach">
				         <span id="refund">退&nbsp;款</span>
				     </div>
	             </div>
	         </div>   
	  </div>
  </body>
</html>
<script type="text/javascript">
   $(function(){
	   $("#serach").on("click",function(){
		   var data = {
				 orderId:$("#orderId").val(),
		         transId:$("#transId").val()
		   };
		   $.post("",data,function(result){
			   if(result.code == 0){
				   
			   }else{
				   alert(result.msg);
			   }
			   
		   });
	   });
   });
</script>
