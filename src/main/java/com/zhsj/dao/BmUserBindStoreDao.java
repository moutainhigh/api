package com.zhsj.dao;


import com.zhsj.bean.UserBean;
import com.zhsj.bean.UserBindStoreBean;
import com.zhsj.util.db.DS;
import com.zhsj.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface BmUserBindStoreDao {

    UserBindStoreBean getByStoreAndUser(@Param("userId") long userId,@Param("storeNo") String storeNO);

    int save(@Param("userId")long userId,@Param("userType") int userType,@Param("storeNO")String storeNO);

}
