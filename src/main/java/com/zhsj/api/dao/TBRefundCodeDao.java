package com.zhsj.api.dao;


import java.util.List;

import com.zhsj.api.bean.RefundCode;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBRefundCodeDao {

    int insert(@Param("code")String code,
               @Param("storeNo")String storeNo);

    
    int updateOpenId(@Param("openId")String openId,
    				 @Param("code")String code);
    
    List<RefundCode> getByCode(@Param("code")String code);

}
