package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.bean.result.RateBean;
import com.zhsj.api.constants.DiscountATypeCons;
import com.zhsj.api.constants.OrderStatus;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.*;
import com.zhsj.api.util.minsheng.CertUtil;
import com.zhsj.api.util.minsheng.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhsj.api.task.async.OrderSuccessAsync;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class MinshengService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MinshengService.class);

	@Autowired
	private TbStoreDao bmStoreDao;
	@Autowired
	private TbStoreBindOrgDao bmStoreBindOrgDao;
	@Autowired
	private TbOrderDao bmOrderDao;
	@Autowired
	private TbUserDao bmUserDao;
	@Autowired
	private TbStorePayInfoDao bmStorePayInfoDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AyncTaskUtil ayncTaskUtil;
	@Autowired
	private TBStoreBindDiscountDao tbStoreBindDiscountDao;
	@Autowired
	private TBDiscountDao tbDiscountDao;
	@Autowired
	private TBDiscountRuleDao tbDiscountRuleDao;

    /*此二字段由民生提供给各个商户*/
//	static String  payKey = "85a6c4e20bf54505bea8e75bc870d587";//此字符串由民生提供，作为商户的唯一标识
//	static String  paySec = "60f811a8b472495fa6c656c507f44cdc";//此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端

    public Map payWeChat(PayBean payBean,String hostName){
		Map map = new HashMap();
		try{
			String storeNo = payBean.getStoreNo();
			if(StringUtils.isEmpty(storeNo)){
				map.put("return_code","FAIL");
				return map;
			}
			//获定单号
			String orderNo = StoreUtils.getOrderNO(storeNo);
			payBean = this.calDiscount(payBean);
			StorePayInfo storePayInfo = prePay(payBean,orderNo);
			double orderPrice = Arith.sub(payBean.getOrderPrice(), payBean.getDiscountPrice());
			if(orderPrice <= 0){
				orderPrice = 0.01;
			}
			//调用接口
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("order_price", String.valueOf(orderPrice)); //支付费用单位：元，支付一分钱
			parameters.put("pay_way_code", "WXF2F");//当面付标识
			parameters.put("order_no", orderNo); ////入驻商户号+yyyyMMddHHmmss
			parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			parameters.put("paykey", storePayInfo.getField1());//此字符串由民生提供，作为商户的唯一标识
			parameters.put("product_name", storePayInfo.getStoreName()+"-支付"); //产品名称，商户根据自己的需求填写
			parameters.put("order_ip", hostName); //发起交易的服务器地址
			parameters.put("remark", storePayInfo.getStoreName()+"-支付"); //附件内容
			parameters.put("order_period", "5");   //必填
			parameters.put("notify_url", "http://wwt.bj37du.com/api/pay/payNotifyWeChat");//必填，请填写通知地址
			parameters.put("field1", payBean.getOpenId()); //可不填
			parameters.put("field2", ""); //可不填，建议填写代理商名称
			parameters.put("field3", "");
			parameters.put("field4", "");
			parameters.put("field5", "");
			String sign = getSign(parameters, storePayInfo.getField2());////paySec 此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
			parameters.put("sign",sign);
			String postUrl = MtConfig.getProperty("ms_URL", "http://115.159.235.109:8208")+"/qthd-pay-web-gateway/scanPay/wxPrePay";
			String result = HttpClient.sendPost(postUrl, parameters);
			map = JSON.parseObject(result, Map.class);
			map.put("orderId",orderNo);
			logger.info("#MinshengService.payWeChat# result={}",result);
		}catch (Exception e){
			logger.error("#MinshengService.payWeChat e={}#",e.getMessage(),e);
			map.put("return_code","FAIL");
		}
		return map;
	}

	public Map<String,String> payAli(PayBean payBean,String hostName){
		Map map = new HashMap();
		try{
			String storeNo = payBean.getStoreNo();
			if(StringUtils.isEmpty(storeNo)){
				map.put("return_code","FAIL");
				return map;
			}
			//获定单号
			String orderNo = StoreUtils.getOrderNO(storeNo);
			payBean = this.calDiscount(payBean);
			StorePayInfo storePayInfo = prePay(payBean, orderNo);
			//调用接口
			double orderPrice = Arith.sub(payBean.getOrderPrice(), payBean.getDiscountPrice());
			if(orderPrice <= 0){
				orderPrice = 0.01;
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("buyer_id", payBean.getBuyerId()); // 支付费用单位：元，支付一分钱
			parameters.put("order_price", orderPrice); // 支付费用单位：元，支付一分钱
			parameters.put("paykey", storePayInfo.getField1());// 此字符串由民生提供，作为商户的唯一标识
			parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			parameters.put("store_id", storeNo); // 附件内容
			parameters.put("operator_id", "oper001"); //
			parameters.put("order_ip", hostName); // 发起交易的服务器地址

			parameters.put("order_no", orderNo); //// 入驻商户号+yyyyMMddHHmmss
			parameters.put("product_name", storePayInfo.getStoreName()+"-支付"); // 产品名称，商户根据自己的需求填写
			parameters.put("remark", storePayInfo.getStoreName()+"-支付"); // 附件内容

			parameters.put("order_period", "5"); // 必填 ，分钟

			String sign = getSign(parameters, storePayInfo.getField2());//// paySec
			//// 此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
			parameters.put("sign", sign);
			String postUrl = MtConfig.getProperty("ms_URL","http://115.159.235.109:8208") + "/qthd-pay-web-gateway/scanPay/createAliFixPayEx";
			String result = HttpClient.sendPost(postUrl, parameters);
			map = JSON.parseObject(result, Map.class);
			map.put("orderId",orderNo);
			logger.info("#MinshengService.payAli# result map={}", map);

		}catch (Exception e){
			logger.error("#MinshengService.payAli e={}#",e.getMessage(),e);
			map.put("return_code","FAIL");
		}
		return map;
	}

	private PayBean calDiscount(PayBean payBean){
		List<Long> discountIdList = tbStoreBindDiscountDao.getDiscountIdByStoreNo(payBean.getStoreNo());
		if(discountIdList == null || discountIdList.isEmpty()){
			return payBean;
		}
		List<DiscountBean> disList = tbDiscountDao.getByIds(discountIdList);
		if(disList == null || disList.isEmpty()){
			return payBean;
		}

		Map<Integer,DiscountBean> discountBeanMap = new HashMap<>();
		for(DiscountBean bean :disList){
			discountBeanMap.put(bean.getaType(),bean);
		}
		//是否有优惠1
		DiscountBean beant1 = discountBeanMap.get(DiscountATypeCons.unique.getType());
		//是否有优惠3
		DiscountBean beant3 = discountBeanMap.get(DiscountATypeCons.blend.getType());
		//是滞有优惠2
		DiscountBean beant2 = discountBeanMap.get(DiscountATypeCons.STORE.getType());
		if(beant1 != null){
			payBean = this.calDiscountProc(payBean,beant1,null);
		}else if(beant2 != null){
			payBean = this.calDiscountProc(payBean,beant2,beant3);
		}else if(beant3 != null){
			payBean = this.calDiscountProc(payBean,beant3,null);
		}
		return payBean;
	}

	private PayBean calDiscountProc(PayBean payBean,DiscountBean bean1,DiscountBean bean2){

		double disPrice = 0.0;
		List<Long> ids = new ArrayList<>();
		if(bean2 !=null){
			disPrice = this.getDiscountPrice(payBean,bean2);
			if(disPrice > 0){
				payBean.setDiscountType(bean2.getType());
				payBean.setDiscountId(bean2.getId());
				ids.add(bean2.getId());
			}
		}

		double price = this.getDiscountPrice(payBean,bean1);
		if(price > 0){
			payBean.setDiscountType(bean1.getType());
			payBean.setDiscountId(bean1.getId());
			payBean.setDiscountPrice(price);
			ids.add(bean1.getId());
		}
		payBean.setDiscountIds(StringUtil.list2SqlString(ids));
		return payBean;

	}

	private double getDiscountPrice(PayBean payBean,DiscountBean discountBean){
		List<DiscountRuleBean> ruleBeans = tbDiscountRuleDao.getByDisId(discountBean.getId());
		if(ruleBeans == null || ruleBeans.isEmpty()){
			return 0;
		}
		DiscountRuleBean discountRuleBean = null;
		for(DiscountRuleBean bean :ruleBeans){
			if(payBean.getOrderPrice() >= bean.getExpendAmount()){
				if(discountRuleBean ==null || discountRuleBean.getExpendAmount() < bean.getExpendAmount()){
					discountRuleBean = bean;
				}
			}
		}
		if(discountRuleBean == null){
			return 0;
		}
		double discountPrice = 0.0f;
		if(discountBean.getType() == 2){
			int min = (int)(discountRuleBean.getDiscount1()*100);
			int max = (int)(discountRuleBean.getDiscount2()*100);
			Random random = new Random();
			int s = random.nextInt(max)%(max-min+1) + min;
			discountPrice = s/100.0;
		}else if(discountBean.getType() == 3){
			double discp = Arith.sub(1.0,Arith.div(discountRuleBean.getDiscount1(), 10.0, 3) );
			discountPrice = Arith.mul(payBean.getOrderPrice() , discp);
			discountPrice = Math.round((discountPrice-0.005)*100)/100.0; //四舍五入
		}else if(discountBean.getType() == 1){
			discountPrice = discountRuleBean.getDiscount1();
		}

		if(discountRuleBean.getPlanAmount() == 0){
			return discountPrice;
		}
		if(discountRuleBean.getPlanAmount()-discountRuleBean.getActualAmount()-discountPrice < 0){
			return 0;
		}
		int num = tbDiscountRuleDao.updateActual(discountRuleBean.getId(),discountPrice);
		if(num > 0){
			return discountPrice;
		}else {
			return 0;
		}


	}

	private StorePayInfo prePay(PayBean payBean,String orderNo){
		String storeNo = payBean.getStoreNo();
		//查询商家
		StoreBean storeBean = bmStoreDao.getStoreByNo(storeNo);
		if(storeBean == null){
			return null;
		}
		//查询商家绑定组织
		Long orgId = bmStoreBindOrgDao.getOrgIdByStoreNO(storeNo);
		//查询人员信息
		UserBean userBean = null;
		if(payBean.getPayMethod() == 1){
			userBean =bmUserDao.getUserByOpenId(payBean.getOpenId(),payBean.getPayMethod());
		}else if(payBean.getPayMethod() == 2){
			userBean =bmUserDao.getUserByOpenId(payBean.getBuyerId(),payBean.getPayMethod());
		}
		//查询商家支付方式
		StorePayInfo storePayInfo = bmStorePayInfoDao.getStorePayInfoByNO(storeNo);
		storePayInfo.setStoreName(storeBean.getName());

		//保存定单
		OrderBean orderBean = new OrderBean();
		orderBean.setOrderId(orderNo);
		orderBean.setActualChargeAmount(payBean.getOrderPrice()-payBean.getDiscountPrice());
		orderBean.setPlanChargeAmount(payBean.getOrderPrice());
		orderBean.setStatus(0);
		orderBean.setDiscountType(0);
		orderBean.setDiscountId(0);

		orderBean.setPayType(storePayInfo.getPayType());
		orderBean.setPayMethod(String.valueOf(payBean.getPayMethod()));
		orderBean.setStoreNo(storeNo);
		if(StringUtils.isEmpty(storeBean.getParentNo()) || "0".equals(storeBean.getParentNo())){
			orderBean.setParentStoreNo(storeBean.getStoreNo());
		}else {
			orderBean.setParentStoreNo(storeBean.getParentNo());
		}
		orderBean.setOrgId(orgId);
		orderBean.setUserId(userBean.getId());

		orderBean.setUtime(DateUtil.unixTime());
		orderBean.setCtime(DateUtil.unixTime());
		orderBean.setOrgIds(storeBean.getOrgIds());
		orderBean.setSaleId(storeBean.getSaleId());
		orderBean.setDiscountType(payBean.getDiscountType());
		orderBean.setDiscountId(payBean.getDiscountId());
		orderBean.setDiscountIds(payBean.getDiscountIds());
		int num = bmOrderDao.insertOrder(orderBean);
		return storePayInfo;
	}

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

			//插入商家
			//插入商家绑定组织
			//插入商家用户
			bmStorePayInfoDao.insertPayInfo(msStoreBean.getStoreNo(),2,"1,2",paykey,paysec,"","",json.toJSONString(),1);
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

	public boolean updateMerchantByPaykey(String storeNo,String wxRate,String aliRate,String settlementType) {
		boolean result = false;
		String url = MtConfig.getProperty("REQ_URL","")+"/updateMerchantByPaykey.do";
		try{
			StorePayInfo storePayInfo = bmStorePayInfoDao.getStorePayInfoByNO(storeNo);
			if(storePayInfo == null){
				return false;
			}
			Map<String, String> reqData = new HashMap<String, String>();
			reqData.put("paykey", storePayInfo.getField1()); //修改商户的paykey
			reqData.put("agent_no", MtConfig.getProperty("agent_no", "95272016121410000062"));//代理商编号
			reqData.put("wx_rate", wxRate); //微信费率
			reqData.put("ali_rate", aliRate);//支付宝费率
			reqData.put("settlement_type", settlementType);//商户结算周期
			String CERT_PATH_P12 = MtConfig.getProperty("CERT_PATH_P12","");
			String CERT_JKS_P12_PASSWORD = MtConfig.getProperty("CERT_JKS_P12_PASSWORD","");
			String signString = CertUtil.reqSign(MapUtil.coverMap2String(reqData), CERT_PATH_P12, CERT_JKS_P12_PASSWORD);
			reqData.put("sign", new String(signString)); // 签名后的字符串
			String stringData = MapUtil.getRequestParamString(reqData);
//
			String reqBase64 = new String(CertUtil.base64Encode(stringData.getBytes("UTF-8")));
			String rspBase64 = SSLUtil.httpsPost(url, reqBase64);
			String rspData = new String(CertUtil.base64Decode(rspBase64.getBytes("UTF-8")));
			Map<String,String> resultMap = MapUtil.convertResultStringToMap(rspData);
			if("SUCCESS".equals(resultMap.get("result_code"))){
				JSONObject json = new JSONObject();
				json.put("aliRate",aliRate);
				json.put("wxRate",wxRate);
				json.put("settlementType",settlementType);
				bmStorePayInfoDao.updateByNo(json.toJSONString(),storeNo);
				return true;
			}
		} catch (Exception e){
			logger.error("#MinshengService.updateMerchantByPaykey# storeNO={},wxRate={},aliRate={},settlementType={}",storeNo,wxRate,aliRate,settlementType,e);
		}
		return result;
	}

	//查询更新状态，发通知
	public OrderBean paySuccess(String orderNo){
		logger.info("#MinshengService.paySuccess# orderNo={}",orderNo);
		OrderBean orderBean = orderService.getByOrderId(orderNo);
		if(orderBean == null){
			return null;
		}
		if(orderBean.getPayMethod().contains("2")){
			ayncTaskUtil.commitAyncTask(new OrderSuccessAsync(orderBean));
		}
		return orderBean;
	}

	//查询更新状态
	public String queryOrderAndUpdate(String orderNo){
		logger.info("#MinshengService.queryOrderAndUpdate# orderNo={}",orderNo);
		String result = queryOrder(orderNo);
		if(StringUtils.isEmpty(result)){
			return "";
		}
		if(OrderStatus.FAIL.equals(result) || OrderStatus.NOTPAY.equals(result)){
			orderService.updateOrderByOrderId(2,orderNo);
		}else if(OrderStatus.SUCCESS.equals(result)){
			orderService.updateOrderByOrderId(1,orderNo);
		}
		return result;
	}

	//查询订单状态
	public String queryOrder(String orderNo){
		logger.info("#MinshengService.queryOrder# orderNo={}",orderNo);
		String result = "";
		try {
			OrderBean orderBean = orderService.getByOrderId(orderNo);
			StorePayInfo storePayInfo = bmStorePayInfoDao.getStorePayInfoByNO(orderBean.getStoreNo());
			if(storePayInfo == null){
				return "";
			}

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("paykey",storePayInfo.getField1());
			parameters.put("order_no",orderNo);  //请自己试试自己发起的订单
			/////签名///
			String sign = getSign(parameters, storePayInfo.getField2());//
			parameters.put("sign",sign);
			String postUrl = MtConfig.getProperty("ms_URL","http://115.159.235.109:8208")+"/qthd-pay-web-gateway/scanPay/QueryReturnJson";
			String str = HttpClient.sendPost(postUrl, parameters);
			Map<String,Object> map = JSON.parseObject(str, Map.class);
			result = (String)map.get("result_code");
			logger.info("#MinshengService.queryOrder# orderNo={} result ={}",orderNo,result);
		}catch (Exception e){
			logger.error("#MinshengService.queryOrder# orderNo={}",orderNo,e);
			return "";
		}
		return result;
//
	}

	public static void main(String[] args) {
//		new MinshengService().updateMerchantByPaykey("85a6c4e20bf54505bea8e75bc870d587","0.4","0.4","D0");
//		JSONObject json = new JSONObject();
//		json.put("aliRate",Double.parseDouble("9.0"));
//		json.put("wxRate",Double.parseDouble("7.8"));
//		json.put("settlementType","e");
//
//		String jj = json.toJSONString();
//		System.out.println(jj);
//		NOTPAY
//				FAIL SUCCESS  "WAITING_PAYMENT
//		new MinshengService().queryOrder("201701141602121fdSN0ba482a1d");
//		new AyncTaskUtil().commitAyncTask(new OrderSuccessAsync(null));
		for(int i =0;i<100;i++){
			int min = (int)2.0*100;
			int max = (int)3.4*100;
			Random random = new Random();
			int s = random.nextInt(max)%(max-min+1) + min;
			System.out.println(s/100.0);
		}

	}

}
