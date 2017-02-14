package com.zhsj.api.dao;


import com.zhsj.api.bean.DiscountBean;
import com.zhsj.api.bean.UserBean;
import com.zhsj.api.bean.WeixinUserBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreBindDiscountDao {

    List<Long> getDiscountIdByStoreNo(@Param("storeNo")String storeNo);

    List<Long> getDiscountPage(@Param("storeNo")String store,
                               @Param("parentStoreNo")String parentStoreNo,
                               @Param("status")int status,
                               @Param("startNo")int startNo,
                               @Param("pageSize")int pageSize);

    long countDiscountPage (@Param("storeNo")String store,
                            @Param("parentStoreNo")String parentStoreNo,
                            @Param("status")int status);

    List<DiscountBean> getByParam(@Param("storeNoList")List<String> storeNos,
                                @Param("startTime")int startTime,
                                @Param("endTime")int endTime);


    int updateByStoreNoAndDisId(@Param("storeNo")String storeNo,
                     @Param("discountId")long discountId,
                     @Param("startTime")int startTime,
                     @Param("endTime")int endTime,
                     @Param("valid") int valid);

    int insert(@Param("storeNos")List<String> storeNos,
               @Param("discountId")long discountId,
               @Param("startTime")int startTime,
               @Param("endTime")int endTime,
               @Param("parentStoreNo")String parentStoreNo);

    List<String> getStoreNoByDiscountId(@Param("discountId")long discountId);
    
    int updateValidByDiscountId(@Param("discountId")long discountId);

    int updateStatusByDiscountId(@Param("discountId")long discountId);
}
