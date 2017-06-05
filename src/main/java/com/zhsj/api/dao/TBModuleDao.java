package com.zhsj.api.dao;


import java.util.List;

import com.zhsj.api.bean.ModuleBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBModuleDao {

    List<ModuleBean> getByParentId(@Param("parentId")long parentId);
    
    ModuleBean getByURI(@Param("uri")String uri);
    
    List<ModuleBean> getByParentIdAndType(@Param("parentId")long parentId,
    									  @Param("type")int type);

}
