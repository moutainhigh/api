<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
        var host =  window.location.host;
        var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&" +
                    "&redirect_uri=http%3a%2f%2f"+host+"%2fapi%2f${redirect}&" +
                    "response_type=code&scope=snsapi_base&state=${code}&component_appid=wx4f6a40c48f6da430#wechat_redirect";
        window.location.href=_href;
    </script>
</head>
<body>
</body>
</html>
