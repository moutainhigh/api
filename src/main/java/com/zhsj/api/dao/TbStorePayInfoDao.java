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

    List<String> getStoreNo(@Param("payType")int payType,
							@Param("payMethod")String payMethod,
							@Param("field1") String field1,
							@Param("mchId") String mchId);


}
