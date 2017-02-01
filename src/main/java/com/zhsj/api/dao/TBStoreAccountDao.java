package com.zhsj.api.dao;


import com.zhsj.api.bean.AccountBean;
import com.zhsj.api.bean.OrgBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBStoreAccountDao {

    StoreAccountBean getByAccount(@Param("account") String account);

    long insert(@Param("bean")StoreAccountBean bean);

    StoreAccountBean getByOpenId(@Param("openId") String openId);

    int updateOpenId(@Param("account")String account,@Param("password")String password,@Param("openId")String openId);

    List<StoreAccountBean> getListByIds(@Param("ids")List<Long> ids);

}
