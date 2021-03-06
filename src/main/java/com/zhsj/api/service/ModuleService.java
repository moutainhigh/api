package com.zhsj.api.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
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
				 if(!moduleIds.contains((int)bean.getId())){
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
					 Integer moduleId = (int)be.getId();
					 if(!moduleIds.contains(moduleId)){
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
	 
	 public boolean authByURI(String uri){
		 logger.info("#ModuleService.authByURI# uri={}",uri);
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
			 
			 result = moduleIds.contains((int)moduleBean.getId());
		 }catch (Exception e) {
			 logger.error("#ModuleService.authByURI# uri={}",uri,e);
		}
		return result;
	 }
	 
	 public CommonResult getAppModule(String storeNo,String os,String rate,String auth,HttpServletRequest request){
		 logger.info("#ModuleService.getAppModule# storeNo={},os={},rate={},auth={}",storeNo,os,rate,auth);
		 try{
			 auth = URLDecoder.decode(auth, "utf-8");
			 String[] args = auth.split(",");
			 List<ModuleBean> allModuleBeans = tbModuleDao.getByParentIdAndType(0, 2);
			 String uri = request.getRequestURL().toString();
			 uri = uri.replace(request.getRequestURI(), "")+ request.getContextPath();
			 
			 List<Integer> moduleIds = new ArrayList<>();
			 List<Integer> roleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(Long.parseLong(args[1]));
			 if(!CollectionUtils.isEmpty(roleIds)){
				 moduleIds = tbModuleBindRoleDao.getModuleIdByRoleIds(roleIds);
				 moduleIds = CollectionUtils.isEmpty(moduleIds)?new ArrayList<Integer>():moduleIds;
			 }
			 List<ModuleBean> resultList = new ArrayList<>();
			 for(ModuleBean bean:allModuleBeans){
				 if(!moduleIds.contains((int)bean.getId())){
					continue;
				 }
				 bean.setIconUrl(uri+bean.getIconUrl());
				 bean.setUrl(uri+bean.getUrl()+"?storeNo="+args[0]+"&accountId="+args[1]+"&v="+DateUtil.unixTime());
				 resultList.add(bean);
			 }
			 return CommonResult.success("", resultList);
		 }catch (Exception e) {
			 logger.error("#ModuleService.getAppModule# storeNo={},os={},auth={}",storeNo,os,auth,e);
		}
		return CommonResult.defaultError("出错了");
	 }

}
