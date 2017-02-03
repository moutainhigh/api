<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>新增进件</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/admin/newInsert.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>

    <style>

    </style>
</head>
<body>
    <input value="${auth}" id="auth" name="auth" type="hidden">
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
                              <img src="../resource/img/jibenziliaozhi_huitai.png">
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
             <div class="p-group">
                 <p >
                     <span class="span-label">店名</span>
                     <span class="span-result"><input id="storeName" type="text" placeholder="店名"></span>
                 </p>
                 <p >
                     <span class="span-label">商户手机</span>
                     <span class="span-result"><input type="text" id="storePhone" placeholder="11位正确手机号"></span>
                 </p>
                 <p >
                     <span class="span-label">登录账号</span>
                     <span class="span-result"><input type="text" id="storeAccount" placeholder="建议使用手机号"></span>
                 </p>
                 <p >
                     <span class="span-label">编&ensp;&ensp;&ensp;&ensp;号</span>
                     <span class="span-result"><input type="text" id="storeNo" placeholder="输入编号"></span>
                 </p>
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
    $(function(){
        load();
    });

    function load(){
        $("#_submit").on("touchend",function(){
            _submit();
        });
    };

    function _submit(){
        var storeName = $.trim($("#storeName").val());
        var storePhone = $.trim($("#storePhone").val());
        var storeAccount = $.trim($("#storeAccount").val());
        var storeNo = $.trim($("#storeNo").val());

        if(storeName == "" || storePhone=="" || storeAccount == "" || storeNo==""){
            alert("不能有空值");
            return;
        }
        var realLength = 0, len = storeName.length, charCode = -1;
        for (var i = 0; i < len; i++) {
            charCode = storeName.charCodeAt(i);
            if (charCode >= 0 && charCode <= 128) realLength += 1;
            else realLength += 2;
        }
        if(realLength<8){
            alert("商家名称不能少于8个字符");
            return false;
        }

        var myreg = /^(1+\d{10})$/;
        if(!myreg.test(storePhone)) {
            alert('请输入有效的手机号码！');
            return false;
        }

        var jsonData = {"storeName":storeName,"storePhone":storePhone,"storeAccount":storeAccount,"storeNo":storeNo,"auth":auth};
        $.post("./realnameauth",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                alert(obj.msg);
            }
        });
    }
</script>