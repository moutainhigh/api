package com.zhsj.api.dao;


import java.util.List;

import com.zhsj.api.bean.StorePayInfo;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TbStorePayInfoDao {


    List<StorePayInfo> getStorePayInfoByNO(@Param("storeNo") String storeNo);
    
    List<StorePayInfo> getByStoreNoAndType(@Param("storeNo") String storeNo,
    										@Param("payType")int payType,
    										@Param("payMethod")String payMethod);

    int insertPayInfo(@Param("storeNo")String storeNo,
                      @Param("payType")int payType,
                      @Param("payMethods")List<String> payMethods,
                      @Param("field1") String field1,
                      @Param("field2") String field2,
                      @Param("field3") String field3,
                      @Param("field4") String field4,
                      @Param("remark") String remark,
                      @Param("status") int status);
    
    int insertFY(@Param("beans")List beans);

//    int updateByNo1 (@Param("remark") String remark,
//    				 @Param("storeNo")String storeNO,
//    				 @Param("rate")String rate,
//    				 @Param("settlementType")String settlementType,
//    				 @Param("payMethod")String paymethod);

     StorePayInfo getByStoreNoAndTypeAndMethod(@Param("storeNo")String storeNo, 
    		 @Param("payType")int payType, @Param("payMethod")String payMethod);
     
     List<String> getStoreNosByField1(@Param("field1")String field1);
     
     List<String> getStoreNosByMchId(@Param("mchId")String mchId);
}
