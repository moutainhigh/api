package com.zhsj.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhsj.api.bean.NotifyBean;
import com.zhsj.api.dao.TBNotifyDao;

@Service
public class NotifyService {

	private Logger logger = LoggerFactory.getLogger(NotifyService.class);
	
	@Autowired
	private TBNotifyDao tbNotifyDao;
	
	public Object getList(){
		logger.info("#getList#");
		try {
			List<NotifyBean> list = tbNotifyDao.getList();
			return list;
		} catch (Exception e) {
			logger.error("#getList#", e);
		}
		return null;
	}
	
}
