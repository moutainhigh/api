package com.zhsj.api.dao;


import com.zhsj.api.bean.UserBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbUserDao {

    UserBean getUserByOpenId(@Param("openId")String openId,@Param("type")int type);

    Long insertOpenId(@Param("userBean")UserBean userBean);


}
