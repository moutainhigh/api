package com.zhsj.api.dao;


import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBAccountDao {

    int updateOpenId(String account,String password,String openId);

    int countByOpenId(String openId);



}
