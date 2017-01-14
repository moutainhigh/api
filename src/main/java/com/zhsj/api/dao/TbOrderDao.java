package com.zhsj.api.dao;


import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.util.db.DS;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

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

    List<OrderBean> getMSAliListByCtime(@Param("id") long id,
                                        @Param("ctime") int ctime,
                                        @Param("pageSize") int pageSize);

}
