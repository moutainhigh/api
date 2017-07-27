package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.*;
import com.zhsj.api.util.minsheng.CertUtil;
import com.zhsj.api.util.minsheng.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class MinshengService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MinshengService.class);

	@Autowired
	private TbStorePayInfoDao tbStorePayInfoDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private TbStorePayInfoDao bmStorePayInfoDao;

    /**
	 * 获取参数签名
	 * @param paramMap  签名参数
	 * @param paySecret 签名密钥
	 * @return
	 */
	public static String  getSign (Map<String , Object> paramMap , String paySecret){
		SortedMap<String, Object> smap = new TreeMap<String, Object>(paramMap);
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, Object> m : smap.entrySet()) {
			Object value = m.getValue();
			if (value != null && StringUtils.isNotBlank(String.valueOf(value))){
				stringBuffer.append(m.getKey()).append("=").append(m.getValue()).append("&");
			}
		}
		stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());

		String argPreSign = stringBuffer.append("&paySecret=").append(paySecret).toString();
		String signStr = MD5Util.encode(argPreSign).toUpperCase();

		return signStr;
	}

	//民生开户
	public String openAccount(MSStoreBean msStoreBean){
		String result = "SUCCESS";
		try{
			Map<String,String> resultMap = this.mersettled(msStoreBean);
			if(!"SUCCESS".equals(resultMap.get("result_code"))){
				return resultMap.get("error_msg");
			}
			String paykey = resultMap.get("paykey");
			String paysec = resultMap.get("paysec");
			String ali_rate = resultMap.get("ali_rate");
			String wx_rate = resultMap.get("wx_rate");
			String settlementType =resultMap.get("settlement_type");

			JSONObject json = new JSONObject();
			json.put("aliRate",ali_rate);
			json.put("wxRate",wx_rate);
			json.put("settlementType",settlementType);
			List<String> payMethods = new ArrayList<>();
			payMethods.add("1");
			payMethods.add("2");
			tbStorePayInfoDao.insertPayInfo(msStoreBean.getStoreNo(),2,payMethods,paykey,paysec,ali_rate,settlementType,json.toJSONString(),1);
		}catch (Exception e){
			logger.error("#MinshengService.openAccount# e={}",e.getMessage(),e);
			result = "系统出错";
		}
		return result;
	}

	public Map<String, String> mersettled(MSStoreBean msStoreBean) {
		Map<String, String> rspMap = new HashMap<>();
		try{
			String url = MtConfig.getProperty("REQ_URL","")+"/merSettled.do";
			Map<String, String> reqData = new HashMap<String, String>();
			reqData.put("reg_contact_tel", msStoreBean.getReg_contact_tel()); //商户手机号
			reqData.put("legal_person", msStoreBean.getLegal_person());//法定代表人姓名
			reqData.put("legal_person_id", msStoreBean.getLegal_person_id()); //法定代表人身份证号
			reqData.put("mer_email", msStoreBean.getMer_email());//商户联系邮箱
			reqData.put("filed1", msStoreBean.getFiled1()); //入驻商户的客服电话
			reqData.put("agent_no", "95272016121410000062");//此字符串由民生提供
			reqData.put("wx_business_type", msStoreBean.getWx_business_type()); //商户营业类别
			reqData.put("ali_business_type", msStoreBean.getAli_business_type()); //支付宝口碑类目
			reqData.put("mer_name", msStoreBean.getMer_name()); //商户全称
			reqData.put("wx_rate", msStoreBean.getWx_rate());   //微信费率     示例:0.5，代表和该入驻商户签约千分之5的费率
			reqData.put("ali_rate", msStoreBean.getAli_rate());   //支付宝费率     示例:0.5，代表和该入驻商户签约千分之5的费率
			reqData.put("sa_name", msStoreBean.getSa_name());   //结算账户名称       G
			reqData.put("sa_num", msStoreBean.getSa_num());   //结算账户账号
			reqData.put("sa_bank_name", msStoreBean.getSa_bank_name());   //结算账户银行
			reqData.put("sa_bank_type", msStoreBean.getSa_bank_type());   //结算账户类型(对公=01)(对私=00)
			reqData.put("settlement_type", msStoreBean.getSettlement_type());   //商户结算类型D0:实时清算,T1:隔天清算 若不传，默认D0清算
//			reqData.put("user_pid", msStoreBean.getUser_pid());//支付宝ISV的pid
			reqData.put("mer_short_name", msStoreBean.getMer_short_name());//商户简称

			String CERT_PATH_P12 = MtConfig.getProperty("CERT_PATH_P12","");
			String CERT_JKS_P12_PASSWORD = MtConfig.getProperty("CERT_JKS_P12_PASSWORD", "");
			String signString = CertUtil.reqSign(MapUtil.coverMap2String(reqData), CERT_PATH_P12, CERT_JKS_P12_PASSWORD);
			reqData.put("sign", new String(signString)); // 签名后的字符串
			String stringData = MapUtil.getRequestParamString(reqData);

			String reqBase64 = new String(CertUtil.base64Encode(stringData.getBytes("UTF-8")));
			String rspBase64 = SSLUtil.httpsPost(url, reqBase64);
			String rspData = new String(CertUtil.base64Decode(rspBase64.getBytes("UTF-8")));
			rspMap = MapUtil.convertResultStringToMap(rspData);
		} catch (Exception e) {
			logger.error("#MinshengService.mersettled# e={}", e.getMessage(), e);
		}
		return rspMap;
	}

	
	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#MinshengService.refundMoney# orderBean={},price{},userId={}",orderBean,price,userId);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("paykey", storePayInfo.getField1());// 01b0e7c141634a1e8ed4c0bd3c51bb58
			if ("1".equals(orderBean.getPayMethod())){
				parameters.put("sub_app_id", storePayInfo.getAppId()); // 商家的微信公众号，必须和入驻二级商户号对应
				parameters.put("pay_way_code", "WXF2F"); // 订单的支付方式 WXF2F,ALIF2F
			}else{
				parameters.put("sub_app_id", "");
				parameters.put("pay_way_code", "ALIF2F"); // 订单的支付方式 WXF2F,ALIF2F
			}
				
			parameters.put("order_no", orderBean.getOrderId()); // 请自己试试自己发起的订单
			parameters.put("device_info", ""); // 目前保留，后期会用
			// 入驻商户号+yyyyMMddHHmmss 商户发起的退款订单号
			parameters.put("out_refund_no", "re"+orderBean.getOrderId());
			parameters.put("order_price", String.valueOf(orderBean.getActualChargeAmount())); // 订单总金额 单位：圆
			parameters.put("refund_fee", String.valueOf(price)); // 订单总金额
			parameters.put("op_user_id", String.valueOf(userId)); // 操作员id,目前保留，后期会用
			
			///// 签名///
			String sign = getSign(parameters, storePayInfo.getField2());// 此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
			parameters.put("sign", sign);
			
			String postUrl = MtConfig.getProperty("ms_URL","")+"/qthd-pay-web-gateway/scanPay/Refund";
			String resultString = HttpClient.sendPost(postUrl, parameters);
			logger.info("#MinshengService.refundMoney# orderBean={},result={}",orderBean,resultString);
			Map<String,String> map = JSON.parseObject(resultString, Map.class);
			if(!"SUCCESS".equals(map.get("result_code"))){
				result = map.get("err_code_des");
			}else {
				result = "SUCCESS";
			}
		}catch (Exception e) {
			result = "系统错误";
			logger.error("#MinshengService.refundMoney# orderBean={}",orderBean,e);
		}
		return result;
	}
	
	public String searchRefund(OrderBean orderBean){
		logger.info("#MinshengService.searchRefund# orderBean={}",orderBean);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("paykey", storePayInfo.getField1());// 此字符串由民生提供，作为商户的唯一标识
			parameters.put("out_refund_no", "re"+orderBean.getOrderId()); // 请自己试试自己发起的订单
			parameters.put("device_info", ""); // 目前保留，后期会用
			if ("1".equals(orderBean.getPayMethod())){
				parameters.put("pay_way_code", "WXF2F"); // 订单的支付方式 WXF2F,ALIF2F
			}else{
				parameters.put("pay_way_code", "ALIF2F"); // 订单的支付方式 WXF2F,ALIF2F
			}
			///// 签名///
			String sign = getSign(parameters, storePayInfo.getField2());//
			parameters.put("sign", sign);
			String postUrl = MtConfig.getProperty("ms_URL","")+"/qthd-pay-web-gateway/scanPay/QueryRefund";
			String resultString = HttpClient.sendPost(postUrl, parameters);
			logger.info("#MinshengService.searchRefund# orderBean={},result={}",orderBean,resultString);
			Map<String,String> map = JSON.parseObject(resultString, Map.class);
			if(!"SUCCESS".equals(map.get("result_code"))){
				result = map.get("err_code_des");
			}else {
				result = "SUCCESS";
			}
		}catch (Exception e) {
			result="系统异常";
			logger.error("#MinshengService.searchRefund# orderBean={}",orderBean,e);
		}
		return result;
	}

	//民生开户
	public String openAccountV2(MchInfoAddBean info){
		String result = "SUCCESS";
		try{
			Map<String,String> resultMap = this.mersettledV2(info);
			if(!"SUCCESS".equals(resultMap.get("result_code"))){
				return resultMap.get("error_msg");
			}else if(StringUtils.isEmpty(resultMap.get("wx_sub_mch_id")) || 
					StringUtils.isEmpty(resultMap.get("ali_sub_mch_id"))){
				return resultMap.get("sub_msg");
			}
			String paykey = resultMap.get("paykey");
			String paysec = resultMap.get("paysec");
			String ali_rate = resultMap.get("ali_rate");
			String wx_rate = resultMap.get("wx_rate");
			String settlementType =resultMap.get("settlement_type");

			JSONObject json = new JSONObject();
			json.put("aliRate",ali_rate);
			json.put("wxRate",wx_rate);
			json.put("settlementType",settlementType);
			List<String> payMethods = new ArrayList<>();
			payMethods.add("1");
			payMethods.add("2");
			bmStorePayInfoDao.insertPayInfo(info.getStoreNo(),2,payMethods,paykey,paysec,ali_rate,settlementType,json.toJSONString(),1);
		}catch (Exception e){
			logger.error("#MinshengService.openAccount# e={}",e.getMessage(),e);
			result = "系统出错";
		}
		return result;
	}
	public Map<String, String> mersettledV2(MchInfoAddBean info) {
		Map<String, String> rspMap = new HashMap<>();
		try{
			String url = MtConfig.getProperty("REQ_URL","")+"/newMerSettled.do";
			Map<String, String> reqData = new HashMap<String, String>();
//			商户基础信息
			reqData.put("merchant_name", info.getStoreName());
			reqData.put("merchant_short_name", info.getShortName());
			reqData.put("agentNo", "95272016121410000062");
			reqData.put("remark", "");
			reqData.put("addType", "3");
			reqData.put("wx_rate", info.getWxRate());
			reqData.put("ali_rate", info.getAliRate());
			reqData.put("service_phone", info.getPhone());
			reqData.put("wx_business_type", String.valueOf(info.getBusinessType()));
			reqData.put("ali_business_type","2015050700000000");
			reqData.put("ali_pid", "");
			
//			商户结算信息
			reqData.put("sa_name", info.getAccountName());
			reqData.put("sa_num", info.getBankAccount());
			reqData.put("sa_name_id_card_no", info.getAccountIdCard());
			reqData.put("sa_phone", info.getAccountPhone());
			
			if("中国民生银行".equals(info.getBankName())){
				reqData.put("sa_bank_name_type", "0");
			}else {
				reqData.put("sa_bank_name_type", "1");
			}
			reqData.put("sa_bank_name", info.getBankName());
			reqData.put("sa_bank_type","00");
			reqData.put("settlement_type", "T1");
			
//			商户联系人信息
			reqData.put("contact_type","OTHER");
			reqData.put("contact_name", info.getContactsPeople());
			reqData.put("contact_phone", "");
			reqData.put("contact_mobile", "");
			reqData.put("contact_email", "");
			reqData.put("contact_id_card_no",info.getIdCard());
			
//			商户地址信息
			reqData.put("province_code",info.getProvince());
			reqData.put("city_code", info.getCity());
			reqData.put("district_code",info.getCounty());
			reqData.put("address",info.getAddress());
			reqData.put("longitude", "");
			reqData.put("latitude", "");
			reqData.put("address_type", "");
			
			String CERT_PATH_P12 = MtConfig.getProperty("CERT_PATH_P12","");
			String CERT_JKS_P12_PASSWORD = MtConfig.getProperty("CERT_JKS_P12_PASSWORD", "");
			String signString = CertUtil.reqSign(MapUtil.coverMap2String(reqData), CERT_PATH_P12, CERT_JKS_P12_PASSWORD);
			reqData.put("sign", new String(signString)); // 签名后的字符串
			String stringData = MapUtil.getRequestParamString(reqData);

			String reqBase64 = new String(CertUtil.base64Encode(stringData.getBytes("UTF-8")));
			String rspBase64 = SSLUtil.httpsPost(url, reqBase64);
			String rspData = new String(CertUtil.base64Decode(rspBase64.getBytes("UTF-8")));
			rspMap = MapUtil.convertResultStringToMap(rspData);
		} catch (Exception e) {
			logger.error("#MinshengService.mersettledV2# info={},", info, e);
		}
		return rspMap;
	}


	public static void main(String[] args) {
//		new MinshengService().updateMerchantByPaykey("85a6c4e20bf54505bea8e75bc870d587","0.4","0.4","D0");
//		JSONObject json = new JSONObject();
//		json.put("aliRate",Double.parseDouble("9.0"));
//		json.put("wxRate",Double.parseDouble("7.8"));
//		json.put("settlementType","e");
//
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("paykey","ffe91102af5a429e8bd2aba258e266a9");
//		parameters.put("order_no","1001170322710655045");  //请自己试试自己发起的订单
//		/////签名///
//		String sign = getSign(parameters, "bfc9a24ccd34474686230d825c03f00b");//
//		parameters.put("sign",sign);
//		String postUrl = MtConfig.getProperty("ms_URL","http://115.159.235.109:8208")+"/qthd-pay-web-gateway/scanPay/QueryReturnJson";
//		String str = HttpClient.sendPost(postUrl, parameters);
//		System.out.print(str);
		
		OrderBean orderBean = new OrderBean();
		orderBean.setActualChargeAmount(1);
		orderBean.setOrderId("106745901705025244617621");
		orderBean.setPayMethod("1");
//		new MinshengService().refundMoney(orderBean);
		new MinshengService().searchRefund(orderBean);
	}

}
