package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.ModuleBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.dao.TBAccountBindRoleDao;
import com.zhsj.api.dao.TBModuleBindRoleDao;
import com.zhsj.api.dao.TBModuleDao;
import com.zhsj.api.dao.TBStoreAccountBindRoleDao;
import com.zhsj.api.util.login.LoginUserUtil;

@Service
public class PermissionService {
	private Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	@Autowired
	private TBModuleDao tbModuleDao;
	@Autowired
	private TBModuleBindRoleDao tbModuleBindRoleDao;
	@Autowired
	private TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
	@Autowired
	private TBAccountBindRoleDao tbAccountBindRoleDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ShopService shopService;
	
	
	public boolean hasPermission(String uri,String auth){
		logger.info("#PermissionService.hasPermission# uri={},auth={}",uri,auth);
		boolean result = false;
		try{
			ModuleBean moduleBean = tbModuleDao.getByURI(uri);
			if(moduleBean == null){
				return false;
			}
			List<Integer> roleIdList = tbModuleBindRoleDao.getRoleIdBymoduleId(moduleBean.getId());
			if(CollectionUtils.isEmpty(roleIdList)){
				return false;
			}
			
			List<Integer> userRoList = new ArrayList<>();
			String type = auth.substring(0, 2);
	        if("11".equals(type)){
	            //为openID、管理员
	            String openId = auth.substring(2);
	            AccountBean accountBean = accountService.getByOpenId(openId);
	            if (accountBean == null){
	                return false;
	            }
	            userRoList = tbAccountBindRoleDao.getRoleIdByAccountId(accountBean.getId());
	        }
	        if("21".equals(type)){
	            //为openID,商家
	            String openId = auth.substring(2);
	            StoreAccountBean storeAccountBean = shopService.getStoreAccountByOpenId(openId);
	            if (storeAccountBean == null){
	                return false;
	            }
	            userRoList = tbStoreAccountBindRoleDao.getRoleIdByAccountId(storeAccountBean.getId());
	        }
			
			if(CollectionUtils.isEmpty(userRoList)){
				return false;
			}
			userRoList.retainAll(roleIdList);
			if(!CollectionUtils.isEmpty(userRoList)){
				return true;
			}
			
		}catch(Exception e){
			logger.error("PermissionService.hasPermission# uri={}",uri,e);
		}
		return result;
	}
}
