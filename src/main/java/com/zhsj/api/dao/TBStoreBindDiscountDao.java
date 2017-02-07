package com.zhsj.api.dao;


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

    int insert(@Param("storeNo")String storeNo,
               @Param("discountId")long discountId,
               @Param("startTime")int startTime,
               @Param("endTime")int endTime,
               @Param("parentStoreNo")String parentStoreNo);

    List<Long> getDiscountIdByStoreNo(@Param("storeNo")String storeNo);

    List<Long> getDiscountPage(@Param("storeNo")String store,
                               @Param("parentStoreNo")String parentStoreNo,
                               @Param("status")int status,
                               @Param("startNo")int startNo,
                               @Param("pageSize")int pageSize);

    long countDiscountPage (@Param("storeNo")String store,
                            @Param("parentStoreNo")String parentStoreNo,
                            @Param("status")int status);

    int updateByStoreNoAndDisId(@Param("storeNo")String storeNo,
                     @Param("discountId")long discountId,
                     @Param("startTime")int startTime,
                     @Param("endTime")int endTime,
                     @Param("valid") int valid);


}
