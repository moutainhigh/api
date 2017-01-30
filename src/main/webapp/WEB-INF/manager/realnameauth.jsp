<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>实名认证</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/realnameauth.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
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
                   <div class="row-label clearfix">
                       <label>费率</label>
                       <span class="sh-right" id="_rate">0.38</span>
                   </div>
              </div>
             <div class="common-br">
                 <div class="row-label clearfix">
                     <label>结算时间</label>
                     <span class="sh-right">
                          <select id="_selectRate" onchange="_selectRate()">
                             <option value="T1">T1</option>
                             <option value="D0">D0</option>
                          </select>
                     </span>
                 </div>
             </div>
             <div class="common-br">
                 <div class="row-label clearfix">
                     <label>
                         商户地址
                         <span>
                             <select id="_province">
                                 <option value="0">请选择省份</option>
                             </select>
                         </span>
                     </label>
                     <span class="sh-right">
                         <select id="_city">
                             <option value="0">请选择城市</option>
                         </select>
                     </span>
                 </div>
                 <div class="row-label clearfix" style="padding-top:0;">
                     <label>
                         &ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;&ensp;
                         <span style="color:#999;">
                             详细地址
                         </span>
                     </label>
                     <span class="sh-right">
                         <input id="_address" type="text" style="border:1px solid #CCC;width:100%">
                     </span>
                 </div>
             </div>
             <div class="common-br">
                 <div class="row-label clearfix">
                     <label>行业类型</label>
                     <span class="sh-right">
                         <select>
                             <option>商户类型</option>
                             <option>个体工商户</option>
                         </select>
                         <select>
                             <option>行业大类</option>
                             <option>餐饮/食品</option>
                             <option>线下零售</option>
                             <option>其它</option>
                         </select>
                         <select id="_businessType">
                             <option>类目</option>
                             <option value="292">食品,292</option>
                             <option value="153">餐饮,153</option>
                             <option value="209">便利店,209</option>
                             <option value="210">其他综合零售,210</option>
                             <option value="158">其他行业,158</option>
                         </select>
                     </span>
                 </div>
             </div>

         </section>
         <section class="f3">
             <div class="common-br">
                 <div class="info">
                       <span class="name">
                          营业执照
                       </span>
                 </div>
             </div>
             <div class="p-group">
                 <p >
                     <span class="span-label">法人姓名</span>
                     <span class="span-result"><input type="text" id="_name" placeholder="请输入法人姓名"></span>
                 </p>
                 <p >
                     <span class="span-label">身份证号</span>
                     <span class="span-result"><input type="text" id="_idCard" placeholder="请输入法人身份证号"></span>
                 </p>
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

    function _selectRate(){
        var _selectRate = $("#_selectRate").val();
        if(_selectRate == "D0"){
            $("#_rate").text(0.39);
        }
        if(_selectRate == "T1"){
            $("#_rate").text(0.40);
        }
    }

    function _selectCity(_id,cityCode){
        $.post("../getCityCode",{"cityCode":cityCode},function(data){
            if(data.code == 0){
                var options = "";
                $.each(data.data,function(n,obj) {
                    options += "<option value=\""+obj.code+"\">"+obj.name+"</option>";
                });
                $("#"+_id).append(options);

            }else{
                    alert("出错了");
                }
            })
    }

    function _submit(){
        var _rate = $.trim($("#_rate").text());
        var _selectRate = $.trim($("#_selectRate").val());
        var _city = $.trim($("#_city").val());
        var _address = $.trim($("#_address").val());
        var _businessType = $.trim($("#_businessType").val());
        var _name = $.trim($("#_name").val());
        var _idCard = $.trim($("#_idCard").val());
        if(_rate=="" || _selectRate==""||_city==""||_address==""||_businessType==""||_name==""||_idCard==""){
            alert("请正确填写");
            return;
        }
        var jsonData = {"rate":_rate,"settlementType":_selectRate,"cityCode":_city,"address":_address,"businessType":_businessType,"name":_name,"idCard":_idCard,"auth":auth,"storeNo":storeNo};
        $.post("./settlement",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                alert(obj.msg);
            }
        });
    }
</script>