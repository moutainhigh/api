package com.zhsj.api.dao;


import com.zhsj.api.bean.StoreItemBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreItemDao {

    List<StoreItemBean> getItems(@Param("storeNo") String storeNo);
    
    
}
