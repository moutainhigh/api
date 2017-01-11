<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <script type="text/javascript">
    </script>
</head>
<body>
<form action="./payMoney">
    <input value="${openId}" id="openId" name="openId"><br>

    <input value="${no}" id="storeNo" name="storeNo">
    <input value="${payMethod}" id="payMethod" name="payMethod">
    <input value="${buyerId}" id="buyerId" name="buyerId">
    金额：<input value="" id="orderPrice" name="orderPrice">
    <input type="submit" name="提交">
</form>
</body>
</html>
private String storeNo;
private String reg_contact_tel; //商户手机号
private String legal_person;//法定代表人姓名
private String legal_person_id; //法定代表人身份证号
private String mer_email ; //商户联系邮箱
private String filed1; //入驻商户的客服电话
private String wx_business_type;//商户营业类别
private String ali_business_type; //支付宝口碑类目
private String mer_name ; //商户全称
private String wx_rate;  //微信费率     示例:0.5，代表和该入驻商户签约千分之5的费率
private String ali_rate;   //支付宝费率     示例:0.5，代表和该入驻商户签约千分之5的费率
private String sa_name;   //结算账户名称       G
private String sa_num;   //结算账户账号
private String sa_bank_name;  //结算账户银行
private String sa_bank_type;   //结算账户类型(对公=01)(对私=00)
private String settlement_type;   //商户结算类型D0:实时清算,T1:隔天清算 若不传，默认D0清算
private String mer_short_name;//商户简称