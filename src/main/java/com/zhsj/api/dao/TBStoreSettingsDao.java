package com.zhsj.api.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreSettingsBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreSettingsDao {
 
	StoreSettingsBean getByStoreNo(@Param("storeNo")String storeNo);
	
}
