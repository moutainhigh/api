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
    
}