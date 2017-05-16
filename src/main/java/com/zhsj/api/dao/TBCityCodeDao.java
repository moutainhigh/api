package com.zhsj.api.dao;


import com.zhsj.api.bean.CityCodeBean;
import com.zhsj.api.bean.OrgBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBCityCodeDao {

    List<CityCodeBean> getCityCode(@Param("cityCode") String cityCode);
    
    List<CityCodeBean> getCityCodes(@Param("cityCodes")List<String> cityCodes);
    
}
