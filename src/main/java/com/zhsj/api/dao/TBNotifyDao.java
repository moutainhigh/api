package com.zhsj.api.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.zhsj.api.bean.NotifyBean;
import com.zhsj.api.util.db.DS;
import com.zhsj.api.util.db.DynamicDataSource;


@Component
@DynamicDataSource(DS.DB_MANAGE)
public interface TBNotifyDao {

	List<NotifyBean> getList();
}
