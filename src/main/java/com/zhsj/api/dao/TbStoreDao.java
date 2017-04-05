package com.zhsj.api.dao;


import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.bean.MinMaxBean;
import com.zhsj.api.bean.StoreBean;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStoreDao {

    StoreBean getStoreByNo(@Param("storeNo") String storeNo);

    int insert(@Param("bean") StoreBean bean);

    int updateAddress(@Param("address") String address,@Param("cityCode")int cityCode,@Param("storeNo")String storeNo);

    List<StoreBean> getListByParentNo(@Param("parentNo") String parentNo);

    int updateStatus(@Param("status") int status,@Param("storeNo")String storeNo);

    int updateAppId(@Param("appId") String  appId,@Param("storeNo")String storeNo);
    
    void updatePrice(@Param("price") double price,@Param("storeNo")String storeNo);

    List<StoreBean> getListByNos(@Param("nos")List<String> nos);

    double getPriceByStoreNo(@Param("storeNo")String storeNo);
    
    int updatePriceByStoreNo(@Param("amount")double amount,@Param("storeNo")String storeNo,
    		@Param("price")double price,@Param("type")int type,@Param("isPrice")int isPrice);
    
    List<StoreBean> getStoreByLimitId(@Param("minId")int minId,
    								  @Param("maxId")int maxId);
    
    MinMaxBean getMaxMin();
    
}
