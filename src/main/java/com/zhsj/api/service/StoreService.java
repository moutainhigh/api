package com.zhsj.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhsj.api.bean.MinMaxBean;
import com.zhsj.api.bean.NotifyBean;
import com.zhsj.api.bean.RoleBean;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreAccountBindNotifyBean;
import com.zhsj.api.bean.StoreAccountBindRoleBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.bean.StoreTransactionSummary;
import com.zhsj.api.bean.result.MoreStoreSta;
import com.zhsj.api.bean.result.OrderSta;
import com.zhsj.api.bean.result.RefundSta;
import com.zhsj.api.bean.result.StoreSta;
import com.zhsj.api.dao.TBNotifyDao;
import com.zhsj.api.dao.TBRoleDao;
import com.zhsj.api.dao.TBStoreAccountBindNotifyDao;
import com.zhsj.api.dao.TBStoreAccountBindRoleDao;
import com.zhsj.api.dao.TBStoreAccountDao;
import com.zhsj.api.dao.TBStoreBindAccountDao;
import com.zhsj.api.dao.TBStoreTransactionSummaryDao;
import com.zhsj.api.dao.TbOrderDao;
import com.zhsj.api.dao.TbStoreDao;
import com.zhsj.api.dao.TbUserBindStoreDao;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.MD5Util;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.login.LoginUserUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcg on 16/12/29.
 */
@Service
public class StoreService {
    Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Autowired
    private TbStoreDao tbStoreDao;
    @Autowired
    private TbOrderDao tbOrderDao;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
    @Autowired
    private TBStoreAccountBindNotifyDao tbStoreAccountBindNotifyDao;
    @Autowired
    private TBRoleDao tbRoleDao;
    @Autowired
    private TBNotifyDao tbNotifyDao;
    @Autowired
    private TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    private TbUserBindStoreDao tbUserBindStoreDao;
    @Autowired
    private TBStoreTransactionSummaryDao tbStoreTransactionSummaryDao;
    

    public StoreBean getStoreByNO(String storeNo){
        return tbStoreDao.getStoreByNo(storeNo);
    }
    
    public List<StoreBean> getStoreByLimitId(int minId,int maxId){
    	return tbStoreDao.getStoreByLimitId(minId, maxId);
    }

    public MinMaxBean getMaxMin(){
    	return tbStoreDao.getMaxMin();
    }
    
    public void updatePrice(double price,String storeNo){
    	 tbStoreDao.updatePrice(price, storeNo);
    }
    
    public List<StoreBean> getListByStoreNo(String storeNo){
    	logger.info("#getListByStoreNo# storeNo = {}" , storeNo);
		try {
			List<StoreBean>  storeBeans = tbStoreDao.getListByParentNo(storeNo);
			StoreBean storeBean = tbStoreDao.getStoreByNo(storeNo);
			storeBeans.add(0, storeBean);
			return storeBeans;
		} catch (Exception e){
			logger.error("#getListByStoreNo# storeNo = {}" , storeNo, e);
			return null;
		}
    }
    
    public Object getStoreStaListByLoginUser(){
    	logger.info("#getStoreStaListByLoginUser#");
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			List<StoreBean>  storeBeans = tbStoreDao.getListByParentNo(storeNo);
			storeBeans.add(0, storeBean);
			List<StoreSta> storeStas = new ArrayList<StoreSta>();
			int startTime = DateUtil.getTodayStartTime();
			int endTime = startTime + 24*60*60 - 1;
			for(StoreBean store:storeBeans){
				StoreSta storeSta = tbOrderDao.getByTodayStoreSta(store.getStoreNo(), startTime, endTime);
				double refundMoney = tbOrderDao.getTodayRefundMoney(store.getStoreNo(), startTime, endTime);
				storeSta.setId(store.getId());
				storeSta.setName(store.getName());
				storeSta.setParentNo(store.getParentNo());
				storeSta.setStoreNo(store.getStoreNo());
				storeSta.setStatus(store.getStatus());
				storeSta.setTransSumMoney(Arith.sub(storeSta.getTransSumMoney(), refundMoney));
				storeStas.add(storeSta);
			}
			return storeStas;
		} catch (Exception e) {
			logger.error("#getStoreStaListByLoginUser#");
		}
    	return null;
    }
    
    public Map<String, Object> getStoreAccountByAccountId(long id){
    	logger.info("#getStoreAccuntByAccountId# id = {}", id);
    	try {
			StoreAccountBean storeAccountBean = tbStoreAccountDao.getById(id);
			List<Integer> roleIdList = tbStoreAccountBindRoleDao.getRoleIdByAccountId(id);
			List<RoleBean> roleList = tbRoleDao.getListByTypeEq2();
			for(RoleBean role:roleList){
				for(Integer roleId: roleIdList){
					if(role.getId() == roleId){
						role.setFlag(1);
						continue;
					}
				}
			}
			List<Integer> notifyIdList = tbStoreAccountBindNotifyDao.getNotifyIdByAccountId(id);
			List<NotifyBean> notifyList = tbNotifyDao.getList();
			for(NotifyBean notify:notifyList){
				for(Integer notifyId:notifyIdList){
					if(notify.getId() == notifyId){
						notify.setFlag(1);
						continue;
					}
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("storeAccount", storeAccountBean);
			resultMap.put("roleList", roleList);
			resultMap.put("notifyList", notifyList);
			return resultMap;
		} catch (Exception e) {
			logger.error("#getStoreAccountByAccountId# id = {}", id, e);
		}
    	return null;
    }
    
    
    public Object addUser(StoreAccountBean storeAccountBean, String storeNo,int[] roleIds, int[] notifyIds){
    	logger.info("#addUser# account={},password={},name={},mobile={},roleIds={},notifyIds={}",storeAccountBean.getAccount(),storeAccountBean.getPassword(),
    			storeAccountBean.getName(),storeAccountBean.getMobile(),roleIds,notifyIds);
    	try {
    		StoreAccountBean sab = tbStoreAccountDao.getOneByAccount(storeAccountBean.getAccount());
    		if(sab != null){
    			return CommonResult.build(2, "账号已被注册");
    		}
			storeAccountBean.setPassword(MD5Util.encode(storeAccountBean.getPassword()));
			long code  = tbStoreAccountDao.insert(storeAccountBean);
			if(code != 1){
				logger.info("#addUser# 添加storeAccount失败");
				return CommonResult.build(2, "添加员工失败");
			}
			long accountId = storeAccountBean.getId();
			int c = tbStoreBindAccountDao.insert(storeNo, accountId);
			if(c < 1){
				logger.info("#addUser# 添加StoreBindAccount失败");
				return CommonResult.build(2, "添加员工失败");
			}
			List<StoreAccountBindRoleBean> storeAccountBindRoleBeans = new ArrayList<StoreAccountBindRoleBean>();
			for(Integer roleId:roleIds){
				StoreAccountBindRoleBean storeAccountBindRoleBean = new StoreAccountBindRoleBean();
				storeAccountBindRoleBean.setAccountId(accountId);
				storeAccountBindRoleBean.setRoleId(roleId);
				storeAccountBindRoleBeans.add(storeAccountBindRoleBean);
			}
			if(roleIds.length > 0){
				int roleCode = tbStoreAccountBindRoleDao.insertList(storeAccountBindRoleBeans);
				if(roleCode < 1){
					logger.info("#addUser# 添加storeAccountBindRoleBeans失败");
					return CommonResult.build(2, "添加员工失败");
				}
			}
			List<StoreAccountBindNotifyBean> storeAccountBindNotifyBeans = new ArrayList<StoreAccountBindNotifyBean>();
			for(Integer notifyId:notifyIds){
				StoreAccountBindNotifyBean storeAccountBindNotifyBean = new StoreAccountBindNotifyBean();
				storeAccountBindNotifyBean.setAccountId(accountId);
				storeAccountBindNotifyBean.setNotifyId(notifyId);
				storeAccountBindNotifyBeans.add(storeAccountBindNotifyBean);
			}
			if(notifyIds.length > 0){
				int notifyCode = tbStoreAccountBindNotifyDao.insertList(storeAccountBindNotifyBeans);
				if(notifyCode < 1){
					logger.info("#addUser# 添加storeAccountBindNotifyBeans失败");
					return CommonResult.build(2, "添加 员工失败");
				}
			}
			return CommonResult.success("添加员工成功");
		} catch (Exception e) {
			logger.error("#addUser# account={},password={},name={},mobile={},roleIds={},notifyIds={}",storeAccountBean.getAccount(),storeAccountBean.getPassword(),
	    			storeAccountBean.getName(),storeAccountBean.getMobile(),roleIds,notifyIds, e);
		}
    	return CommonResult.defaultError("系统异常");
    }
    
    
    public Object modifyUser(long accountId, String storeNo, int status, int valid, Integer[] roleIds, Integer[] notifyIds){
    	logger.info("#modifyUser# accountId = {}, storeNo = {}, status = {}, roleIds = {}, notifyIds = {}", accountId, storeNo, status, roleIds, notifyIds);
    	try {
    		List<Long> accountIds = tbStoreBindAccountDao.getAccountIdByStoreNo(storeNo);
    		List<StoreAccountBean> storeAccountBeans = tbStoreAccountDao.getListByIdsAndRoleId(accountIds, Integer.valueOf(MtConfig.getProperty("STORE_MANAGER_ROLE", "")));
    		List<Integer> saRoleIds = tbStoreAccountBindRoleDao.getRoleIdByAccountId(accountId);
    		StoreAccountBean storeAccountBean = tbStoreAccountDao.getById(accountId);
    		if(valid == 0){
    			if(storeAccountBeans != null && storeAccountBeans.size() == 1 && saRoleIds.contains(MtConfig.getProperty("STORE_MANAGER_ROLE", ""))){
    				return CommonResult.build(2, "不能被删除,最后一个了");
    			}
    			int vCode = tbStoreAccountDao.updateValid(accountId);
    			if(vCode < 1){
    				logger.info("#modifyUser# 删除失败 ");
    				return CommonResult.build(2, "删除失败");
    			}
    			return CommonResult.success("删除成功");
    		}
    		if(status == 0){//更新状态
    			int sta = storeAccountBean.getStatus() == 1?0:1;
    			if(storeAccountBeans != null && storeAccountBeans.size() == 1 && sta == 0){
    				return CommonResult.build(2, "更新状态失败,最后一个了");
    			}
    			int sCode = tbStoreAccountDao.updateStatus(sta, accountId);
    			if(sCode < 1){
    				logger.info("#modifyUser# 更新状态失败");
    				return CommonResult.build(2, "更新状态失败");
    			}
    			return CommonResult.success("修改状态成功");
    		}
			List<Integer> roleIdList = tbStoreAccountBindRoleDao.getRoleIdByAccountId(accountId);
			List<Integer> comRoleIds = new ArrayList<Integer>();//共有的roleId
			for(Integer roleId:roleIdList){
				for(int roleid:roleIds){
					if(roleId == roleid){
						comRoleIds.add(roleId);
						continue;
					}
				}
			}
			List<Integer> newRoleIds = Arrays.asList(roleIds);
			newRoleIds = new ArrayList<Integer>(newRoleIds);
			newRoleIds.removeAll(comRoleIds);
			roleIdList.removeAll(comRoleIds);
			if(newRoleIds.size() > 0){//添加
				List<StoreAccountBindRoleBean> storeAccountBindRoleBeans = new ArrayList<StoreAccountBindRoleBean>();
				for(Integer roleid : newRoleIds){
					StoreAccountBindRoleBean storeAccountBindRoleBean = new StoreAccountBindRoleBean();
					storeAccountBindRoleBean.setAccountId(accountId);
					storeAccountBindRoleBean.setRoleId(roleid);
					storeAccountBindRoleBeans.add(storeAccountBindRoleBean);
				}
				int nrCode = tbStoreAccountBindRoleDao.insertList(storeAccountBindRoleBeans);
				if(nrCode < 1){
					logger.info("#modifyUser# 添加StoreAccountBindRoleBean失败");
					return CommonResult.build(2, "修改失败");
				}
			}
			if(roleIdList.size() > 0){//删除
				int drCode = tbStoreAccountBindRoleDao.deleteAll(roleIdList, accountId);
				if(drCode < 1){
					logger.info("#modifyUser# 删除StoreAccountBindRoleBean失败");
					return CommonResult.build(2, "修改失败");
				}
			}
			List<Integer> notifyIdList = tbStoreAccountBindNotifyDao.getNotifyIdByAccountId(accountId);
			List<Integer> comNotifyIds = new ArrayList<Integer>();//共有的notifyId
			for(Integer notifyId:notifyIdList){
				for(int notifyid:notifyIds){
					if(notifyId == notifyid){
						comNotifyIds.add(notifyId);
						continue;
					}
				}
			}
			List<Integer> newNotifyIds = Arrays.asList(notifyIds);
			newNotifyIds = new ArrayList<Integer>(newNotifyIds);
			newNotifyIds.removeAll(comNotifyIds);
			notifyIdList.removeAll(comNotifyIds);
			if(newNotifyIds.size() > 0){//添加
				List<StoreAccountBindNotifyBean> storeAccountBindNotifyBeans = new ArrayList<StoreAccountBindNotifyBean>();
				for(Integer notifyId : newNotifyIds){
					StoreAccountBindNotifyBean storeAccountBindNotifyBean = new StoreAccountBindNotifyBean();
					storeAccountBindNotifyBean.setNotifyId(notifyId);
					storeAccountBindNotifyBean.setAccountId(accountId);
					storeAccountBindNotifyBeans.add(storeAccountBindNotifyBean);
				}
				int nNCode = tbStoreAccountBindNotifyDao.insertList(storeAccountBindNotifyBeans);
				if(nNCode < 1){
					logger.info("#modifyUser# 添加StoreAccountBindNotifyBean失败");
					return CommonResult.build(2, "修改失败");
				}
			}
			if(notifyIdList.size() > 0){//删除
				int dNCode = tbStoreAccountBindNotifyDao.deleteList(notifyIdList, accountId);
				if(dNCode < 1){
					logger.info("#modifyUser# 删除StoreAccountBindNotifyBean失败");
					return CommonResult.build(2, "修改失败");
				}
			}
			return CommonResult.success("修改成功");
		} catch (Exception e) {
            logger.error("#modifyUser# accountId = {}, status = {}, roleIds = {}, notifyIds = {}", accountId, status, roleIds, notifyIds, e);
			return CommonResult.defaultError("系统异常");
		}
    }
    
    
    public OrderSta getTodaySta(){
    	logger.info("#getTodaySta#");
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			int startTime = DateUtil.getTodayStartTime();
			int endTime = startTime + 24*60*60-1;
			OrderSta orderSta = tbOrderDao.getTodayOrderSta(storeNo, startTime, endTime);
			RefundSta refundSta = tbOrderDao.getTodayRefundSta(storeNo, startTime, endTime);
			orderSta.setAm(Arith.sub(orderSta.getAm(), refundSta.getRefundMoney()));
			if(orderSta.getCount() != 0){
			    orderSta.setAverage(Arith.div(orderSta.getAm(), orderSta.getCount(), 2));
			}else{
				orderSta.setAverage(0.00);
			}
			return orderSta;
		} catch (Exception e) {
			logger.error("#getTodaySta#", e);
			return null;
		}
    }
    
    public int getChildStoreCount(){
    	logger.info("#getChildStoreCount#");
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			int count = tbStoreDao.getChildStoreCountByStoreNo(storeNo);
			return count;
		} catch (Exception e) {
			logger.error("#getChildStoreCount#");
			return 0;
		}
    }
    
    //会员统计
    public Object getMemberStaByDay(int day){
    	//1、今日新增会员人数  2、老会员 3、活跃会员
    	logger.info("#getMemberSta#");
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			int startTime = DateUtil.getTodayStartTime();
			int endTime = startTime + 24*60*60-1;
			int oneDay = 24*60*60;
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("storeNo", storeNo);
			paramMap.put("startTime", startTime - (day -1) * oneDay);
			paramMap.put("endTime", endTime);
			int todayNewAddCount = tbUserBindStoreDao.getMemberCountByParam(paramMap);//新增会员
			paramMap.put("startTime", startTime - 24*60*60*365);
			int oldCount = tbUserBindStoreDao.getMemberCountByParam(paramMap);//老会员
			int activeCount = tbUserBindStoreDao.countByStoreNo(storeNo, startTime - (day -1) * oneDay, endTime);
			activeCount = activeCount - todayNewAddCount;
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("addCount", todayNewAddCount);
			resultMap.put("oldCount", oldCount);
			resultMap.put("activeCount", activeCount);
			return resultMap;
		} catch (Exception e) {
			logger.error("#getMemberSta#", e);
			return null;
		}
    }
    
    
    public Object getMoneyStaByDay(int day){
    	logger.info("#getMoneyStaByDay# day = {}", day);
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			List<StoreBean> stores = tbStoreDao.getListByParentNo(storeNo);
			stores.add(0, storeBean);
			int oneDay = 24 * 60 * 60;
			int startTime = DateUtil.getTodayStartTime() - day * oneDay;
			int endTime = DateUtil.getTodayStartTime() - 1;
			List<String> timeList = new ArrayList<String>();
			for(int i =day;i >=1;i--){
				long date = (DateUtil.getTodayStartTime() - i * oneDay) * 1000L;
				timeList.add(DateUtil.getYMD(date));
			}
			List<String> storeNos = new ArrayList<String>();
			for(StoreBean sb:stores){
				storeNos.add(sb.getStoreNo());
			}
			List<StoreTransactionSummary> list = tbStoreTransactionSummaryDao.getMoneyByListAndTime(storeNos, 
					startTime, endTime);
			Map<String, Object> map = new HashMap<String, Object>();
			for(StoreTransactionSummary sts : list){
				if(map.containsKey(sts.getTime())){
					StoreTransactionSummary storeTransactionSummary = (StoreTransactionSummary) map.get(sts.getTime());
					storeTransactionSummary.setActualSumAmount(Arith.add(storeTransactionSummary.getActualSumAmount(), sts.getActualSumAmount()));
					map.put(sts.getTime(), storeTransactionSummary);
				}else{
					map.put(sts.getTime(), sts);
				}
			}
			double sum = 0.00;
			for(StoreTransactionSummary sts : list){
				sum = Arith.add(sum, sts.getActualSumAmount());
			}
			double average = 0.00;
			if(list != null && list.size() > 0){
				average = Arith.div(sum, day, 2);
			}
			List<StoreTransactionSummary> newlist = new ArrayList<StoreTransactionSummary>();
			for(String time:timeList){
				int flag = 0;
				for(String key : map.keySet()){
					if(time.equals(key)){
						newlist.add((StoreTransactionSummary) map.get(key));
						flag = 1;
						break;
					}
				}
				if(flag == 0){
					StoreTransactionSummary st = new StoreTransactionSummary();
					st.setActualSumAmount(0.00);
					st.setOrderCount(0);
					st.setTime(time);
					newlist.add(st);
				}
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("sum", sum);
			resultMap.put("average", average);
			resultMap.put("list", newlist);
			return resultMap;
		} catch (Exception e) {
			logger.error("#getMoneyStaByDay# day = {}", day, e);
			return null;
		}
    }
    
    
    public Object getCountStaByDay(int day){
    	logger.info("#getCountStaByDay# day = {}", day);
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			List<StoreBean> stores = tbStoreDao.getListByParentNo(storeNo);
			stores.add(0, storeBean);
			int oneDay = 24 * 60 * 60;
			int startTime = DateUtil.getTodayStartTime() - day * oneDay;
			int endTime = DateUtil.getTodayStartTime() - 1;
			List<String> timeList = new ArrayList<String>();
			for(int i =day;i >=1;i--){
				long date = (DateUtil.getTodayStartTime() - i * oneDay) * 1000L;
				timeList.add(DateUtil.getYMD(date));
			}
			
			List<String> storeNos = new ArrayList<String>();
			for(StoreBean sb:stores){
				storeNos.add(sb.getStoreNo());
			}
			List<StoreTransactionSummary> list = tbStoreTransactionSummaryDao.getCountByListAndTime(storeNos, startTime, endTime);
			Map<String, Object> map = new HashMap<String, Object>();
			for(StoreTransactionSummary sts : list){
				if(map.containsKey(sts.getTime())){
					StoreTransactionSummary storeTransactionSummary = (StoreTransactionSummary) map.get(sts.getTime());
					storeTransactionSummary.setOrderCount(storeTransactionSummary.getOrderCount()+sts.getOrderCount());
					map.put(sts.getTime(), storeTransactionSummary);
				}else{
					map.put(sts.getTime(), sts);
				}
			}
		    int sum_count = 0;
		    for(StoreTransactionSummary sts:list){
		    	sum_count += sts.getOrderCount();
		    }
		    int average_count = 0;
		    if(list != null && list.size() > 0){
		    	average_count = sum_count/day;
		    }
		    List<StoreTransactionSummary> newlist = new ArrayList<StoreTransactionSummary>();
			for(String time:timeList){
		        int flag = 0;
				for(String key : map.keySet()){
					if(time.equals(key)){
						newlist.add((StoreTransactionSummary) map.get(key));
						flag = 1;
						break;
					}
				}
				if(flag == 0){
					StoreTransactionSummary st = new StoreTransactionSummary();
					st.setActualSumAmount(0.00);
					st.setOrderCount(0);
					st.setTime(time);
					newlist.add(st);
				}
			}
		    Map<String, Object> resultMap = new HashMap<String, Object>();
		    resultMap.put("sum_count", sum_count);
		    resultMap.put("average_count", average_count);
		    resultMap.put("list", newlist);
			return resultMap;
		} catch (Exception e) {
			logger.error("#getCountByStaByDay# day = {}", day, e);
			return null;
		}
    }
    
    
    public Object getStoreStaByDay(int day){
    	logger.info("#getStoreStsaByDay# day = {}", day);
    	try {
			StoreBean storeBean = LoginUserUtil.getStore();
			String storeNo = storeBean.getStoreNo();
			List<StoreBean> stores = tbStoreDao.getListByParentNo(storeNo);
			stores.add(0, storeBean);
			int oneDay = 24 * 60 * 60;
			int startTime = DateUtil.getTodayStartTime() - day * oneDay;
			int endTime = DateUtil.getTodayStartTime() - 1;
			List<MoreStoreSta> mssList = new ArrayList<MoreStoreSta>();
			for(StoreBean sb: stores){
				List<StoreTransactionSummary> stsList = tbStoreTransactionSummaryDao.getMoneyByTime(storeNo, startTime, endTime);
				double sum = 0.00;
				for(StoreTransactionSummary sts :stsList){
					sum = Arith.add(sum, sts.getActualSumAmount());
				}
				MoreStoreSta mss = new MoreStoreSta();
				mss.setName(sb.getName());
				mss.setStoreNo(sb.getStoreNo());
				mss.setSum(sum);
				mssList.add(mss);
			}
			double sumSta = 0.00;
			for(MoreStoreSta mss:mssList){
				sumSta = Arith.add(sumSta, mss.getSum());
			}
			double average = Arith.div(sumSta, stores.size(), 2);
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("list", mssList);
			resultMap.put("sum", sumSta);
			resultMap.put("average", average);
			return resultMap;
		} catch (Exception e) {
			logger.error("#getStoreStaByDay# day = {}", day, e);
			return null;
		}
    }
    
    
    
    
    
    
}
