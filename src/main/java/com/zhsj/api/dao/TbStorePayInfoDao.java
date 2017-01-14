package com.zhsj.api.dao;


import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStorePayInfoDao {


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
