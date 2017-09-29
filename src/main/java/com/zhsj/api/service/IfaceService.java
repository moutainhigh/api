package com.zhsj.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.iface.BaseBean;
import com.zhsj.api.bean.iface.MicroPayReqBean;
import com.zhsj.api.bean.iface.MicroPayReqV2Bean;
import com.zhsj.api.bean.iface.MicroPayResBean;
import com.zhsj.api.bean.iface.MicroPayResV2Bean;
import com.zhsj.api.bean.iface.PreCreateReqBean;
import com.zhsj.api.bean.iface.PreCreateResBean;
import com.zhsj.api.bean.iface.QueryReqBean;
import com.zhsj.api.bean.iface.QueryReqV2Bean;
import com.zhsj.api.bean.iface.QueryResBean;
import com.zhsj.api.bean.iface.QueryResV2Bean;
import com.zhsj.api.bean.iface.RefundReqBean;
import com.zhsj.api.bean.iface.RefundReqV2Bean;
import com.zhsj.api.bean.iface.RefundResBean;
import com.zhsj.api.constants.DiscountATypeCons;
import com.zhsj.api.constants.OrderStatusCons;
import com.zhsj.api.constants.PayChannelCons;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.wft.RandomStringGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class IfaceService {
    private static  Logger logger = LoggerFactory.getLogger(IfaceService.class);

    @Autowired
    private StoreService storeService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    
    public CommonResult micropay(String req){
    	logger.info("#IfaceService.micropay# req={}",req);
    	try{
    		MicroPayReqBean reqBean = JSON.parseObject(req, MicroPayReqBean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		int channel = 0;
    		if(PayChannelCons.WSY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.WSY_CHANNEL.getType(); 
    		}else if(PayChannelCons.YDC_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.YDC_CHANNEL.getType();
    		}else if(PayChannelCons.XY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.XY_CHANNEL.getType();
    		}else if(PayChannelCons.SY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.SY_CHANNEL.getType();
    		}else{
    			return CommonResult.build(10006, "组织不存在");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getByDevice(channel, reqBean.getTerm_id());
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.build(10009, "终端号不存在");
    		}
    		String accountId = String.valueOf(accountBeans.get(0).getId());
    		
    		String uri = MtConfig.getProperty("PAY_URL", "")+"microPayAsyn";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("price", reqBean.getOrder_amt());
			map.put("accountId", accountId);
			map.put("storeNo", reqBean.getMchnt_cd());
			map.put("authCode", reqBean.getAuth_code());
			map.put("payChannel", channel);
			map.put("auth", "");
			map.put("mchOrderNo", reqBean.getMchnt_order_no());
			String content = HttpClient.sendPost(uri, map);
			CommonResult commonResult = JSON.parseObject(content, CommonResult.class);
			if(commonResult.getCode() != 0){
				return commonResult;
			}
			OrderBean bean = orderService.getByOrderId(commonResult.getData().toString());
    		MicroPayResBean resBean = new MicroPayResBean();
    		String order_type = "1".equals(bean.getPayMethod())?"WECHAT":"ALIPAY" ;
    		resBean.setOrder_type(order_type);
    		resBean.setIns_cd(reqBean.getIns_cd());
    		resBean.setMchnt_cd(reqBean.getMchnt_cd());
    		resBean.setTerm_id(reqBean.getTerm_id());
    		resBean.setTotal_amount(reqBean.getOrder_amt());
    		resBean.setActual_amount((int)Arith.mul(bean.getActualChargeAmount(), 100));
    		resBean.setTransaction_id(bean.getTransactionId());
    		resBean.setWwt_order_no(bean.getOrderId());
    		resBean.setMcnnt_order_no(reqBean.getMchnt_order_no());
			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
			resBean.setAddn_inf(reqBean.getAddn_inf());
			resBean.setSign(resBean.sign());
			return CommonResult.build(0, "SUCCESS", resBean);
    	}catch (Exception e) {
    		logger.error("#IfaceService.micropay# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }
    
    public CommonResult micropayV2(String req){
    	logger.info("#IfaceService.micropayV2# req={}",req);
    	try{
    		MicroPayReqV2Bean reqBean = JSON.parseObject(req, MicroPayReqV2Bean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		int channel = 0;
    		if(PayChannelCons.WSY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.WSY_CHANNEL.getType(); 
    		}else if(PayChannelCons.YDC_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.YDC_CHANNEL.getType();
    		}else if(PayChannelCons.XY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.XY_CHANNEL.getType();
    		}else if(PayChannelCons.SY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.SY_CHANNEL.getType();
    		}else{
    			return CommonResult.build(10006, "组织不存在");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getByDevice(channel, reqBean.getTerm_id());
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.build(10009, "终端号不存在");
    		}
    		String accountId = String.valueOf(accountBeans.get(0).getId());
    		
    		int price = (int)Arith.mul(Double.parseDouble(reqBean.getOrder_amt()), 100);
    		
    		String uri = MtConfig.getProperty("PAY_URL", "")+"microPayAsyn";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("price", price);
			map.put("accountId", accountId);
			map.put("storeNo", reqBean.getMchnt_cd());
			map.put("authCode", reqBean.getAuth_code());
			map.put("payChannel", channel);
			map.put("auth", "");
			map.put("mchOrderNo", reqBean.getMchnt_order_no());
			String content = HttpClient.sendPost(uri, map);
			CommonResult commonResult = JSON.parseObject(content, CommonResult.class);
			if(commonResult.getCode() != 0){
				return commonResult;
			}
			OrderBean bean = orderService.getByOrderId(commonResult.getData().toString());
    		MicroPayResV2Bean resBean = new MicroPayResV2Bean();
    		String order_type = "1".equals(bean.getPayMethod())?"WECHAT":"ALIPAY" ;
    		resBean.setOrder_type(order_type);
    		resBean.setIns_cd(reqBean.getIns_cd());
    		resBean.setMchnt_cd(reqBean.getMchnt_cd());
    		resBean.setTerm_id(reqBean.getTerm_id());
    		resBean.setTotal_amount(reqBean.getOrder_amt());
    		resBean.setActual_amount(String.valueOf(Arith.div(bean.getActualChargeAmount(), 1,2)));
    		resBean.setTransaction_id(bean.getTransactionId());
    		resBean.setWwt_order_no(bean.getOrderId());
    		resBean.setMcnnt_order_no(reqBean.getMchnt_order_no());
			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
			resBean.setAddn_inf(reqBean.getAddn_inf());
			resBean.setSign(resBean.sign());
			return CommonResult.build(0, "SUCCESS", resBean);
    	}catch (Exception e) {
    		logger.error("#IfaceService.micropayV2# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }
    
    public CommonResult commonQuery(String req){
    	logger.info("#IfaceService.commonQuery# req={}",req);
    	try{
    		QueryReqBean reqBean = JSON.parseObject(req, QueryReqBean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		
    		if(StringUtils.isEmpty(reqBean.getMchnt_order_no()) && StringUtils.isEmpty(reqBean.getTransaction_id()) 
    				&& StringUtils.isEmpty(reqBean.getWwt_order_no())){
    			return CommonResult.build(10002, "必填参数不能为空");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
    		String payMethod = "WECHAT".equals(reqBean.getOrder_type())?"1":"2" ;
    		
    		
			OrderBean bean = orderService.getOrder(reqBean.getMchnt_order_no(),reqBean.getTransaction_id(),
														reqBean.getWwt_order_no(),reqBean.getMchnt_cd());
			if(bean == null){
				return CommonResult.build(10005, "订单不存在");
			}
			
			String trans_stat= OrderStatusCons.of(bean.getStatus()).getDesc();
			
			String termId = "";
			if(bean.getAccountId() > 0){
				StoreAccountBean accountBean = tbStoreAccountDao.getById(bean.getAccountId());
				if(accountBean != null){
					termId = StringUtils.isEmpty(accountBean.getDeviceNo())?"":accountBean.getDeviceNo();
				}
			}
			
			QueryResBean resBean = new QueryResBean();
			resBean.setOrder_type(reqBean.getOrder_type());
			resBean.setIns_cd(reqBean.getIns_cd());
			resBean.setMchnt_cd(reqBean.getMchnt_cd());
			resBean.setTerm_id(termId);
			resBean.setOrder_amount((int)Arith.mul(bean.getPlanChargeAmount(), 100));
			resBean.setActual_amount((int)Arith.mul(bean.getActualChargeAmount(), 100));
			resBean.setTransaction_id(bean.getTransactionId());
    		resBean.setWwt_order_no(bean.getOrderId());
    		resBean.setMcnnt_order_no(reqBean.getMchnt_order_no());
    		resBean.setTrans_stat(trans_stat);
			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
			resBean.setSign(resBean.sign());
			return CommonResult.build(0, "SUCCESS", resBean);
    	}catch (Exception e) {
    		logger.error("#IfaceService.commonQuery# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }
    
    public CommonResult commonQueryV2(String req){
    	logger.info("#IfaceService.commonQueryV2# req={}",req);
    	try{
    		QueryReqV2Bean reqBean = JSON.parseObject(req, QueryReqV2Bean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		
    		if(StringUtils.isEmpty(reqBean.getMchnt_order_no()) && StringUtils.isEmpty(reqBean.getTransaction_id()) 
    				&& StringUtils.isEmpty(reqBean.getWwt_order_no())){
    			return CommonResult.build(10002, "必填参数不能为空");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
//    		String payMethod = "WECHAT".equals(reqBean.getOrder_type())?"1":"2" ;
    		
    		
			OrderBean bean = orderService.getOrder(reqBean.getMchnt_order_no(),reqBean.getTransaction_id(),
														reqBean.getWwt_order_no(),reqBean.getMchnt_cd());
			if(bean == null){
				return CommonResult.build(10005, "订单不存在");
			}
			
			String trans_stat= OrderStatusCons.of(bean.getStatus()).getDesc();
			
			String termId = "";
			if(bean.getAccountId() > 0){
				StoreAccountBean accountBean = tbStoreAccountDao.getById(bean.getAccountId());
				if(accountBean != null){
					termId = StringUtils.isEmpty(accountBean.getDeviceNo())?"":accountBean.getDeviceNo();
				}
			}
			
			QueryResV2Bean resBean = new QueryResV2Bean();
			resBean.setOrder_type(reqBean.getOrder_type());
			resBean.setIns_cd(reqBean.getIns_cd());
			resBean.setMchnt_cd(reqBean.getMchnt_cd());
			resBean.setTerm_id(termId);
			resBean.setOrder_amount(String.valueOf(Arith.div(bean.getPlanChargeAmount(), 1,2)));
			resBean.setActual_amount(String.valueOf(Arith.div(bean.getActualChargeAmount(), 1,2)));
			resBean.setTransaction_id(bean.getTransactionId());
    		resBean.setWwt_order_no(bean.getOrderId());
    		resBean.setMcnnt_order_no(reqBean.getMchnt_order_no());
    		resBean.setTrans_stat(trans_stat);
			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
			resBean.setSign(resBean.sign());
			return CommonResult.build(0, "SUCCESS", resBean);
    	}catch (Exception e) {
    		logger.error("#IfaceService.commonQueryV2# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }

    
    public CommonResult commonRefund(String req){
    	logger.info("#IfaceService.commonRefund# req={}",req);
    	try{
    		RefundReqBean reqBean = JSON.parseObject(req, RefundReqBean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		
    		int channel = 0;
    		if(PayChannelCons.WSY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.WSY_CHANNEL.getType(); 
    		}else if(PayChannelCons.YDC_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.YDC_CHANNEL.getType();
    		}else if(PayChannelCons.XY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.XY_CHANNEL.getType();
    		}else if(PayChannelCons.SY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.SY_CHANNEL.getType();
    		}else{
    			return CommonResult.build(10006, "组织不存在");
    		}
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getByDevice(channel, reqBean.getTerm_id());
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.build(10009, "终端号不存在");
    		}
    		int accountId = (int)(accountBeans.get(0).getId());
    		
    		OrderBean orderBean = orderService.getByOrderId(reqBean.getRefund_order_no());
    		if(orderBean == null){
				return CommonResult.build(10005, "订单不存在");
			}
			double v1 = reqBean.getRefund_amt();
    		CommonResult refundReult = orderService.appRefund(orderBean.getId(), Arith.div(v1, 100, 2), accountId);
    		if(refundReult.getCode() == 2){
    			return CommonResult.build(10007, refundReult.getMsg());
    		}
    		if(refundReult.getCode() == 0){
    			RefundResBean resBean = new RefundResBean();
    			String order_type = "1".equals(orderBean.getPayMethod())?"WECHAT":"ALIPAY" ;
    			resBean.setOrder_type(order_type);
    			resBean.setIns_cd(reqBean.getIns_cd());
    			resBean.setMchnt_cd(reqBean.getMchnt_cd());
    			resBean.setTerm_id(String.valueOf(orderBean.getAccountId()));
    			resBean.setTransaction_id(orderBean.getTransactionId());
        		resBean.setWwt_order_no(orderBean.getOrderId());
        		resBean.setMcnnt_order_no(orderBean.getMchntOrderNo());
    			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
    			resBean.setSign(resBean.sign());
    			return CommonResult.build(0, "SUCCESS", resBean);
    		}
    		return refundReult;
    	}catch (Exception e) {
    		logger.error("#IfaceService.commonRefund# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }

    public CommonResult commonRefundV2(String req){
    	logger.info("#IfaceService.commonRefundV2# req={}",req);
    	try{
    		RefundReqV2Bean reqBean = JSON.parseObject(req, RefundReqV2Bean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		int channel = 0;
    		if(PayChannelCons.WSY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.WSY_CHANNEL.getType(); 
    		}else if(PayChannelCons.YDC_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.YDC_CHANNEL.getType();
    		}else if(PayChannelCons.XY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.XY_CHANNEL.getType();
    		}else if(PayChannelCons.SY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.SY_CHANNEL.getType();
    		}else{
    			return CommonResult.build(10006, "组织不存在");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
    		List<StoreAccountBean> accountBeans = tbStoreAccountDao.getByDevice(channel, reqBean.getTerm_id());
    		if(CollectionUtils.isEmpty(accountBeans)){
    			return CommonResult.build(10009, "终端号不存在");
    		}
    		int accountId = (int)(accountBeans.get(0).getId());
    		
    		OrderBean orderBean = orderService.getByOrderId(reqBean.getRefund_order_no());
    		if(orderBean == null){
    			orderBean = orderService.getByTransactionId(reqBean.getRefund_order_no());
    			if(orderBean  == null){
    				return CommonResult.build(10005, "订单不存在");
    			}
			}
    		if(orderBean.getStatus() == 3 || orderBean.getStatus() == 4 || orderBean.getStatus() == 5){
    			return CommonResult.build(10007, "重复退款");
    		}
			double v1 = Double.parseDouble(reqBean.getRefund_amt());
    		CommonResult refundReult = orderService.appRefund(orderBean.getId(),v1, accountId);
    		if(refundReult.getCode() == 2){
    			return CommonResult.build(10007, refundReult.getMsg());
    		}
    		if(refundReult.getCode() == 0){
    			RefundResBean resBean = new RefundResBean();
    			String order_type = "1".equals(orderBean.getPayMethod())?"WECHAT":"ALIPAY" ;
    			resBean.setOrder_type(order_type);
    			resBean.setIns_cd(reqBean.getIns_cd());
    			resBean.setMchnt_cd(reqBean.getMchnt_cd());
    			resBean.setTerm_id(reqBean.getTerm_id());
    			resBean.setTransaction_id(orderBean.getTransactionId());
        		resBean.setWwt_order_no(orderBean.getOrderId());
        		resBean.setMcnnt_order_no(orderBean.getMchntOrderNo());
    			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
    			resBean.setSign(resBean.sign());
    			return CommonResult.build(0, "SUCCESS", resBean);
    		}
    		return refundReult;
    	}catch (Exception e) {
    		logger.error("#IfaceService.commonRefundV2# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }
    
    public CommonResult preCreate(String req){
    	logger.info("#IfaceService.preCreate# req={}",req);
    	try{
    		PreCreateReqBean reqBean = JSON.parseObject(req, PreCreateReqBean.class);
    		
    		CommonResult result = this.checkMicroPay(reqBean);
    		if(result.getCode() != 0){
    			return result;
    		}
    		int channel = 0;
    		if(PayChannelCons.WSY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.WSY_CHANNEL.getType(); 
    		}else if(PayChannelCons.YDC_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.YDC_CHANNEL.getType();
    		}else if(PayChannelCons.XY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.XY_CHANNEL.getType();
    		}else if(PayChannelCons.SY_CHANNEL.getDesc().equals(reqBean.getIns_cd())){
    			channel = PayChannelCons.SY_CHANNEL.getType();
    		}else{
    			return CommonResult.build(10006, "组织不存在");
    		}
    		
    		StoreBean storeBean = storeService.getStoreByNO(reqBean.getMchnt_cd());
    		if(storeBean == null){
    			return CommonResult.build(10004, "商家不存在");
    		}
    		
    		int payMethod = 0;
    		if("WECHAT".equals(reqBean.getOrder_type())){
    			payMethod = 1;
    		}else if ("ALIPAY".equals(reqBean.getOrder_type())) {
    			payMethod = 2;
			}
    		if(payMethod == 0){
    			return CommonResult.build(10010, "支付类型不支持");
    		}
    		
    		double price = Arith.div(Double.parseDouble(reqBean.getOrder_amt()),100,2);
    		
    		String uri = MtConfig.getProperty("PAY_URL", "")+"preCreate";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("payMethod", payMethod);
			map.put("storeNo", reqBean.getMchnt_cd());
			map.put("orderPrice", price);
			map.put("openId", reqBean.getOpenid());
			map.put("payChannel",channel);
			map.put("mchNo", reqBean.getMchnt_order_no());
			map.put("notifyUrl", reqBean.getNotify_url());
			
			String content = HttpClient.sendPost(uri, map);
			CommonResult commonResult = JSON.parseObject(content, CommonResult.class);
			if(commonResult.getCode() != 0){
				return commonResult;
			}
			
			
			Map<String, String> resMap = JSON.parseObject(commonResult.getData().toString(), Map.class);
			PreCreateResBean resBean = new PreCreateResBean();
    		
    		resBean.setIns_cd(reqBean.getIns_cd());
    		resBean.setMchnt_cd(reqBean.getMchnt_cd());
			resBean.setRandom_str(RandomStringGenerator.getRandomStringByLength(8));
    		resBean.setOrder_type(reqBean.getOrder_type());
    		resBean.setWwt_order_no(resMap.get("orderId"));
    		resBean.setMchnt_order_no(reqBean.getMchnt_order_no());
    		resBean.setSdk_appid(resMap.get("appId"));
    		resBean.setSdk_noncestr(resMap.get("nonceStr"));
    		resBean.setSdk_package(resMap.get("package"));
    		resBean.setSdk_signtype(resMap.get("signType"));
    		resBean.setSdk_timestamp(resMap.get("timeStamp"));
    		resBean.setSdk_sign(resMap.get("paySign"));
    		resBean.setTrade_no(resMap.get("trade_no"));
			resBean.setSign(resBean.sign());
			return CommonResult.build(0, "SUCCESS", resBean);

    	}catch (Exception e) {
    		logger.error("#IfaceService.preCreate# req={}",req,e);
		}
    	return CommonResult.defaultError("系统出错");
    }


    private CommonResult checkMicroPay(BaseBean bean){
    	if(bean == null){
    		return CommonResult.build(10001, "参数不正确");
    	}
    	if(!bean.checkNull()){
    		return CommonResult.build(10002, "必填参数不能为空");
    	}
    	String sign = bean.sign();
    	
    	if(!StringUtils.isNotEmpty(sign) || !sign.equals(bean.sign())){
    		return CommonResult.build(10003, "验签失败");
    	}
    	
    	return CommonResult.success("");
    }
    
    
   
    
}
