package com.zhsj.api.dao;

import com.zhsj.api.bean.StoreAccountSignBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreSignDao {

	int insert(@Param("accountId")long accountId,
				@Param("lat")String lat,
				@Param("lon")String lon,
				@Param("status")int status,
				@Param("storeNo")String storeNo,
				@Param("imei")String imei);
	
	StoreAccountSignBean getLastStoreSign(@Param("accountId")long accountId,
					 @Param("status")int status);
	
}
