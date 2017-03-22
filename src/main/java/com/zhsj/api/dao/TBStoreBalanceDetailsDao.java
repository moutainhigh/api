package com.zhsj.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhsj.api.bean.StoreBalanceDetailBean;

public interface TBStoreBalanceDetailsDao {

	int insert(@Param("bean")StoreBalanceDetailBean storeBalanceDetailBean);
	
	List<StoreBalanceDetailBean> getListByStoreNo(@Param("storeNo")String storeNo,
			@Param("start")int start,@Param("end")int end);
	
	int update(StoreBalanceDetailBean storeBalanceDetailBean);
}
