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
import com.zhsj.api.constants.Const;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.XMLBeanUtils;
import com.zhsj.api.util.fuyou.HttpUtils;
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
			
			String dataString = this.getResultData(map, "/commonRefund");
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
			
			String dataString = this.getResultData(map, "/commonQuery");
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
	
	public String getResultData(Map<String, String> map,String uri) throws Exception{
		String sign = Utils.getSign(map);
		map.put("sign", sign);
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
        httpUtils.post(MtConfig.getProperty("FUYOU_URL", "")+ uri, nvs, result);

        return URLDecoder.decode(result.toString(), Const.FUYOU_CHARSET);
	}
	
	public static void main(String[] args) {
		OrderBean orderBean = new OrderBean();
		orderBean.setActualChargeAmount(1);
		orderBean.setOrderId("1001170710532789007");
		orderBean.setPayMethod("1");
//		new PinganService().refundMoney(orderBean);
		new FuyouService().searchRefund(orderBean);
	}
}
