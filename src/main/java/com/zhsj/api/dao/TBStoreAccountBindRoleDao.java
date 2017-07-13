package com.zhsj.api.dao;


import com.zhsj.api.bean.StoreAccountBindRoleBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreAccountBindRoleDao {

    List<Long> filterAccountIdByRole(@Param("roleId") long roleId,@Param("accountIdList")List<Long> accountIdList);

    int insert(@Param("accountId") long accountId,@Param("role_id")long role_id);

    int updateByAccountIdAndRoleId(@Param("roleId")long roleId,@Param("accountId") long accountId);

    List<Integer> getRoleIdByAccountId(@Param("accountId")long accountId);

    int insertList(@Param("sabrbs")List<StoreAccountBindRoleBean> storeAccountBindRoleBeans);
    
    int deleteAll(@Param("roleIds")List<Integer> roleIds, @Param("accountId")long accountId);
}
