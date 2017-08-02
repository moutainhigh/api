package com.zhsj.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhsj.api.bean.StoreItemBean;
import com.zhsj.api.dao.TBStoreItemDao;

@Service
public class StoreItemService {
	
	Logger logger = LoggerFactory.getLogger(StoreItemService.class);
	
	@Autowired
	private TBStoreItemDao tbStoreItemDao;

	public List<StoreItemBean> getItems(String storeNo){
		logger.info("#StoreItemService.getItems# storeNo={}",storeNo);
		return tbStoreItemDao.getItems(storeNo);
	}
    
}
