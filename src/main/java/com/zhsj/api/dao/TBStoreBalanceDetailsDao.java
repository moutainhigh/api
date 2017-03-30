package com.zhsj.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreBalanceDetailBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreBalanceDetailsDao {

	int insert(@Param("bean")StoreBalanceDetailBean storeBalanceDetailBean);
	
	List<StoreBalanceDetailBean> getListByStoreNo(@Param("storeNo")String storeNo,@Param("type")int type,
			@Param("start")int start,@Param("end")int end);
	
	int update(StoreBalanceDetailBean storeBalanceDetailBean);
	
	List<String> getlistByStatus();
	
	StoreBalanceDetailBean getByPartnerTradeNo(@Param("partnerTradeNo")String partnerTradeNo);
}
