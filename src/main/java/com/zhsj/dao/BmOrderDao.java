package com.zhsj.dao;


import com.zhsj.bean.OrderBean;
import com.zhsj.bean.StoreBean;
import com.zhsj.bean.StorePayInfo;
import com.zhsj.util.db.DS;
import com.zhsj.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_FLOW)
public interface BmOrderDao {

    int insertOrder(@Param("bean") OrderBean orderBean);

    int updateOrderByOrderId(@Param("status") int status,
                             @Param("orderId") String orderId);

}
