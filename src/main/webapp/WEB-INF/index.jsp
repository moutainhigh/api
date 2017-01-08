<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script type="text/javascript">
        function opendAcount(){
            var srcUrl = "https://www.mszxyh.com/wapserver/outer/index.html?Page=login&ChannelId=mszx02279";
            var sr = document.getElementById("iframe").setAttribute("src",srcUrl);
        }
        function login(){
            var srcUrl = "https://www.mszxyh.com/wapserver/outer/index.html?Page=relogin&ChannelId=mszx02279";
            var sr = document.getElementById("iframe").setAttribute("src",srcUrl);
        }
    </script>
</head>
    <input type="button" value="电子账户开户" onclick="opendAcount()"/>
    <input type="button" value="电子账户登录" onclick="login()"/>
<iframe id="iframe" style="display: block;margin-top: 20px" src="https://www.mszxyh.com/wapserver/outer/index.html?Page=login&ChannelId=mszx02279" height="600px"></iframe>
</body>
</html>
