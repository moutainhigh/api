package com.zhsj.api.dao;


import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.util.db.DS;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@DynamicDataSource(DS.DB_FLOW)
public interface TbOrderDao {

    int insertOrder(@Param("bean") OrderBean orderBean);

    int updateOrderByOrderId(@Param("status") int status,
                             @Param("orderId") String orderId);

    int updateOrderByOrderIdIde(@Param("status") int status,
                             @Param("preStatus") int prestatus,
                             @Param("orderId") String orderId);

    OrderBean getByOrderId(@Param("orderId")String orderId);

    OrderBean getById(@Param("id")long id);

    List<OrderBean> getMSAliListByCtime(@Param("id") long id,
                                        @Param("ctime") int ctime,
                                        @Param("pageSize") int pageSize);

    CountDealBean countDealBySaleId(@Param("saleId")long saleId,
                                    @Param("startTime")int startTime,
                                    @Param("endTime")int endTime);

    CountDealBean countDealByStoreNo(@Param("storeNo")String storeNo,
                                    @Param("startTime")int startTime,
                                    @Param("endTime")int endTime);

    CountDealBean countDealByParentNo(@Param("parentNO")String parentNO,
                                     @Param("startTime")int startTime,
                                     @Param("endTime")int endTime);


    CountDealBean countByParam(@Param("storeNo")String storeNo,
                               @Param("parentNo")String parentNo,
                               @Param("startTime")int startTime,
                               @Param("endTime")int endTime,
                               @Param("status") List status,
                               @Param("payMethod") int payMethod);

    List<OrderBean> getByParam(@Param("storeNo")String storeNo,
                               @Param("parentNo")String parentNo,
                               @Param("startTime")int startTime,
                               @Param("endTime")int endTime,
                               @Param("status") List status,
                               @Param("payMethod") int payMethod,
                               @Param("startNo") int startNo,
                               @Param("pageSize")int pageSize);


}
