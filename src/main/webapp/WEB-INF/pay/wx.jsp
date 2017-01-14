<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
        var host =  window.location.host;
        var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&" +
                    "&redirect_uri=http%3a%2f%2f"+host+"%2fapi%2fpay%2fgetUserOpenId&" +
                    "response_type=code&scope=snsapi_base&state=${no}#wechat_redirect";
        window.location.href=_href;
    </script>
</head>
<body>
</body>
</html>
