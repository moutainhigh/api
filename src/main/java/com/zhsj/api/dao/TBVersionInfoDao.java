package com.zhsj.api.dao;

import com.zhsj.api.bean.VersionInfo;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBVersionInfoDao {

	VersionInfo getByVersion(@Param("version")String version,
							 @Param("os")String os);


}
