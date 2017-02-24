package com.zhsj.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.BusinessTypeBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBBusinessTypeDao {

	List<BusinessTypeBean> getListById(@Param("id") int id);
	
	int insert(BusinessTypeBean businessTypeBean);
}
