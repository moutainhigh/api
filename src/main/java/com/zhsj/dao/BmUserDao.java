package com.zhsj.dao;


import com.zhsj.bean.UserBean;
import com.zhsj.util.db.DS;
import com.zhsj.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface BmUserDao {

    UserBean getUserByOpenId(@Param("openId")String openId,@Param("type")int type);

    Long insertOpenId(@Param("userBean")UserBean userBean);


}
