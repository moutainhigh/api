package com.zhsj.api.dao;


import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStoreBindOrgDao {

    Long getOrgIdByStoreNO(@Param("storeNo") String storeNo);

    int insert(@Param("storeNo")String storeNo,@Param("orgId")long orgId,@Param("orgIds")String orgIds);

}
