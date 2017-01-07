<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script type="text/javascript">
    </script>
</head>
<body>
${openId}
<form action="./payMoney">
    <input value="${openId}" id="openId" name="openId">
    <input value="${no}" id="storeNo" name="storeNo">
    <input value="${payMethod}" id="payMethod" name="payMethod">
    <input value="${buyerId}" id="buyerId" name="buyerId">
    金额：<input value="" id="orderPrice" name="orderPrice">
    <input type="submit" name="提交">
</form>
</body>
</html>
