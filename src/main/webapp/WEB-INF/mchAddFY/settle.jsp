<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<head>
    <meta charset="UTF-8">
    <title>结算信息</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/base.css"/>
    <link rel="stylesheet" href="../resource/css/mchAdd/settle.css"/>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script src="../resource/js/fastclick.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.select.js"></script>
    
    
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/320+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>
	<style type="text/css">
		.settlement-tit{font-size:0.6rem}
		.settlement-input input{font-size:0.5rem}
		.mask{height:1.1rem}
	</style>
</head>
<input value="${auth}" id="auth" name="auth" type="hidden">
<input value="${info.storeNo}" id="storeNo" name="storeNo" type="hidden">
<div class="item">
    <div class="create-title">
        <div class="create-icon">
            <ul class="clearfix create-icon-lis">
                <li class="fl create-right">
                        <i class="create-box"><img src="../resource/img/mchAdd/chuangjnianshanghu.png" class="tit-icon"/></i><span class="tit-arrow-pos create-box2"><img src="../resource/img/mchAdd/zhihujiantou.png" id="tit-arrow1"/></span>
                        <p class="font-white">创建商户</p>
                </li>
                <li class="fl create-right">
                        <i class="create-box3"><img src="../resource/img/mchAdd/jibenziliao-1.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow2-2"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow "/></span>
                        <p class="font-white">基本资料</p>
                </li>
                <li class="fl create-right">
                        <i class="create-box3"><img src="../resource/img/mchAdd/jiesuanxinxi-1.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow3-3"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow"/></span>
                        <p class="font-white">结算信息</p>
                </li>
                <li class="fl">
                    <i class="create-box4"><img src="../resource/img/mchAdd/shenhetongguo-2.png" class="tit-icon2"/></i>
                    <p class="font-gray">审核状态</p>
                </li>
            </ul>
        </div>
    </div>

    <div class="base-con">
    	<div class="settlement-box settlement-padd" >
    		<span  class="settlement-tit">
           		<input type="radio" name="acnt_type" id="acnt_type" checked="checked" value="1" style="-webkit-appearance:radio;">企业账户
           		&nbsp;&nbsp;
           		<input type="radio" name="acnt_type" id="acnt_type" value="2" style="-webkit-appearance:radio;box-sizing: border-box;">个人账户
          		<span class="settlement-input" id="_aaf" style="margin-left:14px;border:1px dashed #000;display:none" ><input id="_acntArtifFlag"  style="width:4.6rem;font-size:0.6rem;" value="法人入账" class="please-input please-input1" data-id="1" /></span>
          	</span>
        </div>
        <div class="settlement-box settlement-padd">
            <span class="settlement-tit">银行卡号:</span><span class="settlement-input"><input id="_acnt_no" value="${info.acnt_no }" class="please-input please-input1" type="tel" placeholder="请输入银行卡号" /></span>
        </div>
         <div class="settlement-box settlement-padd">
             <span class="settlement-tit">开户银行:</span><span class="settlement-input"><input id="_iss_bank_nm" value="${info.iss_bank_nm }"  class="please-input" type="text" placeholder="请填写开户行"/></span>
         </div>
         <div class="settlement-box settlement-padd">
             <span class="settlement-tit">联行号:</span><span class="settlement-input"><input id="_inter_bank_no" value="${info.inter_bank_no }"  class="please-input" type="text" placeholder="请填写开户行联行号"/></span>
         </div>
          <div class="settlement-box settlement-padd">
              <span class="settlement-tit">开&nbsp;&nbsp;户&nbsp;&nbsp;名:</span><span class="settlement-input please-input1"><input id="_acnt_nm" value="${info.acnt_nm }" class="please-input" type="text" placeholder="个人姓名或公司名称"/></span>
          </div>
		   <!--身份证号-->
	        <div class="settlement-box settlement-padd">
	            <span class="settlement-tit">身份证号:</span><span class="settlement-input"><input id="_acnt_certif_id" value="${info.acnt_certif_id}" class="please-input " type="text" placeholder="请输入身份证号"/></span>
	        </div>
       

    </div>


    <!--结算信息-->
    <div class="base-con ">
        </div>
        <!--结算时间-->
		<div class="set-time-tit fl">
		        <span class="settlement-tit sett-time" style="margin-top: 0.4rem; display: block;">费率(支付宝):</span>
		        <span class="settlement-tit sett-time" style="margin-top: 0.9rem; display: block;">费率(微信):</span>
		</div>
        <div class="settlement-box-last settlement-time fr">
            <input type="button" id="_aliRate" value="0.6" class="mask mask-one"/>
            <input type="button" id="_wxRate" value="0.6" class="mask mask-one"/>
        </div>


    <!--下一步-->

                    <div class="next-box">
                        <input id="_submit" type="button" value="下一步" class="industy-next"/>
                    </div>

                </div>
    <!--遮罩层费率支付宝-->
<div class="" style="position: absolute;">
        <div class="popup set-box" style="position: fixed; background:#efefef; bottom:0; display: none; width:100%">
            <input class="popup-one" type="button" attr="aaa" value="0.3" style="width:100%; height:2.4rem; border-bottom:1px solid #ccc;"/>
            <input class="popup-one" type="button" attr="aaa" value="0.38" style="width:100%; height:2.4rem; border-bottom:1px solid #ccc;"/>
            <input class="popup-one" type="button" attr="aaa" value="0.45" style="width:100%; height:2.4rem; border-bottom:1px solid #ccc;"/>
            <input class="popup-one" type="button" attr="aaa" value="0.6" style="width:100%; height:2.4rem; "/>
        </div>
</div>
    <!--遮罩层费率end-->
    <!--微信-->
    <div class="cutofftime popup2 set-box" style="position: fixed; background:#efefef; bottom:0; display: none; width:100%">
        <input class="popup-one" type="button" attr="bbb" value="0.3" style="width:100%; height:2.4rem; border:1px solid #ccc;"/>
        <input class="popup-one" type="button" attr="bbb" value="0.38" style="width:100%; height:2.4rem; border:1px solid #ccc;"/>
        <input class="popup-one" type="button" attr="bbb" value="0.45" style="width:100%; height:2.4rem; border:1px solid #ccc;"/>
        <input class="popup-one" type="button" attr="bbb" value="0.6" style="width:100%; height:2.4rem; "/>
    </div>
    <!--结算时间end-->

</div>

</body>
</html>
<script>

$(function(){
	load();
})

function load(){
	 $("#_submit").on("click",function(){
         _submit();
     });
	 $(":radio").click(function(){
		 var value=$(this).val();
		 if(value==2){
			 $("#_aaf").show();
		 }else{
			 $("#_aaf").hide();
		 }
	 });
	 
	 loadAcntArtifFlag();
}

function _submit(){
	var _acnt_type = $("input[name='acnt_type']:checked").val();
	var _aaf = $.trim($("#_acntArtifFlag").attr("data-id"));
	
	 var _acnt_no = $.trim($("#_acnt_no").val()).replace(/\s/g,"");
     var _iss_bank_nm = $.trim($("#_iss_bank_nm").val());
     var _inter_bank_no = $.trim($("#_inter_bank_no").val());
     var _acnt_nm = $.trim($("#_acnt_nm").val());
     var _acnt_certif_id = $.trim($("#_acnt_certif_id").val());
     var _wx_set_cd = $.trim($("#_wxRate").val());
     var _ali_set_cd = $.trim($("#_aliRate").val());
     var storeNo = $.trim($("#storeNo").val());
     var auth = $.trim($("#auth").val());
     if(_acnt_no == "" || _iss_bank_nm == "" || _inter_bank_no == ""){
    	 jalert.show("请完整填写银行信息");
         return;
     }
     if(_acnt_nm == "" || _acnt_certif_id == ""  ){
    	 jalert.show("请完整填写开户信息");
         return;
     }
     if(_wx_set_cd == "" || _ali_set_cd == "" ){
    	 jalert.show("请完整填写费率信息");
         return;
     }
     $("#_submit").css("background","#d3d3d3");
     $("#_submit").unbind("click");
     var jsonData = {"acnt_type":_acnt_type,"acnt_artif_flag":_aaf,
    		 		 "acnt_no":_acnt_no,"iss_bank_nm":_iss_bank_nm,"inter_bank_no":_inter_bank_no,
    		 		 "acnt_nm":_acnt_nm,"acnt_certif_id":_acnt_certif_id,
    		 		 "wx_set_cd":_wx_set_cd,"ali_set_cd":_ali_set_cd,
					"storeNo":storeNo,"auth":auth};
		$.post("./mchSettle",jsonData,function(obj){
		  if(obj.code == 0){
		      location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
		  }else{
			  $("#_submit").css("background","#fc324a");
	             $("#_submit").on("click",function(){
	                 _submit();
	             });
		      jalert.show(obj.msg);
		  }
		});
}

function loadAcntArtifFlag(){
	 //开户银行
    $("#_acntArtifFlag").on("click",function(){
    	jselect.operateObj.defaultsel = $(this).attr("data-id");
    	jselect.operateObj.curObj = $(this);
    	jselect.init();
    	jselect.add({
			  msg:'非法人入账',
			  id:'0',
			  exec:function(){backfill("_acntArtifFlag")}
		  }).add({
			  msg:'法人入账',
			  id:'1',
			  exec:function(){backfill("_acntArtifFlag")}
		  });
  	jselect.show();
    });
}

function backfill(_id){
	var _text = $("#"+_id).text();
	$("#"+_id).text("");
	$("#"+_id).val(_text);
}


$('.mask').click(function(){
    $('.mask').removeClass('on-bg').eq($(this).index()).addClass('on-bg');
    $('.set-box').fadeOut().eq($(this).index()).fadeIn();
    $('.set-box input').click(function(){
        $('.on-bg').val($(this).val());
        $('.set-box').fadeOut();
    })

});



  //身份证隔四位有一个空格
  $('.please-input1').on('keyup mouseout input click',function(){
        var $this = $(this);
        var v = $this.val();
        /\S{5}/.test(v) && $this.val(v.replace(/\s/g, '').replace(/(.{4})/g, "$1 "));
    })
</script>