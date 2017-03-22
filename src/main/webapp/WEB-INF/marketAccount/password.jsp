<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>提现密码</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style type="text/css">
           .f1{
               background: #FFF;
           }
           .f1 .row:not(:last-child){
               border-bottom:1px solid #EEE;
           }
           .row .input_row{
               margin:0 1rem;
               padding:.75rem 0;
               display: flex;
               font-size: .85rem;
           }
           .input_row label{
               width:4rem;
               color: #666;
           }
           .input_row .input_text{
               flex:1;
               position: relative;
           }
           .input_text input{
               width:100%;
               height: 100%;
               position: absolute;
               left: 0;
               top: 0;
               font-size:.7rem;
           }
          .f2{
              margin-top: .5rem;
              text-align: center;
              background: #fc324a;
              color: #FFF;
          }
          .f2 .btn{
              padding:.6rem 0;
           }
    </style>
</head>
<body>
   <div class="container">
          <section class="f1">
              <div class="row">
                    <div class="input_row">
                        <label>提现密码</label>
                        <div class="input_text">
                            <input type="text" placeholder="请输入6位提现密码" autofocus>
                        </div>
                    </div>
              </div>
              <div class="row">
                  <div class="input_row">
                      <label>确认密码</label>
                      <div class="input_text">
                          <input type="text" placeholder="确认提现密码">
                      </div>
                  </div>
              </div>
          </section>

          <section class="f2" id="save">
              <div class="btn">
                  <span>保存</span>
              </div>
          </section>
   </div>
</body>
</html>
<script>
    $(function(){
        $("#save").on("click",function(){
            jalert.show('save');
        })
    })
</script>