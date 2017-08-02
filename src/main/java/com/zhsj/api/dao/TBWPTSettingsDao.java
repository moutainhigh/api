package com.zhsj.api.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.zhsj.api.bean.PWTSettings;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBWPTSettingsDao {
 
	List<PWTSettings> getByEn(@Param("isTest")int isTest);
	
}
