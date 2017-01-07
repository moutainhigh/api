<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script src="https://as.alipayobjects.com/g/component/antbridge/1.1.1/antbridge.min.js"></script>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript">

        document.addEventListener('AlipayJSBridgeReady', function () {
            AlipayJSBridge.call("tradePay", {tradeNO:"${data}"}, function (result) {
//                $.post("payNotifyAli",result);
//                alert(JSON.stringify(result));
            });}, false);

    </script>
</head>
<body>
ff${data}
</body>
</html>
