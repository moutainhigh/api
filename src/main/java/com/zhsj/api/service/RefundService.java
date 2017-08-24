package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ModelAndView;
import com.mysql.fabric.xmlrpc.base.Array;
import com.zhsj.api.bean.AliPayInfo;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.OrderRefundBean;
import com.zhsj.api.bean.RefundCode;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.iface.RefundResBean;
import com.zhsj.api.dao.TBOrderDao;
import com.zhsj.api.dao.TBOrderRefundDao;
import com.zhsj.api.dao.TBRefundCodeDao;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.dao.TbStorePayInfoDao;
import com.zhsj.api.dao.TbUserDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.WebUtils;
import com.zhsj.api.util.wft.RandomStringGenerator;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class RefundService {
    Logger logger = LoggerFactory.getLogger(RefundService.class);
    
    @Autowired
    private TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    private TBRefundCodeDao tbRefundCodeDao;
    @Autowired
    private TBOrderDao tbOrderDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TbUserDao tbUserDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private TbStorePayInfoDao tbStorePayInfoDao;
    @Autowired
    WXService wxService;
    @Autowired
    AliService aliService;
    @Autowired
    TBOrderRefundDao tbOrderRefundDao;
    
    public CommonResult refundCode(String accountId){
    	logger.info("#RefundService.refundCode# accountId={}",accountId);
    	try{
    		long aid = Long.parseLong(accountId);
    		String storeNo = tbStoreBindAccountDao.getStoreNoByAccountId(aid);
    		if(StringUtils.isEmpty(storeNo)){
    			return CommonResult.defaultError("用户信息错误");
    		}
    		String code = storeNo + accountId + DateUtil.unixTime();
    		code = Md5.encrypt(code);
    		int num = tbRefundCodeDao.insert(code, storeNo);
    		if(num < 1){
    			return CommonResult.defaultError("保存信息出错");
    		}
    		Map<String,String> map = new HashMap();
    		map.put("code", code);
    		String url = MtConfig.getProperty("API_URL", "")+"refund/scanCode?code="+code;
    		map.put("url",url);
    		return CommonResult.success("", map);
    	}catch (Exception e) {
			logger.error("#RefundService.refundCode# accountId={}",accountId,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    public CommonResult searchByNo(String orderId,String transactionId){
    	logger.info("#RefundService.searchByNo# orderId={} transactionId={}",orderId,transactionId);
    	try{
    		OrderBean orderBean = null;
    		if(StringUtils.isNotEmpty(orderId)){
    			orderBean = tbOrderDao.getByOrderId(orderId);
    		}
    		if(StringUtils.isNotEmpty(transactionId) && orderBean == null){
    			orderBean = tbOrderDao.getByTransactionId(transactionId);
    		}
    		if(orderBean == null){
    			return CommonResult.defaultError("订单不存在");
    		}
    		String operator = "";
    		if(orderBean.getPayChannel() == 1){
    			operator = "固定二维码";
    		}else if(orderBean.getAccountId() > 0) {
    			StoreAccountBean bean = tbStoreAccountDao.getById(orderBean.getAccountId());
    			operator = StringUtils.isNotEmpty(bean.getName())?bean.getName():"--";
			}
    		Map<String, String> map = new HashMap<>();
    		map.put("payMethod", orderBean.getPayMethod());
    		map.put("actualMoney", orderBean.getActualChargeAmount()+"");
    		map.put("operator", operator);
    		map.put("time", orderBean.getCtime()+"");
    		map.put("status", orderBean.getStatus()+"");
    		map.put("orderId", orderBean.getOrderId());
    		
    		if(orderBean.getStatus() == 3 || orderBean.getStatus() == 4 || orderBean.getStatus() == 5){
    			map.put("refundMoney", orderBean.getRefundMoney()+"");
    			OrderRefundBean bean = tbOrderRefundDao.getByRefundNo(orderBean.getRefundNo());
    			if(bean != null){
    				long rid = bean.getSubmitUserId() == 0?bean.getApproveUserId():bean.getSubmitUserId();
    				StoreAccountBean accountBean = null;
    				if(rid > 0){
    					accountBean = tbStoreAccountDao.getById(rid);
    				}
    				if(accountBean != null){
    					map.put("refundPersion", accountBean.getName());
    				}
    			}
    		}
    		
    		return CommonResult.success("", map);
    	}catch (Exception e) {
			logger.error("#RefundService.searchByNo# orderId={} transactionId={}",orderId,transactionId,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    
    public CommonResult searchByCode(String code){
    	logger.info("#RefundService.searchByCode# code={}",code);
    	try{
    		List<RefundCode> list = tbRefundCodeDao.getByCode(code);
    		if(CollectionUtils.isEmpty(list)){
    			return CommonResult.defaultError("code出错,重新生成二维码");
    		}
    		RefundCode refundCode = list.get(0);
    		if(StringUtils.isEmpty(refundCode.getOpenId())){
    			return CommonResult.build(-1, "等待用户扫码确认");
    		}
    		
    		Map<String,Object> map = new HashMap<>();
    		map.put("num", 0);
    		UserBean bean = tbUserDao.getUserByOpenId(refundCode.getOpenId(), refundCode.getType());
    		if(bean == null){
    			return CommonResult.success("用户没有付款记录", map);
    		}
    		
    		long curTime = DateUtil.unixTime();
    		long startTime = curTime - 30*24*60*60;
    		List<Integer> statusList = new ArrayList<>();
    		statusList.add(1);
    		statusList.add(3);
    		statusList.add(4);
    		statusList.add(5);
    		List<OrderBean> orderBeans = tbOrderDao.getByUser(bean.getId(), refundCode.getStoreNo(), startTime, curTime, statusList);
    		if(CollectionUtils.isEmpty(orderBeans)){
    			return CommonResult.success("用户没有付款记录", map);
    		}
    		
    		List<Map<String, String>> list2 = new ArrayList<>();
    		for(OrderBean orderBean:orderBeans){
    			Map<String, String> oMap = new HashMap<>();
    			oMap.put("payMethod", orderBean.getPayMethod());
    			oMap.put("time", orderBean.getCtime()+"");
    			oMap.put("status", orderBean.getStatus()+"");
    			oMap.put("actualMoney", orderBean.getActualChargeAmount()+"");
    			oMap.put("orderId", orderBean.getOrderId());
    			oMap.put("refundMoney", orderBean.getRefundMoney()+"");
    			list2.add(oMap);
    		}
    		map.put("num", list2.size());
    		map.put("list", list2);
    		return CommonResult.success("", map);
    	}catch (Exception e) {
			logger.error("#RefundService.searchByCode# code={}",code,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
   
    
    public CommonResult refundMoney(String accountId,String price,String orderId){
    	logger.info("#RefundService.refundMoney# accountId={},price={},orderId={}",accountId,price,orderId);
    	try{
    		OrderBean orderBean = tbOrderDao.getByOrderId(orderId);
    		if(orderBean == null){
    			return CommonResult.defaultError("订单不存在");
			}
    		if(orderBean.getStatus() == 3 || orderBean.getStatus() == 4 || orderBean.getStatus() == 5){
    			return CommonResult.defaultError("重复退款");
    		}
			double v1 = Double.parseDouble(price);
    		CommonResult refundReult = orderService.appRefund(orderBean.getId(),v1, Integer.parseInt(accountId));
    		if(refundReult.getCode() == 2){
    			return CommonResult.defaultError(refundReult.getMsg());
    		}
    		return refundReult;
    	}catch (Exception e) {
        	logger.error("#RefundService.refundMoney# accountId={},price={},orderId={}",accountId,price,orderId,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    public ModelAndView scanCode(String code,HttpServletRequest request){
    	logger.info("#RefundService.scanCode# code={}",code);
    	ModelAndView modelAndView = new ModelAndView();
    	try{
	     	Map<String, List<String>> headers = WebUtils.getHeaders(request);
	        List<String> userAgentList = headers.get("user-agent");
	        //得不到类型，返回错误
	        if(userAgentList == null || userAgentList.size() == 0){
	        	modelAndView.setViewName("error_pay");
	        	return modelAndView;
	        }
	        modelAndView.addObject("code", code);
	        modelAndView.addObject("redirect", "refund/getUserOpenId");
	        String userAgent = userAgentList.get(0);
	        if(userAgent.indexOf("MicroMessenger") >= 0){
	        	List<RefundCode> list = tbRefundCodeDao.getByCode(code);
	        	if(CollectionUtils.isEmpty(list)){
	        		modelAndView.setViewName("error");
	        		return modelAndView;
	        	}
	        	List<StorePayInfo> storePayInfos = tbStorePayInfoDao.getStorePayInfoByNO(list.get(0).getStoreNo());
	        	StorePayInfo storePayInfo = null;
	        	for(StorePayInfo payInfo:storePayInfos){
	        		if("1".equals(payInfo.getPayMethod())){
	        			storePayInfo = payInfo;
	        			break;
	        		}
	        	}
	        	if(storePayInfo == null){
	        		modelAndView.setViewName("error");
	        		return modelAndView;
	        	}
         		modelAndView.addObject("appid",storePayInfo.getAppId());
                modelAndView.setViewName("/open/wx");
	         }else if(userAgent.indexOf("Alipay") >= 0){
        		modelAndView.addObject("appid","2016090701864634");
         		modelAndView.setViewName("/open/zfb");
	         }else {
	             //其它
	             modelAndView.setViewName("error_pay");
	         }
	     }catch (Exception e) {
	     	logger.error("#RefundService.scanCode# error code={}",code,e);
	     	modelAndView.setViewName("error_pay");
		}
	     return modelAndView;
    }
    
    
    public ModelAndView getUserOpenId(String auth,String appId,String state,int type){
    	logger.info("#RefundService.getUserOpenId# auth={},appId={},state={},type={}",auth,appId,state,type);
    	ModelAndView modelAndView = new ModelAndView();
    	try{
    		String openId = "";
    		if(type == 1){
    			 openId = wxService.getOpenId(auth,appId);
    		}else if(type ==2){
    			AliPayInfo aliPayInfo = aliService.getAliPayInfoByAppId(appId);
		        if(aliPayInfo == null){
		        	 modelAndView.setViewName("error");
		             return modelAndView;
		        }
		        openId = aliService.getAliUserId(appId, auth, aliPayInfo.getPrivateKey(), aliPayInfo.getGateWay());
    		}else{
    			modelAndView.setViewName("error");
        		return modelAndView;
    		}
    		tbRefundCodeDao.updateOpenId(openId, state,type);
    		modelAndView.setViewName("app/redirectHode");
    		return modelAndView;
	     }catch (Exception e) {
	     	logger.error("#RefundService.getUserOpenId# auth={},appId={},state={},type={}",auth,appId,state,type,e);
	     	modelAndView.setViewName("error_pay");
		}
	     return modelAndView;
    }
    
    
    
}

