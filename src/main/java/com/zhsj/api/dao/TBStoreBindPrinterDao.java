package com.zhsj.api.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreBindPrinterBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreBindPrinterDao {

	int insert(StoreBindPrinterBean storeBindPrinterBean);
	
	StoreBindPrinterBean getByStoreNo(@Param("storeNo")String storeNo);
}
