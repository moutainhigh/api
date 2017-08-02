package com.zhsj.api.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.BusinessTypeBean;
import com.zhsj.api.bean.CityCodeBean;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.fuyou.MchInfoFY;
import com.zhsj.api.constants.Const;
import com.zhsj.api.constants.T1RateCons;
import com.zhsj.api.dao.TBBusinessTypeDao;
import com.zhsj.api.dao.TBCityCodeDao;
import com.zhsj.api.dao.TBStoreExtendDao;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.SpringBeanUtil;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.fuyou.HttpUtils;
import com.zhsj.api.util.fuyou.Sign;
import com.zhsj.api.util.fuyou.Utils;
import com.zhsj.api.util.wft.RandomStringGenerator;

@Service
public class FuyouService {
	
    private static final Logger logger = LoggerFactory.getLogger(FuyouService.class);
    
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;
    @Autowired
    private TBCityCodeDao tbCityCodeDao;
    @Autowired
    private TBBusinessTypeDao tbBusinessTypeDao;
    
	public String refundMoney(OrderBean orderBean,double price,int userId){
		logger.info("#FuyouService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId);
		String result = "SUCCESS";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, String> map = new HashMap<>();
			map.put("version", "1.0");
			map.put("ins_cd",MtConfig.getProperty("FUYOU_INS_CD", ""));//机构号
			map.put("mchnt_cd", storePayInfo.getMchId());
			map.put("term_id", RandomStringGenerator.getRandomStringByLength(8));
			map.put("random_str", RandomStringGenerator.getRandomStringByLength(8));
			map.put("sign", "");
			if("1".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "WECHAT");
			}else if("2".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "ALIPAY");
			}
			map.put("mchnt_order_no", orderBean.getOrderId());
			map.put("refund_order_no", "re"+orderBean.getOrderId());
			map.put("total_amt", String.valueOf((int)(Arith.mul(price, 100))));
			map.put("refund_amt", String.valueOf((int)(Arith.mul(price, 100))));
			map.put("operator_id", "");
			map.put("reserved_fy_term_id", "");
			String sign = Utils.getSign(map);
			map.put("sign", sign);
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_URL", "")+"/commonRefund");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"000000".equals(resMap.get("result_code"))){
				result = resMap.get("result_msg");
			}else{
				result = "SUCCESS";
			}
		}catch (Exception e) {
			result = "系统导常";
			logger.error("#FuyouService.refundMoney# orderBean={},price={},userId={}",orderBean,price,userId,e);
		}
		return result;
	}
	
	public String searchRefund(OrderBean orderBean){
		logger.info("#FuyouService.refundMoney# orderBean={}",orderBean);
		String result = "FAIL";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, String> map = new HashMap<>();
			map.put("version", "1.0");
			map.put("ins_cd", MtConfig.getProperty("FUYOU_INS_CD", ""));//机构号
			map.put("mchnt_cd", storePayInfo.getMchId());
			map.put("term_id", RandomStringGenerator.getRandomStringByLength(8));
			map.put("random_str", RandomStringGenerator.getRandomStringByLength(8));
			map.put("sign", "");
			if("1".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "WECHAT");
			}else if("2".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "ALIPAY");
			}
			map.put("mchnt_order_no", orderBean.getOrderId());
			String sign = Utils.getSign(map);
			map.put("sign", sign);
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_URL", "")+"/commonQuery");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"000000".equals(resMap.get("result_code"))){
				result = resMap.get("result_msg");
			}else{
				if("REFUND".equals(resMap.get("trans_stat"))){
					result = "SUCCESS";
				}else{
					result = "FAIL";
				}
				
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.refundMoney# orderBean={}",orderBean,e);
		}
		return result;
	}
	
	public String searchOrder(OrderBean orderBean){
		logger.info("#FuyouService.searchOrder# orderBean={}",orderBean);
		String result = "FAIL";
		try{
			List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getByStoreNoAndType(orderBean.getStoreNo(), orderBean.getPayType(), orderBean.getPayMethod());
			if(CollectionUtils.isEmpty(storePayInfos)){
				return "支付类型错误";
			}
			StorePayInfo storePayInfo = storePayInfos.get(0);
			
			Map<String, String> map = new HashMap<>();
			map.put("version", "1.0");
			map.put("ins_cd", MtConfig.getProperty("FUYOU_INS_CD", ""));//机构号
			map.put("mchnt_cd", storePayInfo.getMchId());
			map.put("term_id", RandomStringGenerator.getRandomStringByLength(8));
			map.put("random_str", RandomStringGenerator.getRandomStringByLength(8));
			map.put("sign", "");
			if("1".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "WECHAT");
			}else if("2".equals(storePayInfo.getPayMethod())){
				map.put("order_type", "ALIPAY");
			}
			map.put("mchnt_order_no", orderBean.getOrderId());
			String sign = Utils.getSign(map);
			map.put("sign", sign);
			
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_URL", "")+"/commonQuery");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"000000".equals(resMap.get("result_code"))){
				result = resMap.get("result_msg");
			}else{
				result = resMap.get("trans_stat");
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.searchOrder# orderBean={}",orderBean,e);
		}
		return result;
	}
	
	public String getResultData(Map<String, String> map,String url) throws Exception{
		Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("xml");
        Iterator it=map.keySet().iterator();
        while(it.hasNext()){
            String key = it.next().toString();
            String value =map.get(key);
            root.addElement(key).addText(value);
        }
        String reqBody = "<?xml version=\"1.0\" encoding=\"GBK\" standalone=\"yes\"?>" + doc.getRootElement().asXML();
        logger.info("#FuyouService.getResultData# preDate={}",reqBody);
        reqBody = URLEncoder.encode(reqBody, Const.FUYOU_CHARSET);
        Map<String, String> nvs = new HashMap<>();
        nvs.put("req", reqBody);
        StringBuffer result = new StringBuffer("");
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.post(url, nvs, result);

        return URLDecoder.decode(result.toString(), Const.FUYOU_CHARSET);
	}
	
	public String mchntNameCheck(String storeName){
		logger.info("#FuyouService.mchntNameCheck# storeName={}",storeName);
		String result = "FAIL";
		try{
			Map<String, String> map = new HashMap<>();
			map.put("trace_no", RandomStringGenerator.getRandomStringByLength(8));
			map.put("mchnt_name", storeName);//机构号
			map.put("sign", Sign.getSign(map,MtConfig.getProperty("FUYOU_MCH_ADD_KEY", "")));
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_MCH_ADD_URL", "")+"wxMchntNameCheck");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"0000".equals(resMap.get("ret_code"))){
				result = resMap.get("ret_msg");
			}else{
				result = "SUCCESS";
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.mchntNameCheck# storeName={}",storeName,e);
		}
		return result;
	}
	
	public String mchntAdd(MchInfoFY mchInfo){
		logger.info("#FuyouService.mchntAdd# mchInfo={}",mchInfo);
		String result = "FAIL";
		try{
			String mchntName = mchInfo.getMchnt_name();
			String checkName = this.mchntNameCheck(mchntName);
			if(!"SUCCESS".equals(checkName)){
				mchntName += "wwt88";
				checkName = this.mchntNameCheck(mchntName);
			}
			if(!"SUCCESS".equals(checkName)){
				return "商家名称已经存在,请联系管理员";
			}
			
			List<String> cityCodes = new ArrayList<>();
			cityCodes.add(mchInfo.getCity());
			cityCodes.add(mchInfo.getCounty());
			List<CityCodeBean> cityCodeBeans = tbCityCodeDao.getCityCodes(cityCodes);
			if(CollectionUtils.isEmpty(cityCodeBeans)){
				return result;
			}
			Map<String,String> cityMap = new HashMap<>();
			for(CityCodeBean bean:cityCodeBeans){
				cityMap.put(bean.getCode(), bean.getFyCode());
			}
			
			BusinessTypeBean btypeBean = tbBusinessTypeDao.getById(Integer.parseInt(mchInfo.getBusiness()));
			if(btypeBean == null){
				return result;
			}
			
			Map<String, String> map = new HashMap<>();
			map.put("trace_no", mchInfo.getStoreNo());
			map.put("ins_cd", MtConfig.getProperty("FUYOU_INS_CD", ""));
			
//			private String link_mchnt_cd;//挂靠商户号
			/***商户基本信息***/
			map.put("mchnt_name", mchntName);//商户名称
			map.put("mchnt_shortname",mchInfo.getMchnt_shortname());
			map.put("real_name", mchInfo.getReal_name());
			map.put("certif_id", mchInfo.getCertif_id());
			map.put("contact_person", mchInfo.getContact_person());
			map.put("contact_mobile", mchInfo.getContact_mobile());
			map.put("contact_email", mchInfo.getContact_email());
			map.put("contact_phone", mchInfo.getContact_phone());
			map.put("city_cd", cityMap.get(mchInfo.getCity()));
			map.put("county_cd", cityMap.get(mchInfo.getCounty()));
			map.put("business", btypeBean.getCode());
			
			/********结算信息*********/
			map.put("acnt_type", mchInfo.getAcnt_type());
			map.put("acnt_artif_flag", mchInfo.getAcnt_artif_flag());
			if("0".equals( mchInfo.getAcnt_artif_flag())){
				map.put("artif_nm", mchInfo.getContact_person());
			}
			map.put("acnt_certif_id",mchInfo.getAcnt_certif_id());
			map.put("inter_bank_no", mchInfo.getInter_bank_no());
			map.put("iss_bank_nm", mchInfo.getIss_bank_nm());
			map.put("acnt_nm", mchInfo.getAcnt_nm());
			map.put("acnt_no", mchInfo.getAcnt_no());
			map.put("wx_set_cd" ,T1RateCons.of(Double.parseDouble(mchInfo.getWx_set_cd())).getDesc());
			map.put("ali_set_cd", T1RateCons.of(Double.parseDouble(mchInfo.getAli_set_cd())).getDesc());
			
			
			map.put("set_cd", T1RateCons.of(Double.parseDouble(mchInfo.getWx_set_cd())).getDesc());
			map.put("settle_amt", "1");//小额清算金额（单位分）
			map.put("settle_tp", "1");//清算类型：1自动结算；2手动结算
			map.put("tx_flag", "0"); //是否开通D0
//			map.put("tx_set_cd", value);//D0
			map.put("daily_settle_flag","0");//	是否开通D1提现（0:不开通，1：开通
//			map.put("daily_settle_set_cd; // D1扣率套餐代码（若开通D1则必填）
					
			
			map.put("wx_flag", "1"); //微信支付标识(0：关闭微信,1：开通微信)
			map.put("ali_flag" , "1"); //支付宝支付标识(0：关闭支付宝,1：开通支付宝)
			map.put("acnt_certif_tp" , "0");;//入账证件类型("0":"身份证","1":"护照","2":"军官证","3":"士兵证","4":"回乡证","5":"户口本","6":"外国护照","7":"其它")
			map.put("th_flag", "1");//退货标识(0:不能退货,1:可以退货)
			
			map.put("qpay_flag", "0");
			map.put("jdpay_flag", "0");
			map.put("wxapp_flag", "0");
			map.put("cup_qrpay_st", "0");
			
			
			map.put("sign", Sign.getSign(map,MtConfig.getProperty("FUYOU_MCH_ADD_KEY", "")));

			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_MCH_ADD_URL", "")+"wxMchntAdd");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"0000".equals(resMap.get("ret_code"))){
				result = resMap.get("ret_msg");
			}else{
				List<StorePayInfo> payInfos = new ArrayList<>();
				StorePayInfo payInfo1 = new StorePayInfo();
				payInfo1.setStoreNo(mchInfo.getStoreNo());
				payInfo1.setPayType(6);
				payInfo1.setPayMethod("1");
				payInfo1.setField3(mchInfo.getWx_set_cd());
				payInfo1.setAppId("wx8651744246a92699");
				payInfo1.setMchId(resMap.get("fy_mchnt_cd"));
				payInfos.add(payInfo1);
				
				StorePayInfo payInfo2 = new StorePayInfo();
				payInfo2.setStoreNo(mchInfo.getStoreNo());
				payInfo2.setPayType(6);
				payInfo2.setPayMethod("2");
				payInfo2.setField3(mchInfo.getAli_set_cd());
				payInfo2.setAppId("2016090701864634");
				payInfo2.setMchId(resMap.get("fy_mchnt_cd"));
				payInfos.add(payInfo2);
				tbStorePayInfoDao.insertFY(payInfos);
				result = "SUCCESS";
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.mchntAdd# mchInfo={}",mchInfo,e);
		}
		return result;
	}
	
	public String mchntUpd(MchInfoFY mchInfo,String mchntCd){
		logger.info("#FuyouService.mchntUpd# mchInfo={}",mchInfo);
		String result = "FAIL";
		try{
			String mchntName = mchInfo.getMchnt_name();
			
			List<String> cityCodes = new ArrayList<>();
			cityCodes.add(mchInfo.getCity());
			cityCodes.add(mchInfo.getCounty());
			List<CityCodeBean> cityCodeBeans = tbCityCodeDao.getCityCodes(cityCodes);
			if(CollectionUtils.isEmpty(cityCodeBeans)){
				return result;
			}
			Map<String,String> cityMap = new HashMap<>();
			for(CityCodeBean bean:cityCodeBeans){
				cityMap.put(bean.getCode(), bean.getFyCode());
			}
			
			BusinessTypeBean btypeBean = tbBusinessTypeDao.getById(Integer.parseInt(mchInfo.getBusiness()));
			if(btypeBean == null){
				return result;
			}
			
			Map<String, String> map = new HashMap<>();
			map.put("trace_no", mchInfo.getStoreNo());
			
			map.put("fy_mchnt_cd", "0001000F0547650");
			map.put("ins_cd", MtConfig.getProperty("FUYOU_INS_CD", ""));
			
//			private String link_mchnt_cd;//挂靠商户号
			/***商户基本信息***/
//			map.put("mchnt_name", mchntName);//商户名称
//			map.put("mchnt_shortname",mchInfo.getMchnt_shortname());
//			map.put("real_name", mchInfo.getReal_name());
//			map.put("certif_id", mchInfo.getCertif_id());
//			map.put("contact_person", mchInfo.getContact_person());
//			map.put("contact_mobile", mchInfo.getContact_mobile());
//			map.put("contact_email", mchInfo.getContact_email());
//			map.put("contact_phone", mchInfo.getContact_phone());
//			map.put("city_cd", cityMap.get(mchInfo.getCity()));
//			map.put("county_cd", cityMap.get(mchInfo.getCounty()));
//			map.put("business", btypeBean.getCode());
//			
//			/********结算信息*********/
//			map.put("acnt_type", mchInfo.getAcnt_type());
//			map.put("acnt_artif_flag", mchInfo.getAcnt_artif_flag());
//			if("0".equals( mchInfo.getAcnt_artif_flag())){
//				map.put("artif_nm", mchInfo.getContact_person());
//			}
//			map.put("acnt_certif_id",mchInfo.getAcnt_certif_id());
//			map.put("inter_bank_no", mchInfo.getInter_bank_no());
//			map.put("iss_bank_nm", mchInfo.getIss_bank_nm());
//			map.put("acnt_nm", mchInfo.getAcnt_nm());
//			map.put("acnt_no", mchInfo.getAcnt_no());
			map.put("wx_set_cd" ,T1RateCons.of(Double.parseDouble(mchInfo.getWx_set_cd())).getDesc());
//			map.put("ali_set_cd", T1RateCons.of(Double.parseDouble(mchInfo.getAli_set_cd())).getDesc());
//			
//			
//			map.put("set_cd", T1RateCons.of(Double.parseDouble(mchInfo.getWx_set_cd())).getDesc());
//			map.put("settle_amt", "1");//小额清算金额（单位分）
//			map.put("settle_tp", "1");//清算类型：1自动结算；2手动结算
//			map.put("tx_flag", "0"); //是否开通D0
////			map.put("tx_set_cd", value);//D0
//			map.put("daily_settle_flag","0");//	是否开通D1提现（0:不开通，1：开通
////			map.put("daily_settle_set_cd; // D1扣率套餐代码（若开通D1则必填）
//					
//			
//			map.put("wx_flag", "1"); //微信支付标识(0：关闭微信,1：开通微信)
//			map.put("ali_flag" , "1"); //支付宝支付标识(0：关闭支付宝,1：开通支付宝)
//			map.put("acnt_certif_tp" , "0");;//入账证件类型("0":"身份证","1":"护照","2":"军官证","3":"士兵证","4":"回乡证","5":"户口本","6":"外国护照","7":"其它")
//			map.put("th_flag", "1");//退货标识(0:不能退货,1:可以退货)
			map.put("sign", Sign.getSign(map,MtConfig.getProperty("FUYOU_MCH_ADD_KEY", "")));

			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_MCH_ADD_URL", "")+"wxMchntUpd");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"0000".equals(resMap.get("ret_code"))){
				result = resMap.get("ret_msg");
			}else{
				
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.mchntAdd# mchInfo={}",mchInfo,e);
		}
		return result;
	}
	
	
	
	
	
	public static void main(String[] args) {
		new FuyouService().mchntNameCheck("张店一二三商务信息咨询服务部");
		
//		OrderBean orderBean = new OrderBean();
//		orderBean.setActualChargeAmount(1);
//		orderBean.setOrderId("1001170710532789007");
//		orderBean.setPayMethod("1");
////		new PinganService().refundMoney(orderBean);
////		new FuyouService().searchRefund(orderBean);
//		new FuyouService().queryWithdrawAmt("0001000F0539031");
////		System.out.println(new FuyouService().mchntNameCheck("1111111333","222在"));
//		
//		MchInfoFY mchInfo = new MchInfoFY();
//		mchInfo.setStoreNo("10001");
//		mchInfo.setStep("3");
//		
//		
//		mchInfo.setIns_cd("08A9999999");
//		mchInfo.setMchnt_name("小林开店");
//		mchInfo.setMchnt_shortname("小林开店了详情");
//		mchInfo.setReal_name("小林开店了详情");
//		mchInfo.setCertif_id("13072719871217189X");
//		
//		mchInfo.setAcnt_type("2");
//		mchInfo.setInter_bank_no("305100001104");
//		mchInfo.setIss_bank_nm("中国民生银行股份有限公司北京上地支行");
//		mchInfo.setAcnt_nm("林春光");
//		mchInfo.setAcnt_no("6226220108357913");
//		mchInfo.setSet_cd("M0174");
//		mchInfo.setArtif_nm("林春光");
//		mchInfo.setAcnt_artif_flag("0");
//		mchInfo.setAcnt_certif_id("13072719871217189X");
//		mchInfo.setAcnt_certif_tp("0");
//		
//		
//		
//		mchInfo.setContact_email("286066088@qq.com");
//		mchInfo.setContact_mobile("13811408460");
//		mchInfo.setContact_person("林春光");
//		mchInfo.setContact_phone("13811408460");
//		
//		mchInfo.setCity_cd("1000");
//		mchInfo.setCounty_cd("1007");
//		
//		
//		mchInfo.setBusiness("292");
//		mchInfo.setSettle_amt("1");
//		
//		mchInfo.setSettle_tp("2");
//		
//		mchInfo.setTx_flag("0");
////		mchInfo.setTx_set_cd("1");
//		
//		mchInfo.setTh_flag("1");
//		mchInfo.setWx_flag("1");
//		mchInfo.setWx_set_cd("M0174");
//		mchInfo.setAli_flag("1");
//		mchInfo.setAli_set_cd("M0174");
////		new FuyouService().mchntAdd(mchInfo);
////		new FuyouService().mchntUpd(mchInfo, "0001000F0539031");
	}
}
