package com.zhsj.api.dao;


import com.zhsj.api.bean.DiscountBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBDiscountDao {

    DiscountBean getById(@Param("id")long id);

    List<DiscountBean> getByIds(@Param("ids")List ids);

    int insert(@Param("bean")DiscountBean bean);
    
    int updateValidById(@Param("id")long id);
    
    int updateStatusById(@Param("id")long id);
}
