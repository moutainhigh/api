<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
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
                <p >
                    <span class="span-label">开户名</span>
                    <span class="span-result"><input type="text" id="sa_name" placeholder=""></span>
                </p>
                <p >
                    <span class="span-label">银行卡号</span>
                    <span class="span-result">
                        <input type="text" id="sa_num" placeholder="已开通快捷支付的银行卡">
                    </span>
                </p>
                <p >
                    <span class="span-label">开户银行</span>
                    <span class="span-result" id="sa_bank_name">
                        中国民生银行
                    </span>
                </p>
                <p >
                    <span class="span-label">邮箱</span>
                    <span class="span-result">
                            <input type="text" id="mer_email" placeholder="电子邮箱">
                    </span>
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
    var storeNo = $("#storeNo").val();
    $(function(){
        load();
    });

    function load(){
        $("#_submit").on("touchend",function(){
            _submit();
        });
    };


    function _submit(){
        var sa_name = $.trim($("#sa_name").val());
        var sa_num = $.trim($("#sa_num").val());
        var sa_bank_name = $.trim($("#sa_bank_name").text());
        var mer_email = $.trim($("#mer_email").val());
        if(sa_name=="" || sa_num==""||sa_bank_name==""||mer_email==""){
            alert("请正确填写");
            return;
        }
        re = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/
        if(!re.test(mer_email)){
            alert("邮箱填写错误");
            return;
        }
        var jsonData = {"saName":sa_name,"saNum":sa_num,"saBankName":sa_bank_name,"merEmail":mer_email,"auth":auth,"storeNo":storeNo};
        $.post("./auditStatus",jsonData,function(obj){
            if(obj.code == 0){
                location.href = obj.data.url+"?auth="+obj.data.auth+"&storeNo="+obj.data.storeNo;
            }else{
                alert(obj.msg);
            }
        });
    }
</script>