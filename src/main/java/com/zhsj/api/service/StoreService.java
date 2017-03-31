package com.zhsj.api.service;

import java.util.List;

import com.zhsj.api.bean.MinMaxBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.dao.TbStoreDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class StoreService {
    Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private TbStoreDao tbStoreDao;

    public StoreBean getStoreByNO(String storeNo){
        return tbStoreDao.getStoreByNo(storeNo);
    }
    
    public List<StoreBean> getStoreByLimitId(int minId,int maxId){
    	return tbStoreDao.getStoreByLimitId(minId, maxId);
    }

    public MinMaxBean getMaxMin(){
    	return tbStoreDao.getMaxMin();
    }
}
