package com.zhsj.dao;


import com.zhsj.bean.StoreBean;
import com.zhsj.util.db.DS;
import com.zhsj.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface BmStoreBindOrgDao {

    Long getOrgIdByStoreNO(@Param("storeNo") String storeNo);



}
