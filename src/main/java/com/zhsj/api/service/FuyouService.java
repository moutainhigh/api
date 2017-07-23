package com.zhsj.api.service;

import java.net.URLDecoder;
import java.net.URLEncoder;
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

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.fuyou.MchInfoFY;
import com.zhsj.api.constants.Const;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.MtConfig;
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
	
	public String queryWithdrawAmt(String mchntCd){
		logger.info("#FuyouService.queryWithdrawAmt# mchntCd={}",mchntCd);
		String result = "FAIL";
		try{
			Map<String, String> map = new HashMap<>();
			map.put("ins_cd", MtConfig.getProperty("FUYOU_INS_CD", ""));//机构号
			map.put("mchnt_cd", mchntCd);
			map.put("random_str", RandomStringGenerator.getRandomStringByLength(8));
			map.put("sign", "");
			String sign = Utils.getSign(map);
			map.put("sign", sign);
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_URL", "")+"/queryWithdrawAmt");
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
			logger.error("#FuyouService.refundMoney# queryWithdrawAmt={}",mchntCd,e);
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
	
	public String mchntNameCheck(String storeNo,String storeName){
		logger.info("#FuyouService.mchntNameCheck# storeNo={},storeName={}",storeNo,storeName);
		String result = "FAIL";
		try{
			Map<String, String> map = new HashMap<>();
			map.put("trace_no", storeNo);
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
			logger.error("#FuyouService.mchntNameCheck# storeNo={},storeName={}",storeNo,storeName,e);
		}
		return result;
	}
	
	public String mchntAdd(MchInfoFY mchInfo){
		logger.info("#FuyouService.mchntAdd# mchInfo={}",mchInfo);
		String result = "FAIL";
		try{
			Map<String, String> map = mchInfo.toMap();
			map.put("trace_no", mchInfo.getStoreNo());
			map.put("sign", Sign.getSign(map,MtConfig.getProperty("FUYOU_MCH_ADD_KEY", "")));
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_MCH_ADD_URL", "")+"wxMchntAdd");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"0000".equals(resMap.get("ret_code"))){
				result = resMap.get("ret_msg");
			}else{
				result = "SUCCESS";
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.mchntAdd# mchInfo={}",mchInfo,e);
		}
		return result;
	}
	
	public String mchntUpd(MchInfoFY mchInfo,String mchntCd){
		logger.info("#FuyouService.mchntAdd# mchInfo={}",mchInfo);
		String result = "FAIL";
		try{
			Map<String, String> map = mchInfo.toMap();
			map.put("trace_no", mchInfo.getStoreNo());
			map.put("fy_mchnt_cd", mchntCd);
			map.put("sign", Sign.getSign(map,MtConfig.getProperty("FUYOU_MCH_ADD_KEY", "")));
			String dataString = this.getResultData(map, MtConfig.getProperty("FUYOU_MCH_ADD_URL", "")+"wxMchntUpd");
			logger.info(dataString);
			Map<String, String> resMap = XMLBeanUtils.xmlToMap(dataString);
			if(!"0000".equals(resMap.get("ret_code"))){
				result = resMap.get("ret_msg");
			}else{
				result = "SUCCESS";
			}		
		}catch (Exception e) {
			result = "系统异常";
			logger.error("#FuyouService.mchntAdd# mchInfo={}",mchInfo,e);
		}
		return result;
	}
	
	
	
	
	
	public static void main(String[] args) {
		OrderBean orderBean = new OrderBean();
		orderBean.setActualChargeAmount(1);
		orderBean.setOrderId("1001170710532789007");
		orderBean.setPayMethod("1");
//		new PinganService().refundMoney(orderBean);
//		new FuyouService().searchRefund(orderBean);
//		new FuyouService().queryWithdrawAmt("0001000F0539031");
//		System.out.println(new FuyouService().mchntNameCheck("1111111333","222在"));
		
		MchInfoFY mchInfo = new MchInfoFY();
		mchInfo.setStoreNo("10001");
		mchInfo.setStep("3");
		
		
		mchInfo.setIns_cd("08A9999999");
		mchInfo.setMchnt_name("小林开店");
		mchInfo.setMchnt_shortname("小林开店了详情");
		mchInfo.setReal_name("小林开店了详情");
		mchInfo.setCertif_id("13072719871217189X");
		
		mchInfo.setAcnt_type("2");
		mchInfo.setInter_bank_no("305100001104");
		mchInfo.setIss_bank_nm("中国民生银行股份有限公司北京上地支行");
		mchInfo.setAcnt_nm("林春光");
		mchInfo.setAcnt_no("6226220108357913");
		mchInfo.setSet_cd("M0174");
		mchInfo.setArtif_nm("林春光");
		mchInfo.setAcnt_artif_flag("0");
		mchInfo.setAcnt_certif_id("13072719871217189X");
		mchInfo.setAcnt_certif_tp("0");
		
		
		
		mchInfo.setContact_email("286066088@qq.com");
		mchInfo.setContact_mobile("13811408460");
		mchInfo.setContact_person("林春光");
		mchInfo.setContact_phone("13811408460");
		
		mchInfo.setCity_cd("1000");
		mchInfo.setCounty_cd("1007");
		
		
		mchInfo.setBusiness("292");
		mchInfo.setSettle_amt("1");
		
		mchInfo.setSettle_tp("2");
		
		mchInfo.setTx_flag("0");
//		mchInfo.setTx_set_cd("1");
		
		mchInfo.setTh_flag("1");
		mchInfo.setWx_flag("1");
		mchInfo.setWx_set_cd("M0174");
		mchInfo.setAli_flag("1");
		mchInfo.setAli_set_cd("M0174");
//		new FuyouService().mchntAdd(mchInfo);
		new FuyouService().mchntUpd(mchInfo, "0001000F0539031");
	}
}
