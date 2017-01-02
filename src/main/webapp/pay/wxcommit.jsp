<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script type="text/javascript">
        function onBridgeReady(){
            WeixinJSBridge.invoke('getBrandWCPayRequest', {
                    "appId": "${hashMap.appId}",                //公众号API
                    "timeStamp": "${hashMap.timeStamp}",          //时间戳，自 1970 年以来的秒数
                    "nonceStr": "${hashMap.nonceStr}",          //随机串
                    "package": "${hashMap.package}",       //商品包信息
                    "signType": "${hashMap.signType}",          //微信签名方式
                    "paySign": "${hashMap.paySign}"             //微信签名
        },
        function(res){
            if(res.err_msg == "get_brand_wcpay_request：ok" ) {alert("od")}else{
                for ( var p in res ) { // 方法
                    alert(res[p]);
                }alert(props)
            } // 最后显示所有的属性 }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
        }
        );
        }
        if (typeof WeixinJSBridge == "undefined"){
            if( document.addEventListener ){
                document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
            }else if (document.attachEvent){
                document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
            }
        }else{
            onBridgeReady();
        }
    </script>
</head>
<body>
ff${hashMap}
</body>
</html>
