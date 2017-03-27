package com.zhsj.api.task;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhsj.api.bean.StoreBalanceDetailBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.dao.TBStoreBalanceDetailsDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.service.WXService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;

@Component
public class QueryTransfersTask implements InitializingBean{
	private Logger logger = LoggerFactory.getLogger(QueryTransfersTask.class);

	@Autowired
	private TBStoreBalanceDetailsDao tbStoreBalanceDetailsDao;
	@Autowired
	private WXService wxService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private TbStoreDao tbStoreDao;
	
	private ScheduledExecutorService refreshExecutorService = Executors.newScheduledThreadPool(1);
	private long initialDelay = 10;
	private long period = 300;
	@Override
	public void afterPropertiesSet() throws Exception {
		
		refreshExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try{
					List<String> partnerTradeNos  = tbStoreBalanceDetailsDao.getlistByStatus();
					for(String partnerTradeNo : partnerTradeNos){
						CommonResult cr = (CommonResult)wxService.queryTransfersInfo(partnerTradeNo);
						StoreBalanceDetailBean sbdb = tbStoreBalanceDetailsDao.getByPartnerTradeNo(partnerTradeNo);
						if(cr != null){
							if(cr.getCode() == 0){
								if("SUCCESS".equals(cr.getMsg())){
									Map<String, String> map = (Map<String, String>) cr.getData();
									sbdb.setPaymentTime(new Long(
											 DateUtil.formatStringUnixTime(map.get("payment_time"), "yyyy-MM-dd HH:mm:ss")).intValue());
									sbdb.setPaymentStatus(1);
									sbdb.setPaymentNo(map.get("payment_no"));
									 int rightId = tbStoreBalanceDetailsDao.update(sbdb);
									 if(rightId == 0){
										 logger.info("#WXService.transfers 提现成功！更新数据失败");
									 }
								}
							}else if(cr.getCode() == 2){//错误
								StoreBean storeBean = tbStoreDao.getStoreByNo(sbdb.getStoreNo());
								shopService.updateAdd(sbdb ,sbdb.getPrice().doubleValue(), sbdb.getStoreNo() ,storeBean.getPrice());//更新数据添加新数据
							}
						}
					}
				}catch(Exception e){
					logger.info("QueryTransfersTask.afterPropertiesSet ",e);
				}
			}
		}, initialDelay, period, TimeUnit.SECONDS);
	}

}
