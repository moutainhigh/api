package com.zhsj.api.dao;


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
    
    StoreAccountBean getOneByAccount(@Param("account") String account);

    long insert(@Param("bean")StoreAccountBean bean);

    StoreAccountBean getByOpenId(@Param("openId") String openId);

    int updateOpenId(@Param("account")String account,
                     @Param("password")String password,
                     @Param("openId")String openId,
                     @Param("name")String name,
                     @Param("headImg")String headImg);
    
    int delById( @Param("id")long id);

    List<StoreAccountBean> getListByIds(@Param("ids")List<Long> ids);
    
    List<StoreAccountBean> getSaListByIds(@Param("ids")List<Long> ids);

    int updatePassword(@Param("account")String account,
                       @Param("pwd")String pwd,
                       @Param("newPwd")String newPwd);
    
    List<String> getOpenIdByAccountId(@Param("accountIdList") List<Long> accountIdList);
    
    
    StoreAccountBean  getById(@Param("id")long id);
    
    int updateStoreAccount(StoreAccountBean storeAccountBean);
    
    int unbindStoreAccount(@Param("id")long id);
    
    int updateSignStatus(@Param("id")long id,@Param("signStatus")int status);

}
