<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>         
<html>
<head>
    <meta charset="UTF-8">
    <title>基本资料</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/realnameauth.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style>

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
                              <img src="../resource/img/jiesuanxinxi_zhihui.png">
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
                           商户信息
                       </span>
                  </div>
              </div>
             <div class="common-br">
                 <div class="row-label-store-address">
                     <label for="_province">
                                               商户地址
                     </label>
                     <div class="select-pro-city">
                         <ul class="clearfix">
                             <li>
	                             <select id="_province">
	                                 <option value="0">请选择省份</option>
	                             </select>
                             </li>
                             <li>
		                         <select id="_city">
		                             <option value="0">请选择城市</option>
		                         </select>
		                     </li>
                         </ul>
                     </div>
                 </div>
                 <div class="row-label-address">
                     <label for="_address">
                                                      详细地址
                     </label>
                     <span >
                         <input id="_address" type="text" placeholder="请输入详细地址">
                     </span>
                 </div>
             </div>
             <div class="common-br">
                 <div class="row-label-hytype">
                     <label for="_businessType">行业类型</label>
                 </div>
                 <div class="row-label-hytype-select">
                     <ul class="clearfix">
                        <li>
	                         <select>
	                             <option>商户类型</option>
	                             <option>个体工商户</option>
	                         </select>
                        </li>
                        <li>
	                         <select>
	                             <option>行业大类</option>
	                             <option>餐饮/食品</option>
	                             <option>线下零售</option>
	                             <option>其它</option>
	                         </select>
                        </li>
                        <li>
	                         <select id="_businessType">
	                             <option value="-1">类目</option>
	                             <option value="292">食品,292</option>
	                             <option value="153">餐饮,153</option>
	                             <option value="209">便利店,209</option>
	                             <option value="210">其他综合零售,210</option>
	                             <option value="158">其他行业,158</option>
	                         </select>
                         </li>
                     </ul>
                 </div>
             </div>

         </section>
         <section class="f4" id="_submit">
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
        $("#_province").on("change",function(){
            var selOpt = $("#_city option");
            selOpt.remove();
            $("#_city").append("<option value=\"0\">请选择城市</option>");
            var _province = $("#_province").val();
            _selectCity("_city",_province)
        });
        _selectCity("_province","0");
    };

    function _selectCity(_id,cityCode){
        $.post("../getCityCode",{"cityCode":cityCode},function(data){
            if(data.code == 0){
                var options = "";
                $.each(data.data,function(n,obj) {
                    options += "<option value=\""+obj.code+"\">"+obj.name+"</option>";
                });
                $("#"+_id).append(options);

            }else{
                jalert.show("出错了");
            }
        })
    }

    function _submit(){
        var _city = $.trim($("#_city").val());
        var _address = $.trim($("#_address").val());
        var _businessType = $.trim($("#_businessType").val());
        if(_city=="0"||_address==""||_businessType=="-1"){
            jalert.show("请正确填写");
            return;
        }
        var jsonData = {"cityCode":_city,"address":_address,"businessType":_businessType,"auth":auth,"storeNo":storeNo};
        $.post("./settlement",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                jalert.show(obj.msg);
            }
        });
    }
</script>