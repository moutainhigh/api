package com.zhsj.api.dao;

import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBSendJGMsgDao {
	
    int save(@Param("type")int type,
    		 @Param("msgId")String msgId,
    		 @Param("orderId")String orderId,
    		 @Param("msg")String msg);


}
