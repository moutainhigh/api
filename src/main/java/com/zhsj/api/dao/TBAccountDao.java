package com.zhsj.api.dao;


import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBAccountDao {

    int updateOpenId(@Param("account")String account,@Param("password")String password,@Param("openId")String openId);

    int countByOpenId(@Param("openId")String openId);

    List<String> getOpenIdByAccountId(@Param("accountIdList") List<Long> accountIdList);

    AccountBean getByOpenId(@Param("openId") String openId);


}
