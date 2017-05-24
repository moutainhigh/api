package com.zhsj.api.service;

import com.alibaba.fastjson.JSON;
import com.sun.tools.classfile.InnerClasses_attribute.Info;
import com.zhsj.api.bean.CityCodeBean;
import com.zhsj.api.bean.BusinessTypeBean;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.bean.VersionInfo;
import com.zhsj.api.bean.jpush.PaySuccessBean;
import com.zhsj.api.bean.result.CheckUpdateResult;
import com.zhsj.api.dao.TBCityCodeDao;
import com.zhsj.api.dao.TBBusinessTypeDao;
import com.zhsj.api.dao.TBVersionInfoDao;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.HttpClient;
import com.zhsj.api.util.SSLUtil;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private JPushService jPushService;

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

    public CommonResult checkUpdate(String version,String auth){
    	logger.info("#BaseService.checkUpdate# version={},auth={}",version,auth);
    	CheckUpdateResult result = new CheckUpdateResult();
    	result.setVersion(version);
    	try{
    		VersionInfo versionInfo = tbVersionInfoDao.getByVersion(version);
    		if(versionInfo == null){
    			return CommonResult.defaultError("版本号不存在");
    		}
    		if(version.equals(versionInfo.getNewVersion())){
    			result.setIsUpdate(0);
    			return CommonResult.success("", result);
    		}
    		result.setIsUpdate(1);
    		result.setIsForce(versionInfo.getIsForce());
    		result.setDownLoadUrl(versionInfo.getNewUrl());
    		result.setNewVersion(versionInfo.getNewVersion());
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
			String uri = "http://wwt.bj37du.com/pay/microPay";
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
			return jPushService.sendSuccessMsg(commonResult.getData().toString());
    	} catch (Exception e) {
			logger.error("#BaseService.microPay# storeNo={},userId={},price={},authCode={},auth={}",
	    			storeNo,userId,price,authCode,auth,e);
		}
    	return CommonResult.defaultError("系统异常");
    }


    public static void main(String[] args){
        BaseService abcService = new BaseService();
//        System.out.println(abcService.abc());
//        String token = "FnGbTVWamObKS8FdcRofrtLtK9Hv51PPbEGywRKx81iDlsJxQt0HlPUtzXe2fse9n2RiYhTv8ky-6vneefTYID_f9cMtVpx0GA4EhjzN72Nu35adMU-Oxx5sdONSRCqrMYZfAGAPBC";
//        System.out.println(abcService.getUserInfo(token, "oFvcxwdPHqk7HaHqSxrdSMg5lAKk"));
    }
}
