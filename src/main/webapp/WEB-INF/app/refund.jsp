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
    <link href="../resource/css/app/refund.css" rel="stylesheet" type="text/css">
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
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
         <input type="hidden" id="accountId" value="${accountId }">
         <div class="refund_con">
             <fieldset>
			     <legend>退款条件</legend>
			     <div class="row">
			         <label>交易号:</label>
			         <div class="row_ipt">
<<<<<<< HEAD
			             <input type="tel" id="transId" placeholder="输入退款的交易号"  onclick="cur_focus(this);" autofocus="autofocus">
=======
			             <input type="tel" id="transId" placeholder="输入退款的交易号">
>>>>>>> master
			         </div>
			     </div>   
			     <div class="row">
			         <label>商户单号:</label>
			         <div class="row_ipt">
<<<<<<< HEAD
			             <input type="tel" id="orderId" placeholder="输入退款的订单号"  onfocus="cur_focus(this);" >
=======
			             <input type="tel" id="orderId" placeholder="输入退款的订单号">
>>>>>>> master
			         </div>
			     </div>   
			     <div class="row tt">
			        <span id="t">*&nbsp;订单号和交易号至少一个必填!</span>
			     </div>     
			     <div class="row serach">
			         <span id="serach">搜&nbsp;索</span>
			     </div>
			     
			  </fieldset>
         </div>
	     <div class="reminders">
	           <div class="reminders_container">
	             <i>!</i>
		         <span id="rem"></span>
	         </div>
	     </div>
	     <div class="refund_order">
	             <div class="tips">
	               <i></i>
	             </div>
	             <div class="redund_detail">
	                <input type="hidden" id="oid">
	                <div class="row">
	                   <label>退款金额:</label>
	                   <div class="row_ipt refund_ipt">
				             <input type="number" id="money" value="" onfocus="cur_focus(this);">
				         </div>
		              <div class="refund-w">
					         <span id="refund">退&nbsp;款</span>
					  </div>
	                </div>
	                <div class="row">
	                   <label>
	                       <img alt="" src="../resource/img/app/order/wechat-ico.png" id="logo">
	                   </label>
	                   <div class="row_ipt details">
	                       <div><span class="color">￥<span id="am"></span></span><del>￥<span id="pm"></span></del><span id="status"></span></div>
	                       <div><span class="color" id="ctime"></span></div>
                       </div>
	                </div>
	                
	             </div>
	         </div>   
	  </div>
	  <div class="wwt_keyboard">
	     <div class="keyboard first" id="keyboard">
	       <span>智慧商街</span>
	     </div>
	     <div class="complete first" id="complete">
	       <span>完成</span>
	     </div>
	     <div class="wwt_num" id="no1">1</div>
	     <div class="wwt_num" id="no2">2</div>
	     <div class="wwt_num" id="no3">3</div>
	     <div class="wwt_num" id="no4">4</div>
	     <div class="wwt_num" id="no5">5</div>
	     <div class="wwt_num" id="no6">6</div>
	     <div class="wwt_num" id="no7">7</div>
	     <div class="wwt_num" id="no8">8</div>
	     <div class="wwt_num" id="no9">9</div>
	     <div class="wwt_num" id="no10">.</div>
	     <div class="wwt_num" id="no0">0</div>
	     <div class="wwt_num delbtn" id="no11">
	        <i class="ico-del">删除<s></s></i>
	     </div>
	  </div>
  </body>
</html>
<script src="../resource/js/app/refund.js"></script>

