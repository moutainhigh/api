<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
    <!--我的start-->
     <div class="tab-mine">
         <div class="top" style=" width: 100%; height: 0rem;">
         </div>
         <div id="_indexModule" style="overflow-y: auto;overflow-x:hidden;  width: 100%;">
	         <div class="mine-title">
	             <p><img src="${store.shopLogo }" onerror="javascript:this.src='../resource/img/merchant/mine-tit.png'"/> </p>
	             <h3>${account }</h3>
	             <h4>${store.name }</h4>
	         </div>
	         <!--以背景条位分割线，可重复的内容start-->
	         <ul class="mine-list" >
	             <li class="clearfix" id="marketAccount">
	                 <span class="fl">
	                      <img src="../resource/img/merchant/my-number.png"/> 
	                 </span>
	                 <span class="mine-list-tit fl">营销账户</span>
	                 <strong class="fr">
	                    <span>￥${price }</span>&nbsp;&nbsp;
	                    <i></i>
	                 </strong>
	             </li>
	             <li class="mine-list-no clearfix" id="signInfo">
	                 <span class="fl"><img src="../resource/img/merchant/my-write.png"/> </span><span class="mine-list-tit fl">签约</span><strong class="fr"><i class="icon"></i></strong>
	             </li>
	
	         </ul>
	         <p class="mine-bg"></p>
	        <!--以背景条位分割线，可重复的内容end-->
	         <!--以背景条位分割线，可重复的内容start-->
	         <ul class="mine-list">
	             <li class="clearfix" id="setting">
	                 <span class="fl"><img src="../resource/img/merchant/my-set.png"/> </span><span class="mine-list-tit fl">设置</span><strong class="fr"><i class="icon"></i></strong>
	             </li>
	             <li class="mine-list-no clearfix">
	                 <span class="fl"><img src="../resource/img/merchant/my-about.png"/> </span><span class="mine-list-tit fl">关于</span><strong class="fr"><i class="icon"></i></strong>
	             </li>
	
	         </ul>
	         <p class="mine-bg"></p>
	         <!--以背景条位分割线，可重复的内容end-->
	        <!--以背景条位分割线，可重复的内容start-->
	         <ul class="mine-list">
	             <li class="mine-list-no clearfix">
	                 <span class="fl">
	                 <img src="../resource/img/merchant/my-tel.png"/> </span>
	                 <span class="mine-list-tit fl">客服热线</span>
	                 <strong class="fr">
	                     <a href="tel:400-661-0003">400-661-0003</a>
	                 </strong>
	             </li>
	
	         </ul>
	          <p class="mine-bg"></p>
	         <!--以背景条位分割线，可重复的内容end-->
	     </div>
     </div>
    <!--我的end-->
    <script>
(function(win,doc){
    function change(){
    	
    	var rem = doc.documentElement.clientWidth*50/375;
        $('#_indexModule').css('height',$(window).height()-$('.top').height()-1.2*rem-5);
        $('#_indexModule').css('padding-bottom',$('.bottom ').height());
    }
    change();
    win.addEventListener('resize',change,false);
})(window,document);

      $("#marketAccount").click(function(){
    	  $.post("../storeManage/toMarketAccount",{
    		  auth:_auth,
    		  authURI:"marketAccount",
    		  type:0
    	  },function(result){
    		  if(result.code == 0){
    			  location.href = "../shop/toAccountBalancePage?auth="+_auth;
    		  }else{
    			  jalert.show(result.msg);
    		  }
    	  });
      });
      
      
      $("#signInfo").click(function(){
    	  location.href = "../shop/signInfo?auth="+_auth;
      });
      $("#setting").click(function(){
    	  location.href = "../storeManage/toSetting?auth="+_auth;
      });

    </script>
