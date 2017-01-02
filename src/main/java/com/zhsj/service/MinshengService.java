package com.zhsj.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.bean.*;
import com.zhsj.dao.*;
import com.zhsj.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class MinshengService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MinshengService.class);

	@Autowired
	private BmStoreDao bmStoreDao;
	@Autowired
	private BmStoreBindOrgDao bmStoreBindOrgDao;
	@Autowired
	private BmOrderDao bmOrderDao;
	@Autowired
	private BmUserDao bmUserDao;
	@Autowired
	private BmStorePayInfoDao bmStorePayInfoDao;
    /*此二字段由民生提供给各个商户*/
//	static String  payKey = "85a6c4e20bf54505bea8e75bc870d587";//此字符串由民生提供，作为商户的唯一标识
//	static String  paySec = "60f811a8b472495fa6c656c507f44cdc";//此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端

    public Map payWeChat(PayBean payBean){
		Map map = new HashMap();
		try{
			String storeNo = payBean.getStoreNo();
			if(StringUtils.isEmpty(storeNo)){

			}
			//查询商家
			StoreBean storeBean = bmStoreDao.getStoreByNo(storeNo);
			//查询商家支付方式
			StorePayInfo storePayInfo = bmStorePayInfoDao.getStorePayInfoByNO(storeNo);
			//查询商家绑定组织
			Long orgId = bmStoreBindOrgDao.getOrgIdByStoreNO(storeNo);
			//查询人员信息
			UserBean userBean = bmUserDao.getUserByOpenId(payBean.getOpenId());
			//获定单号
			String orderNo = StoreUtils.getOrderNO(storeNo);
			//保存定单
			OrderBean orderBean = new OrderBean();
			orderBean.setOrderId(orderNo);
			orderBean.setActualChargeAmount(payBean.getOrderPrice());
			orderBean.setPlanChargeAmount(payBean.getOrderPrice());
			orderBean.setStatus(0);
			orderBean.setDiscountType(0);
			orderBean.setDiscountId(0);

			orderBean.setPayType(storePayInfo.getPayType());
			orderBean.setPayMethod(String.valueOf(payBean.getPayMethod()));
			orderBean.setStoreNo(storeNo);
			orderBean.setParentStoreNo(storeBean.getParentNo());
			orderBean.setOrgId(orgId);
			orderBean.setUserId(userBean.getId());
			orderBean.setUtime(DateUtil.unixTime());
			orderBean.setCtime(DateUtil.unixTime());
			int num = bmOrderDao.insertOrder(orderBean);
			//调用接口
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("order_price", String.valueOf(payBean.getOrderPrice())); //支付费用单位：元，支付一分钱
			parameters.put("pay_way_code", "WXF2F");//当面付标识
			parameters.put("order_no", orderNo); ////入驻商户号+yyyyMMddHHmmss
			parameters.put("order_date", new SimpleDateFormat("yyyyMMdd").format(new Date()));
			parameters.put("order_time", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
			parameters.put("paykey", storePayInfo.getField1());//此字符串由民生提供，作为商户的唯一标识
			parameters.put("product_name", "微信公众号支付测试"); //产品名称，商户根据自己的需求填写
			parameters.put("order_ip", "114.215.223.220"); //发起交易的服务器地址
			parameters.put("remark", "微信主扫测试"); //附件内容
			parameters.put("order_period", "5");   //必填
			parameters.put("notify_url", "http://wwt.bj37du.com/api/pay/payNotifyWeChat");//必填，请填写通知地址
			parameters.put("field1", payBean.getOpenId()); //可不填
			parameters.put("field2", ""); //可不填，建议填写代理商名称
			parameters.put("field3", "");
			parameters.put("field4", "");
			parameters.put("field5", "");
			String sign = getSign(parameters, storePayInfo.getField2());////paySec 此字符串由民生提供，用作商户投递信息加密用，请妥善保管，请只放在服务器端
			parameters.put("sign",sign);
			String postUrl = MtConfig.getProperty("ms_URL","http://115.159.235.109:8208")+"/qthd-pay-web-gateway/scanPay/wxPrePay";
			String result = HttpClient.sendPost(postUrl, parameters);
			map = JSON.parseObject(result, Map.class);
			logger.info("#MinshengService.payWeChat# result={}",result);
		}catch (Exception e){
			logger.error("#MinshengService.payWeChat e={}#",e.getMessage(),e);
			map.put("return_code","FAIL");
		}
		return map;
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
	
	public static void main(String[] args) {
	}
}
