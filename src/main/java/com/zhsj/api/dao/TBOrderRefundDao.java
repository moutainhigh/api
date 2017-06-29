package com.zhsj.api.dao;


import java.util.List;

import com.zhsj.api.bean.OrderRefundBean;
import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.util.db.DS;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_FLOW)
public interface TBOrderRefundDao {
	
	List<OrderRefundBean> getByNo(@Param("orderNo")String orderNo);
	
	int updateStatusByNo(@Param("reOrderNo")String reOrderNo,
					@Param("status")int status);
    
	int insert(OrderRefundBean orderRefundBean);
	
	OrderRefundBean getByRefundNo(@Param("refundNo")String refundNo);
	
	int updateStatusAndOrderNo(@Param("reOrderNo")String reOrderNo,
							   @Param("preOrderNo")String preOrderNo,
							   @Param("status")int status);

}
