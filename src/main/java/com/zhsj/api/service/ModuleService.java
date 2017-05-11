package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.ModuleBean;
import com.zhsj.api.dao.TBModuleBindRoleDao;
import com.zhsj.api.dao.TBModuleDao;
import com.zhsj.api.dao.TBStoreAccountBindRoleDao;
import com.zhsj.api.util.login.LoginUserUtil;

@Service
public class ModuleService {
	 private Logger logger = LoggerFactory.getLogger(ModuleService.class);
	 
	 @Autowired
	 private TBModuleDao tbModuleDao;
	 @Autowired
	 private TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
	 @Autowired
	 private TBModuleBindRoleDao tbModuleBindRoleDao;
	 
	 public List<ModuleBean> getMainMenu(String auth){
		 logger.info("#ModuleService.getMainMenu# auth={}",auth);
		 List<ModuleBean> moduleBeans = new ArrayList();
		 try{
			 LoginUser loginUser = LoginUserUtil.getLoginUser();
			 if(loginUser == null){
				 return moduleBeans;
			 }
			 List<ModuleBean> allModuleBeans = tbModuleDao.getByParentId(0);
			 if(CollectionUtils.isEmpty(allModuleBeans)){
				 return new ArrayList<>();
			 }
			 List<Integer> moduleIds = new ArrayList<>();
			 
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(loginUser.getId());
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 
			 for(ModuleBean bean:allModuleBeans){
				 if(!moduleIds.contains(bean.getId())){
					 bean.setUrl("");
				 }
				 moduleBeans.add(bean);
			 }
		 }catch (Exception e) {
			 logger.error("#ModuleService.getMainMenu# auth={}",auth,e);
		}
		return moduleBeans;
	 }
	 
	 public List<Map<String,Object>> getById(long id,String auth){
		 logger.info("#ModuleService.getById# id={},auth={}",id,auth);
		 List<Map<String,Object>> list = new ArrayList<>();
		 try{
			 LoginUser loginUser = LoginUserUtil.getLoginUser();
			 if(loginUser == null){
				 return list;
			 }
			 List<Integer> moduleIds = new ArrayList<>();
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(loginUser.getId());
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 
			 List<ModuleBean> moduleBeans = tbModuleDao.getByParentId(id);
			 if(CollectionUtils.isEmpty(moduleBeans)){
				 return new ArrayList<>();
			 }
			 
			 for(ModuleBean bean:moduleBeans){
				 Map<String, Object> map = new HashMap<String, Object>();
				 list.add(map);
				 if(!moduleIds.contains(bean.getId())){
					 bean.setUrl("");
				 }
				 map.put("title", bean);
				 List<ModuleBean> beans = tbModuleDao.getByParentId(bean.getId());
				 if(CollectionUtils.isEmpty(beans)){
					 map.put("module", new ArrayList<>());
					 continue;
				 }
				 
				 List<ModuleBean> beanList = new ArrayList<>();
				 for(ModuleBean be:beans){
					 if(!moduleIds.contains(be.getId())){
						 be.setUrl("");
					 }
					 beanList.add(be);
				 }
				 map.put("module", beanList);
			 }
		 }catch (Exception e) {
			 logger.error("#ModuleService.getById# id={},auth={}",id,auth,e);
		 }
		 return list;
		 
	 }
	 
	 public boolean authByURI(String uri,String auth){
		 logger.info("#ModuleService.authByURI# uri={},auth={}",uri,auth);
		 boolean result = false;
		 try{
			 LoginUser loginUser = LoginUserUtil.getLoginUser();
			 if(loginUser == null){
				 return result;
			 }
			 ModuleBean moduleBean = tbModuleDao.getByURI(uri);
			 if(moduleBean== null){
				 return result;
			 }
			 
			 List<Integer> moduleIds = new ArrayList<>();
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(loginUser.getId());
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 
			 result = moduleIds.contains(moduleBean.getId());
		 }catch (Exception e) {
			 logger.error("#ModuleService.authByURI# uri={},auth={}",uri,auth,e);
		}
		return result;
	 }

}
