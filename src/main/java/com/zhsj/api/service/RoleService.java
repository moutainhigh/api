package com.zhsj.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhsj.api.bean.RoleBean;
import com.zhsj.api.dao.TBRoleDao;

@Service
public class RoleService {

	private Logger logger = LoggerFactory.getLogger(RoleService.class);
	
	@Autowired
	private TBRoleDao tbRoleDao;
	
	public Object getList(){
		logger.info("#getList#");
		try {
			List<RoleBean> list = tbRoleDao.getListByTypeEq2();
			return list;
		} catch (Exception e) {
			logger.error("#getList#", e);
		}
		return null;
	}
}
