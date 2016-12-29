<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
        var _href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=http%3a%2f%2fwwt.bj37du.com%2fapi%2fgetUser&response_type=code&scope=snsapi_base&state=${storeNo}#wechat_redirect";
        window.location.href=_href;
    </script>
</head>
<body>
wx.html ${code}
</body>
</html>
