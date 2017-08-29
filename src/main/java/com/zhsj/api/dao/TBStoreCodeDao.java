package com.zhsj.api.dao;


import java.util.List;
import com.zhsj.api.bean.StoreCodeBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreCodeDao {

    List<StoreCodeBean> getByStoreCode(@Param("storeNo")String storeNo,
    					               @Param("code")String code);


}
