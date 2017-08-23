package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.zhsj.api.bean.CityCodeBean;
import com.zhsj.api.bean.BusinessTypeBean;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.VersionInfo;
import com.zhsj.api.bean.jpush.PaySuccessBean;
import com.zhsj.api.bean.result.CheckUpdateResult;
import com.zhsj.api.dao.TBCityCodeDao;
import com.zhsj.api.dao.TBBusinessTypeDao;
import com.zhsj.api.dao.TBVersionInfoDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.MtConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class BaseService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    TBCityCodeDao tbCityCodeDao;
    @Autowired
    TBBusinessTypeDao tbBusinessTypeDao;
    @Autowired
    private TBVersionInfoDao tbVersionInfoDao;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreAccountService storeAccountService;
    @Autowired
    OrderService orderService;

    public List<CityCodeBean> getCityCode(String cityCode){
        logger.info("#BaseService.getCityCode# cityCode={}",cityCode);
        List<CityCodeBean> cityCodeBeanList= new ArrayList<>();
        try {
            cityCodeBeanList = tbCityCodeDao.getCityCode(cityCode);
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

        return cityCodeBeanList;
    }
    
    
    public List<BusinessTypeBean> getBusinessTypeListByParentId(int id){
    	logger.info("#BaseService.getMgtListByParentId# id={}",id);
    	List<BusinessTypeBean> businessTypeBeanList = new ArrayList<>();
    	try {
    		businessTypeBeanList = tbBusinessTypeDao.getListById(id);
		} catch (Exception e) {
			logger.error("AbcService e={}",e.getMessage(),e);
		}
    	return businessTypeBeanList;
    }
    
    public int addBusinessType(BusinessTypeBean businessTypeBean){
    	logger.info("#BaseService.addBusinessType #businessTypeBean",businessTypeBean);
    	int index = 0;
    	try {
			index = tbBusinessTypeDao.insert(businessTypeBean);
		} catch (Exception e) {
			logger.error("AbcService e={}",e.getMessage(),e);
		}
    	return index;
    }

    public CommonResult checkUpdate(String version,String os,String auth){
    	logger.info("#BaseService.checkUpdate# version={},os={},auth={}",version,os,auth);
    	CheckUpdateResult result = new CheckUpdateResult();
    	result.setVersion(version);
    	try{
    		VersionInfo versionInfo = tbVersionInfoDao.getByVersion(version,os);
    		if(versionInfo == null){
    			return CommonResult.defaultError("版本号不存在");
    		}
    		result.setDownLoadUrl(versionInfo.getNewUrl());
    		result.setNewVersion(versionInfo.getNewVersion());
    		if(version.equals(versionInfo.getNewVersion())){
    			result.setIsUpdate(0);
    			return CommonResult.success("", result);
    		}
    		result.setIsUpdate(1);
    		result.setIsForce(versionInfo.getIsForce());
    		return CommonResult.success("", result);
    		
    	}catch (Exception e) {
    		logger.error("#BaseService.checkUpdate# version={},auth={}",version,auth,e);
		}
    	return CommonResult.defaultError("系统出错");
    }

    
    public CommonResult microPay(String storeNo,String userId,String price,String authCode, String auth){
    	logger.info("#BaseService.microPay# storeNo={},userId={},price={},authCode={},auth={}",
    			storeNo,userId,price,authCode,auth);
    	try {
			String uri = MtConfig.getProperty("PAY_URL", "")+"microPay";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("price", price);
			map.put("accountId", userId);
			map.put("storeNo", storeNo);
			map.put("authCode", authCode);
			map.put("auth", "");
			String result = HttpClient.sendPost(uri, map);
			CommonResult commonResult = JSON.parseObject(result, CommonResult.class);
			if(commonResult.getCode() != 0){
				return commonResult;
			}
			OrderBean bean = orderService.getByOrderId(commonResult.getData().toString());
			
			PaySuccessBean psBean = orderService.getPaySuccessBean(bean);
			return commonResult.success("", psBean);
    	} catch (Exception e) {
			logger.error("#BaseService.microPay# storeNo={},userId={},price={},authCode={},auth={}",
	    			storeNo,userId,price,authCode,auth,e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    public CommonResult createPayCode(String storeNo,String userId,String price, String auth){
    	logger.info("#BaseService.createPayCode# storeNo={},userId={},price={},auth={}",storeNo,userId,price,auth);
    	try{
    		StoreBean storeBean = storeService.getStoreByNO(storeNo);
    		if(storeBean == null){
    			return CommonResult.defaultError("商家编号不存在");
    		}
    		String no = storeAccountService.getStoreNo(Long.parseLong(userId));
    		if(StringUtils.isEmpty(no)){
    			return CommonResult.defaultError("登录用户错误");
    		}
    		if(!no.equals(storeBean.getStoreNo())){
    			return CommonResult.defaultError("登录用户没有权限");
    		}
    		String uri = MtConfig.getProperty("PAY_URL", "");
    		Map<String,String> map = new HashMap<>();
	        map.put("url", uri+"scanCode?no="+storeNo+"&userId="+userId+"&price="+price);
	        map.put("code", userId+price);
	        return  CommonResult.success("",map);
    	}catch (Exception e) {
    		logger.error("#BaseService.createPayCode# storeNo={},userId={},price={},auth={}",storeNo,userId,price,auth,e);
		}
    	return CommonResult.defaultError("系统出错了");
    }


    public static void main(String[] args){
        BaseService abcService = new BaseService();
//        System.out.println(abcService.abc());
//        String token = "FnGbTVWamObKS8FdcRofrtLtK9Hv51PPbEGywRKx81iDlsJxQt0HlPUtzXe2fse9n2RiYhTv8ky-6vneefTYID_f9cMtVpx0GA4EhjzN72Nu35adMU-Oxx5sdONSRCqrMYZfAGAPBC";
//        System.out.println(abcService.getUserInfo(token, "oFvcxwdPHqk7HaHqSxrdSMg5lAKk"));
    }
}
