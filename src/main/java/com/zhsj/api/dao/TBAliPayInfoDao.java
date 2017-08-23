package com.zhsj.api.dao;


import com.zhsj.api.bean.AliPayInfo;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBAliPayInfoDao {

	AliPayInfo getByAppId(@Param("appId")String appId);

}
