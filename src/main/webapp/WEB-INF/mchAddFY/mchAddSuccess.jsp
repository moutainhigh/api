<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>审核状态</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/base.css"/>
    <link rel="stylesheet" href="../resource/css/mchAdd/success.css"/>
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/320+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document)
    </script>

</head>
<body>
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
                    <p class="font-gray">基本资料</p>
                </li>
                <li class="fl create-right">
                    <i class="create-box3"><img src="../resource/img/mchAdd/jiesuanxinxi-1.png" class="tit-icon2"/></i><span class="tit-arrow-pos create-box2" id="tit-arrow3-3"><img src="../resource/img/mchAdd/zhihujiantou.png" class="tit-arrow"/></span>
                    <p class="font-gray">结算信息</p>
                </li>
                <li class="fl">
                    <i class="create-box4"><img src="../resource/img/mchAdd/shenhetongguo-1.png" class="tit-icon2"/></i>
                    <p class="font-gray">审核状态</p>
                </li>
            </ul>
        </div>
    </div>
    <div class="footer">
        <p class="footer-tit"><img src="../resource/img/mchAdd/shenhechenggong.png" class="footer-img"/>
            <span class="footer-font">审核成功</span>
            <h3 class="footer-pass">恭喜，您已进件成功</h3>
        </p>
    </div>

</div>

</body>
</html>
<script type='text/javascript'>
    document.querySelector('body').addEventListener('touchstart', function (ev) {
        event.preventDefault();
    });
</script>