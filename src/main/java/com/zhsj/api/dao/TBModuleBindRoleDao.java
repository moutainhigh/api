package com.zhsj.api.dao;


import java.util.List;

import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBModuleBindRoleDao {

    List<Integer> getModuleIdByRoleIds(@Param("roleIds")List<Integer> roleIds);
    
    List<Integer> getRoleIdBymoduleId(@Param("moduleId")long moduleId);

}
