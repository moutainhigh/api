package com.zhsj.api.dao;


import com.zhsj.api.bean.OrgBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBOrgDao {

    OrgBean getById(@Param("id")long id);


}
