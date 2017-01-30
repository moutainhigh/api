package com.zhsj.api.dao;


import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStoreNoDao {

    int updateStatusByStoreNo (@Param("saleId")long saleId,@Param("storeNO")String storeNO);

    int updateStatusByStoreNoAndSaleId(@Param("saleId")long saleId,@Param("storeNO")String storeNO);



}
