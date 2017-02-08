<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<html>
<head>
    <meta charset="UTF-8">
    <title>立减功能设置</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <style>
        .container{
            margin-bottom:44px;
        }
        .f1,.f2{
            background-color: #FFF;
        }
        .f1 .wraper:not(:last-child),
        .f2 .wraper:not(:last-child){
            border-bottom: 1px solid #EEE;
        }
        .all-store{
            background: url(../resource/img/app/gengduo.png) no-repeat right center/12%;
        }
        .all-store p {
            margin-right:20px;
            color:#333;
        }
        .inside{
            margin:0 20px;
        }
        .inside span{
            padding:15px 0;
            display: block;
            color:#666;
            font-size:.9em;
        }
        .f1 .inside span:first-child{
            float: left;
        }
        .f1 .inside span:last-child{
            float: right;
        }
        .f1 .wraper:last-child .inside span:last-child{
            font-size: .7em;
            color:#111;
        }
        .f1 .wraper:last-child .inside span:last-child p{
            line-height:2;
        }
        .f2,.f3{
            margin-top:10px;
        }
        .f2 .wraper p{
            font-size:.75em;
            color:#333;
            margin:0 20px;
        }
        .f2 .wraper p:last-child{
            padding:0 0 15px;
        }
        .f2 .wraper p input{
            width:50px;
            line-height: 1.4;
        }
        .f2 .wraper p input[type=checkbox]{
            -webkit-appearance:none;
            position: relative;
            width:20px;
            height:20px;
            display: block;
            border-radius: 0;
        }
        .f2 .wraper p input[type=text]{
            -webkit-appearance:none;
            border-radius: 0;
            border:1px solid #CCC;
        }
        .fm{
            margin-left:22px;
        }
        .f3{
            margin:10px 25px;
            font-size:.9em;
        }
        .f3,.f4 {
            background-color: #fc324a;
            text-align: center;
            color:#FFF;
        }
        .addRule,.confirm{
            padding:10px 0;
            /*font-weight: 100;*/
        }

        .addRule em{
            height:20px;
            width:20px;
            position:relative;
        }

        .addRule em:before, .addRule em:after{
            content:'';
            height:2px;
            width:15px;
            display:block;
            background:#FFF;
            position:absolute;
            top:11px;
            left:-67px;
        }

        .addRule em:after{
            height:15px;
            width:2px;
            top:4px;
            left:-60px;
        }
        .sel{
            content:'';
            width:20px;
            height:20px;
            background-color: #fc324a;
            display: block;
            position: absolute;
            top:21px;
            left:0;
        }
        .no{
            content:'';
            width:20px;
            height:20px;
            border:1px solid #fc324a;
            background-color: #FFF;
            display: block;
            position: absolute;
            top:20px;
            left:0;
        }
        .default{
            content:'';
            width:20px;
            height:20px;
            border:1px solid #CCC;
            background-color: #FFF;
            display: block;
            position: absolute;
            top:20px;
            left:0;
        }
        .delete{
            content: '';
            width:20px !important;
            height:20px !important;
            background: url(../resource/img/app/chexiao.png) no-repeat center;
            background-size:100% auto;
            display: block;
            position: absolute;
            top: 20px;
            left: 0;
        }
    </style>

</head>
<body>
    <div class="container">
          <section class="f1">
              <div class="wraper">
                  <div class="inside clearfix">
                      <span>活动名称</span>
                        <span >
                            <p>dd</p>
                        </span>
                  </div>
              </div>
                 <div class="wraper">
                    <div class="inside clearfix">
                        <span>适用门店</span>
                        <span class="all-store">
                            <p>全部门店</p>
                        </span>
                    </div>
                 </div>
                 <div class="wraper">
                      <div class="inside clearfix">
                          <span>活动时间</span>
                          <span>
                              <p>2016-12-20 12:00 - 2016-12-30 12:23</p>
                          </span>
                      </div>
                 </div>
          </section>


          <section class="f2">
                  <div class="wraper">
                        <div class="inside">
                             <span>优惠规则:</span>
                        </div>
                  </div>
                  <div class="wraper">
                        <p class="lj">
                            <input type="checkbox" class="radio no">
                            <span class="fm">满 XXX</span>
                            <span>元，立减 XXX</span>
                            <span>元</span>
                        </p>
                        <p class="sj">
                              <input type="checkbox" class="radio no">
                              <span class="fm">满 XXX </span>
                              <span> 元，随机减 XXX 至 XXX</span>
                              <span>元</span>
                        </p>
                        <p class="zk">
                              <input type="checkbox" class="radio no">
                              <span class="fm">满 XXX</span>
                              <span>元，</span>
                              <span>XX 折</span>
                        </p>
                  </div>
          </section>

          <section class="f3">
                <div class="addRule" id="add">
                    <em></em>
                    <span>添加新规则</span>
                </div>
          </section>
          <section class="f2">
              <div class="wraper" id="newRules">
                          <!-- 新加规则
                  <p>
                      <input type="checkbox" class="delete">
                      <span class="fm">满</span>
                      <input type="text">
                      <span>元，</span>
                      <input type="text">
                      <span>折</span>
                  </p>
                  -->
              </div>
          </section>


    </div>
    <footer>
        <section class="f4" id="confirm">
            <div class="confirm">确认</div>
        </section>
    </footer>
</body>
</html>
<script>
    var auth="${auth}";
  $(function(){
      $(".all-store").on("click",function(){
          location.href="./selectStore?auth="+auth;
      });
      //点击选中
      $(".radio").on("click",function(){
          if($(this).attr("class").indexOf("sel") != -1){//如果选中。需要再次点击 。(用于重新选择)
              $(".radio").removeClass("default").removeClass("sel").addClass("no");
              $("#newRules").empty();
          }else if($(this).attr("class").indexOf("no") != -1){//开始选择。让选中的红色 。其他的至灰
              $(".radio").removeClass("sel").removeClass("no").addClass("default");
              $(this).removeClass("default").removeClass("no").addClass("sel");
          }
      })

      //添加规则
      $("#add").on("click",function(){
            var selrule = $(".sel").parent();
            var htm = selrule.html();
            if(htm == undefined){
                alert('请选择一个优惠规则');
                return false;
            }else{
                var checkbox = $("<input>").attr("type","checkbox").attr("class","delete")
                checkbox.on("click",function(){
                    $(this).parent().remove();
                });
                var s = "";
                if(selrule.attr('class') == 'lj') {
                    s = ' <span class="fm">满</span> '
                        + '<input type="text"> '
                        + '<span>元，立减</span> '
                        + '<input type="text"> '
                        + '<span>元</span>';

                }else if(selrule.attr('class') == 'sj'){
                     s = ' <span class="fm">满</span> '
                        + '<input type="text"> '
                        + '<span>元，随机减</span> '
                        + '<input type="text"> '
                        + '<span>至</span> '
                        + '<input type="text"> '
                        + '<span>元</span>';
                }else if(selrule.attr('class') == 'zk'){
                       s = ' <span class="fm">满</span> '
                           + '<input type="text"> '
                           + '<span>元，</span> '
                           + '<input type="text"> '
                           + '<span>折</span>';
                }
                $("#newRules").append($("<p>").append(checkbox).append(s));
            }
      })

      $("#confirm").on("click",function(){
             alert('确定');
      })
  })
</script>