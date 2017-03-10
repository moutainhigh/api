package com.zhsj.api.dao;


import java.util.List;
import java.util.Map;

import com.zhsj.api.bean.UserBindStoreBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbUserBindStoreDao {

    UserBindStoreBean getByStoreAndUser(@Param("userId") long userId,@Param("storeNo") String storeNO);

    int save(@Param("userId")long userId,@Param("userType") int userType,
             @Param("storeNO")String storeNO,@Param("parentNo")String parentNO);

    int updateTimeById(@Param("id")long id);

    int countByStoreNo(@Param("storeNo")String storeNo,
                       @Param("startTime")int startTime,
                       @Param("endTime")int endTime);

    int countByParentNo(@Param("parentNO")String parentNO,
                        @Param("startTime")int startTime,
                        @Param("endTime")int endTime);
    //xlc
    int getMemberCountByParam(Map<String, Object> paramMap);
    
    List<UserBindStoreBean> getListByStoreNo(@Param("storeNo")String storeNo,
    		                                 @Param("startTime")int startTime,
    		                                 @Param("endTime")int endTime);
}
