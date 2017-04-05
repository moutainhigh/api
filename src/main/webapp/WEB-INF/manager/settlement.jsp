<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>结算</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/settlement.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.select.js"></script>
    <style>
          .js_time{
             border:1px solid #EEE;
             /*box-shadow:0 0 5px #EEE;*/
          }
          .js_time span{
             padding:2px 5px;
          }
          
          input[type=tel]{
		   -webkit-appearance: none;
		    width: 100%;
		    padding: 0;
		    border: 0;
		    background-color: transparent;
		    font-size:100%;
		}
    </style>
</head>
<body>
<input value="${auth}" id="auth" name="auth" type="hidden">
<input value="${storeNo}" id="storeNo" name="storeNo" type="hidden">
    <div class="container">
        <section class="f1">
            <div class="common-mg common-pd">
                <ul class="clearfix">
                    <li>
                        <p>
                            <img src="../resource/img/chuangjianshanghu.png">
                        </p>
                        <p>
                            <span>创建商户</span>
                        </p>
                    </li>
                    <li>
                      <span>
                          <p>
                              <img src="../resource/img/jibenziliao--zhengchang.png">
                          </p>
                          <p>
                              <span>基本资料</span>
                          </p>
                      </span>
                    </li>
                    <li>
                      <span>
                          <p>
                              <img src="../resource/img/jiesuanxinxi--zhengchang.png">
                          </p>
                          <p>
                              <span>结算信息</span>
                          </p>
                      </span>
                    </li>
                    <li>
                      <span>
                          <p>
                              <img src="../resource/img/shenghezhuangtai_zhihu.png">
                          </p>
                          <p>
                              <span>审核状态</span>
                          </p>
                      </span>
                    </li>
                </ul>
            </div>
        </section>
        <section class="f2">
            <div class="common-br">
                <div class="info">
                       <span class="name">
                          提现账号
                       </span>
                </div>
            </div>
            <div class="p-group">
                <p>
                    <label class="span-label" for="sa_name">开户名</label>
                    <span class="span-result"><input type="text" id="sa_name" placeholder="个人姓名"></span>
                </p>
                <p >
                     <label class="span-label" for="_idCard">身份证号</label>
                     <span class="span-result"><input type="tel" id="_idCard" placeholder="请输入法人身份证号"></span>
                 </p>
                <p>
                    <label class="span-label" for="sa_num">电子卡号</label>
                    <span class="span-result">
                        <input type="tel" id="sa_num" placeholder="已开通快捷支付的银行卡">
                    </span>
                </p>
                 <p >
                     <label class="span-label" for="storePhone">开户手机</label>
                     <span class="span-result"><input type="tel" id="storePhone" placeholder="11位正确手机号"></span>
                 </p>
                <p>
                    <label class="span-label" for="sa_bank_name">开户银行</label>
                    <span class="span-result" id="sa_bank_name">
                                                                   中国民生银行
                    </span>
                </p>
                <p >
                    <label class="span-label" for="mer_email">邮箱</label>
                    <span class="span-result">
                            <input type="text" id="mer_email" placeholder="电子邮箱">
                    </span>
                </p>
            </div>
        </section>
        <section class="f2">
	        <div class="common-br">
                 <div class="info">
                       <span class="name">
                          结算信息
                       </span>
                 </div>
             </div>
              <div class="common-br common-mg">
                 <div class="row-label clearfix">
                     <label>结算时间</label>
                     <span class="sh-right" id="_selectRate" data-id="T1">T1</span>
                 </div>
             </div>
              <div class="common-br common-mg">
                   <div class="row-label clearfix">
                       <label>费率</label>
                       <div class="sh-right">
                          <div class="js_time">
                               <span id="_rate" data-id="0.6">0.6</span>
                        </div>
                     </div>
                   </div>
              </div>
         </section>
        <section class="f3" id="_submit">
            <div class="next">下一步</div>
        </section>
    </div>
</body>
</html>
<script>
    var auth = $("#auth").val();
    var storeNo = $("#storeNo").val();
    $(function(){
        load();
    });

    function load(){
        $("#_submit").on("touchend",function(){
            _submit();
        });
        
      //费率
        $("#_rate").on("click",function(){
            	jselect.operateObj.defaultsel = $(this).attr("data-id");
            	jselect.operateObj.curObj = $(this);
            	jselect.init();
            	jselect.add({
    				  msg:'0.3',
    				  id:'0.3',
    			  }).add({
    				  msg:'0.38',
    				  id:'0.38',
    			  }).add({
    				  msg:'0.4',
    				  id:'0.4',
    			  }).add({
    				  msg:'0.45',
    				  id:'0.45',
    			  }).add({
    				  msg:'0.5',
    				  id:'0.5',
    			  }).add({
    				  msg:'0.55',
    				  id:'0.55',
    			  }).add({
    				  msg:'0.6',
    				  id:'0.6',
    			  });
            	jselect.show();
            });
    };


    function _submit(){
        var sa_name = $.trim($("#sa_name").val());
        var _idCard = $.trim($("#_idCard").val());
        var sa_num = $.trim($("#sa_num").val());
        var _phone = $.trim($("#storePhone").val());
        var sa_bank_name = $.trim($("#sa_bank_name").text());
        var mer_email = $.trim($("#mer_email").val());
        
        var _rate = $.trim($("#_rate").attr("data-id"));
        var _selectRate = $.trim($("#_selectRate").attr("data-id"));
        
        if(sa_name=="" ||_idCard=="" || sa_num=="" || _phone=="" ||sa_bank_name==""||mer_email==""){
            jalert.show("账号信息请填写完整");
            return;
        }
        var myreg = /^(1+\d{10})$/;
        if(!myreg.test(_phone)) {
            jalert.show('请输入有效的手机号码！');
            return false;
        }
        
        var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        if(reg.test(_idCard) === false)
        {
            jalert.show("身份证输入不合法");
            return  false;
        }
        
        re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
        if(!re.test(mer_email)){
            jalert.show("邮箱填写错误");
            return;
        }
        
        var _rate = $.trim(_rate);
        var _selectRate = $.trim(_selectRate);
        console.log(_rate);
        return false;
        var jsonData = {"saName":sa_name,"saNum":sa_num,"saBankName":sa_bank_name,"merEmail":mer_email,"auth":auth,
        		"storeNo":storeNo,"settlementType":_selectRate,"rate":_rate,"idCard":_idCard,"phone":_phone};
        $.post("./auditStatus",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                jalert.show(obj.msg);
            }
        });
    }
</script>