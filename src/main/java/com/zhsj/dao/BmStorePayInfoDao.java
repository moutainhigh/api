package com.zhsj.dao;


import com.zhsj.bean.StoreBean;
import com.zhsj.bean.StorePayInfo;
import com.zhsj.util.db.DS;
import com.zhsj.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface BmStorePayInfoDao {


    StorePayInfo getStorePayInfoByNO(@Param("storeNo") String storeNo);

    int insertPayInfo(@Param("storeNo")String storeNo,
                      @Param("payType")int payType,
                      @Param("payMethod")String payMethod,
                      @Param("field1") String field1,
                      @Param("field2") String field2,
                      @Param("field3") String field3,
                      @Param("field4") String field4,
                      @Param("remark") String remark,
                      @Param("status") int status);

    int updateByNo (@Param("remark") String remark,@Param("storeNo")String storeNO);


}
