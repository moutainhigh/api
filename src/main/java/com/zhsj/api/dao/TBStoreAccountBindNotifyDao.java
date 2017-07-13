package com.zhsj.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreAccountBindNotifyBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreAccountBindNotifyDao {

	int insert();
	
	int insertList(@Param("sabnbs")List<StoreAccountBindNotifyBean> storeAccountBindNotifyBeans);
	
	int deleteList(@Param("notifyIds")List<Integer> notifyIds, @Param("accountId")long accountId);
    
	List<Integer> getNotifyIdByAccountId(@Param("accountId")long id);
}
