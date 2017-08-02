package com.zhsj.api.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.zhsj.api.bean.LoginUser;
import com.zhsj.api.bean.StoreAccountBean;
import com.zhsj.api.bean.StoreBean;
import com.zhsj.api.service.NotifyService;
import com.zhsj.api.service.OrderService;
import com.zhsj.api.service.RoleService;
import com.zhsj.api.service.ShopService;
import com.zhsj.api.service.StoreAccountService;
import com.zhsj.api.service.StoreService;
import com.zhsj.api.util.CommonResult;
import com.zhsj.api.util.login.LoginUserUtil;

@RestController
@RequestMapping("storeManage")
public class StoreManageController {

    Logger logger = LoggerFactory.getLogger(StoreManageController.class);
	@Autowired
    private OrderService orderService;
	@Autowired
    private StoreService storeService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private StoreAccountService storeAccountService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private NotifyService notifyService;
	
	@RequestMapping(value = "index")
	public Object index(ModelAndView mv,String auth){
		logger.info("#index#");
		mv.addObject("auth", auth);
		mv.setViewName("merchant/index");
		return mv;
	}
	
	@RequestMapping("refundPage")
	public Object refundPage(ModelAndView mv,String auth){
		logger.info("#refundPage#");
		LoginUser loginUser = LoginUserUtil.getLoginUser();
		StoreBean storeBean = LoginUserUtil.getStore();
		String storeNo = storeBean.getStoreNo();
		long accountId = loginUser.getId();
		mv.addObject("storeNo", storeNo);
		mv.addObject("accountId", accountId);
		mv.setViewName("storeManage/refund");
		return mv;
	}
	
	
    @RequestMapping(value = "serach", method = RequestMethod.POST)
    public Object serach(String storeNo, String orderId,String transId){
    	logger.info("#serach# storeNo = {}, orderId = {}, transId = {}", storeNo, orderId, transId);
    	if(StringUtils.isEmpty(orderId) && StringUtils.isEmpty(transId)){
    		return CommonResult.build(2, "参数不能为空");
    	}
    	return orderService.serachByOrderIdOrTransId(storeNo, orderId, transId);
    }
    
    @RequestMapping(value = "refund", method = RequestMethod.POST)
    public Object refund(long id,double price,int accountId,String auth){
    	logger.info("#refund# id = {}, price = {}, accountId = {}", id, price, accountId);
    	if(price <= 0){
    		return CommonResult.build(2, "退款金额有误");
    	}
    	return orderService.appRefund(id, price, accountId);
    }
	    
	    
    @RequestMapping(value = "orderListPage")
    public Object orderListPage(ModelAndView mv, String auth){
    	logger.info("#orderListPage#" );
    	StoreBean storeBean = LoginUserUtil.getStore();
    	String storeNo = storeBean.getStoreNo();
    	mv.addObject("storeList", storeService.getListByStoreNo(storeNo));
    	mv.addObject("storeNo", storeNo);
    	mv.setViewName("storeManage/order");
    	return mv;
    }
    
    @RequestMapping(value = "orderList", method = RequestMethod.POST)
    public Object orderList(int type,String storeNo,int payChannel,int payMethod, int startTime, int endTime, int status, int page,int pageSize){
    	logger.info("#orderList# type={},storeNo = {}, payChannel = {}, payMethod = {}, startTime= {},endTime={}, status ={},page={}, pageSize={}",
    			type,storeNo, payChannel, payMethod, startTime, endTime, status, page, pageSize);
    	if(StringUtils.isEmpty(storeNo)){
    		return CommonResult.build(2, "门店编号有误");
    	}
    	return orderService.getOrderListByParam(type,storeNo, payChannel,payMethod, startTime, endTime, status, page, pageSize);
    } 
    
    
    @RequestMapping(value = "toSetting")
    public Object toSetting(ModelAndView mv,String auth){
    	logger.info("#toSetting#");
    	mv.setViewName("storeManage/mine/setting");
    	mv.addObject("auth", auth);
    	return mv;
    }
    
    @RequestMapping(value = "toPasswordReset")
    public Object toPasswordReset(ModelAndView mv,String auth){
    	logger.info("#toPasswordReset#");
    	mv.addObject("auth", auth);
    	mv.setViewName("storeManage/mine/passwordReset");
    	return mv;
    }
    
    @RequestMapping(value = "toMemberManage")
    public Object toMemberManage(ModelAndView mv, String auth){
    	logger.info("#toMemberManage#");
    	mv.addObject("auth", auth);
    	mv.addObject("data", shopService.getMemberData());
    	mv.setViewName("storeManage/manage/memberManage");
    	return mv;
    }
    @RequestMapping(value = "toMarketAccount")
	public Object toMarketAccount(String auth){
		logger.info("#toMarketAccount#");
		return CommonResult.success("","../shop/toAccountBalancePage");
	}  
    
    @RequestMapping(value = "addUserPage")
    public Object addUserPage(ModelAndView mv, String storeNo,String auth){
    	logger.info("#addUser#");
    	mv.addObject("auth", auth);
    	mv.addObject("storeNo", storeNo);
    	mv.addObject("roleList", roleService.getList());
    	mv.addObject("notifyList", notifyService.getList());
    	mv.setViewName("storeManage/manage/add_user");
    	return mv; 
    }
    
    @RequestMapping(value = "memberConsume")
    public Object memberConsume(ModelAndView mv, String auth){
    	logger.info("#memberConsume#");
    	mv.addObject("auth", auth);
    	mv.addObject("memberMap", storeService.getMemberStaByDay(1));
    	mv.setViewName("storeManage/manage/member_consume");
    	return mv;
    }
    
    @RequestMapping(value = "getMemberStaByDay")
    public Object getMemberStaByDay(int day, String auth){
    	logger.info("#getMemberStaByDay# day = {}", day);
    	return CommonResult.success("", storeService.getMemberStaByDay(day));
    }
    
    @RequestMapping(value = "store")
    public Object store(ModelAndView mv, String auth){
    	logger.info("#store#");
    	mv.addObject("auth", auth);
    	mv.addObject("storeStaList", storeService.getStoreStaListByLoginUser());
    	mv.setViewName("storeManage/manage/store");
    	return mv;
    }
    
    @RequestMapping(value = "transaction")
    public Object transaction(ModelAndView mv, String auth){
    	logger.info("transaction");
    	mv.addObject("auth", auth);
    	mv.addObject("orderSta", storeService.getTodaySta());
    	mv.addObject("childStoreCount", storeService.getChildStoreCount());
    	mv.setViewName("storeManage/manage/transaction-analysis");
    	return mv;
    }
    
    @RequestMapping(value = "getMoneyStaByDay")
    public Object getMoneyStaByDay(int day, String auth){
    	logger.info("#getMoneyStaByDay# day = {}", day);
    	return CommonResult.success("", storeService.getMoneyStaByDay(day));
    }
    
    @RequestMapping(value = "getCountStaByDay")
    public Object getCountStaByDay(int day, String auth){
    	logger.info("#getCountStaByDay# day = {}", day);
    	return CommonResult.success("",  storeService.getCountStaByDay(day));
    }
    
    @RequestMapping(value = "getStoreStaByDay")
    public Object getStoreStaByDay(int day, String auth){
    	logger.info("#getStoreStaByDay# day = {}", day);
    	return CommonResult.success("", storeService.getStoreStaByDay(day));
    }
    
    @RequestMapping(value = "userInfo")
    public Object userInfo(ModelAndView mv, long id, String storeNo,String auth){
    	logger.info("#userInfo# id = {}, storeNo = {}", id, storeNo);
    	mv.addObject("auth", auth);
    	Map<String, Object> resultMap = storeService.getStoreAccountByAccountId(id);
    	resultMap.put("storeNo", storeNo);
    	mv.addObject("map", resultMap);
    	mv.setViewName("storeManage/manage/user_info");
    	return mv;
    }
    
    @RequestMapping(value = "userList")
    public Object userList(ModelAndView mv, String storeNo, String auth){
    	logger.info("#userList# storeNo = {}", storeNo);
    	mv.addObject("auth", auth);
    	mv.addObject("storeNo", storeNo);
    	mv.addObject("storeAccountList", storeAccountService.getListByStoreNo(storeNo));
    	mv.setViewName("storeManage/manage/user_list");
    	return mv;
    }
    
    @RequestMapping(value = "addUser", method=RequestMethod.POST)
    public Object addUser(String auth,StoreAccountBean storeAccountBean,String storeNo, int[] roleIds,int[] notifyIds){
    	logger.info("#addUser# account={},password={},name={},mobile={},roleIds={},notifys={}", storeAccountBean.getAccount(),
    			storeAccountBean.getPassword(),storeAccountBean.getName(),storeAccountBean.getMobile(),roleIds,notifyIds);
    	if(StringUtils.isEmpty(storeAccountBean.getAccount()) && StringUtils.isEmpty(storeAccountBean.getPassword())
    			&& StringUtils.isEmpty(storeAccountBean.getName()) && StringUtils.isEmpty(storeAccountBean.getMobile())){
    		return CommonResult.build(2, "员工信息填写完整");
    	}
    	return storeService.addUser(storeAccountBean, storeNo, roleIds, notifyIds);
    }
    
    @RequestMapping(value = "modifyUser", method = RequestMethod.POST)
    public Object modifyUser(long accountId, String storeNo,int status, int valid, Integer[] roleIds, Integer[] notifyIds, String auth){
    	logger.info("#modifyUser# accountId = {}, storeNo= {} status = {}, roleIds = {}, notifyIds = {}",
    			accountId, storeNo, status, roleIds, notifyIds);
    	return storeService.modifyUser(accountId, storeNo,status, valid, roleIds, notifyIds);
    }
    
    
}
