package com.zhsj.api.dao;


import com.zhsj.api.bean.DiscountBean;
import com.zhsj.api.bean.DiscountRuleBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBDiscountRuleDao {

    List<DiscountRuleBean> getByDisId(@Param("disId") long disId);

    int insert(@Param("beans")List<DiscountRuleBean> beans);
    
    int delByDiscountId(@Param("discountId")long discountid);


}
