package com.zhsj.api.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.dao.TBStoreSignDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.dao.TbStoreNoDao;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;

@Service
public class StoreAccountService {
	
	private Logger logger = LoggerFactory.getLogger(StoreAccountService.class);
	
	@Autowired
	private TBStoreAccountDao tbStoreAccountDao;
	@Autowired
	private TBStoreSignDao tbStoreSignDao;
	@Autowired
	private TbStoreDao tbStoreDao;
	@Autowired
	private TBStoreBindAccountDao tbStoreBindAccountDao;
	
	public CommonResult signCashier(String account,String passwd,String lat,String lon,String auth){
		logger.info("#StoreAccountService.signCashier# account={},passwd={},lat={},lon={},auth={}",
				account,passwd,lat,lon,auth);
		try{
			StoreAccountBean storeAccountBean =  tbStoreAccountDao.getByAccount(account);
			if(storeAccountBean == null || !passwd.equals(storeAccountBean.getPassword())){
				return CommonResult.defaultError("账号或密码不正确");
			}
			tbStoreAccountDao.updateSignStatus(storeAccountBean.getId(), 1);
			tbStoreSignDao.insert(storeAccountBean.getId(), lat, lon, 1);
			
			String storeNo = tbStoreBindAccountDao.getStoreNoByAccountId(storeAccountBean.getId());
			StoreBean storeBean = tbStoreDao.getStoreByNo(storeNo);
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("time", DateUtil.unixTime());
			map.put("store", storeBean);
			map.put("user", storeAccountBean);
			return CommonResult.success("签到成功",map);
		}catch(Exception e){
			logger.error("#StoreAccountService.signCashier# account={},passwd={},lat={},lon={},auth={}",
					account,passwd,lat,lon,auth,e);
			return CommonResult.defaultError("系统错误");
		}
	}	
	
}