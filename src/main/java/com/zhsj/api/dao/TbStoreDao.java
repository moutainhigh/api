package com.zhsj.api.dao;


import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.bean.StoreBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStoreDao {

    StoreBean getStoreByNo(@Param("storeNo") String storeNo);

    int insert(@Param("bean") StoreBean bean);

    int updateAddress(@Param("address") String address,@Param("cityCode")int cityCode,@Param("storeNo")String storeNo);

}
