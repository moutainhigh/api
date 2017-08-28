package com.zhsj.api.dao;


import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreBindDeviceDao {

	int updateOnlineByStoreNo(@Param("online")int online,
							  @Param("storeNo")String storeNo,
							  @Param("imei")String imei);
	
	int updateOnlineByImei(@Param("online")int online,
			  			   @Param("imei")String imei);
	

}
