<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script language="javascript">
        var host =  window.location.host;
        var _href = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=${appid}&scope=auth_base&redirect_uri="+
                "http%3a%2f%2f"+host+"%2fapi%2f${redirect}&state=${no}";
       window.location.href=_href;
    </script>
</head>
<body>
</body>
</html>