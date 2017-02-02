<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>交易查询</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <style type="text/css">
        .f3_select {
            border:1px solid #fc324a;
            color:#fc324a;
        }
    </style>
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <link href="../resource/css/manager/paystyle.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
</head>
<body>
    <div class="container">
          <section class="f1">
                  <div class="pay-style">
                      <span class="common-mg"> 支付方式</span>
                  </div>
                  <div class="pay-select">
                      <div class="common-mg">
                          <ul class="clearfix">
                              <li>
                                  <label for="radion1">
                                         <img src="../resource/img/app/zhifubao.png">
                                  </label>
                                  <input type="radio" value="2" id="radion1" name="radion">
                              </li>
                              <li>
                                  <label for="radion2">
                                        <img src="../resource/img/app/weixinzhifu.png">
                                  </label>
                                  <input type="radio" value="1" id="radion2" name="radion">
                              </li>
                          </ul>
                      </div>
                  </div>
          </section>
          <section class="f2">
                   <div class="tran-date">
                        <span class="common-mg">交易日期</span>
                   </div>
                  <div class="tran-input">
                      <div class="common-mg">
                          <input type="date" id="startTime"><img src="../resource/img/app/shijianduanjiantou.png" class="jt"><input type="date" id="endTime">
                      </div>
                  </div>
          </section>
          <section class="f3">
                    <div class="tran-status">
                          <span class="common-mg">交易状态</span>
                    </div>
                    <div class="tran-status-select">
                        <div class="common-mg  pd">
                             <ul class="clearfix">
                                 <li>
                                     <span id="v1">收款成功</span>
                                 </li>
                                 <li>
                                     <span id="v2">收款失败</span>
                                 </li>
                                 <li>
                                     <span id="v3">有退款</span>
                                 </li>
                             </ul>
                        </div>
                    </div>
          </section>
          <section class="f4">
              <div class="common-mg store">
                <span>门店</span>
                <span>
                    <select id="_selectStoreNo">
                        <option value="-1">--选择门店--</option>
                        <c:forEach items="${storeList}" var="store">
                            <option value="${store.storeNo}">${store.name}</option>
                        </c:forEach>
                    </select>
                </span>
              </div>
          </section>
          <!--<section class="f5">
                    <div class="payee">
                        <span class="common-mg">收款对象</span>
                    </div>
                    <div class="manager-pop pd">
                        <div class="common-mg">
                            <ul class="clearfix">
                                <li>
                                    <span>店长</span>
                                </li>
                                <li>
                                    <span>店长</span>
                                </li>
                                <li>
                                    <span>店长</span>
                                </li>
                            </ul>
                        </div>
                    </div>
          </section>-->
    </div>
    <footer>
        <div class="footer">
            <ul class="clearfix">
                <li id="_reset">
                    <span>重置</span>
                </li>
                <li id="_submit">
                    <span>确定</span>
                </li>
            </ul>
        </div>

    </footer>
</body>
</html>
<script>
    var auth = "${auth}";
    $(function(){
        load();
    })

    function load(){
        $(".f3 .tran-status-select ul li span").on("touchend",function(){
            $(this).toggleClass("f3_select");
        });
        $("#_reset").on("touchend",function(){
            $(".f3 .tran-status-select ul li span").removeClass("f3_select");
            $("#startTime").val("");
            $("#endTime").val("");
            $('input:radio[name="radion"]:checked').prop("checked",false);

        });
        $("#_submit").on("touchend",function(){
            _submit();
        });

    }

    function _submit(){
        var chkRadio = $('input:radio[name="radion"]:checked').val();
        if (chkRadio == null) {
            chkRadio = 0
        }
        var startTime = $("#startTime").val();
        var endTime = $("#endTime").val();

        var status = "-1";
        var statusObj = $(".f3_select");
        if(statusObj && statusObj.length > 0){
            status="";
            for(var i=0;i<statusObj.length;i++){
                status +=$(statusObj[i]).attr("id").substr(1,1)+",";
            }
            if(status != ""){
                status = status.substr(0,status.length-1);
            }
        }
        var _selectStoreNo = $("#_selectStoreNo").val();
        var data = {"payType":chkRadio,"startTime":startTime,"endTime":endTime,"status":status,"storeNo":_selectStoreNo};
        var jStr = "{ ";
        for(var item in data){
            jStr += "'"+item+"':'"+data[item]+"',";
        }
        jStr += " }";
        location.href = "./toTransactionDetails?auth="+auth+"&param="+jStr;
//        $.("./toTransactionDetails",{"param":jStr,"auth":auth});
    }
</script>