package com.zhsj.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreTransactionSummary;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreTransactionSummaryDao {

	List<StoreTransactionSummary> getMoneyByListAndTime(@Param("storeNos")List<String> storeNos, @Param("startTime")int startTime, @Param("endTime")int endTime);
	
	
	List<StoreTransactionSummary> getMoneyByTime(@Param("storeNo")String storeNo, @Param("startTime")int startTime, @Param("endTime")int endTime);
	
	List<StoreTransactionSummary> getCountByListAndTime(@Param("storeNos")List<String> storeNos, @Param("startTime")int startTime, @Param("endTime")int endTime);
}
