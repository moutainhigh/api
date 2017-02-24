package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.constants.ResultStatus;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.DateUtil;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lcg on 17/1/28.
 */
@Service
public class ManagerService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private WXService wxService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private TBStoreAccountDao tbStoreAccountDao;
    @Autowired
    private TbStoreNoDao tbStoreNoDao;
    @Autowired
    TBStoreAccountBindRoleDao tbStoreAccountBindRoleDao;
    @Autowired
    TbStoreDao tbStoreDao;
    @Autowired
    TBAccountBindOrgDao tbAccountBindOrgDao;
    @Autowired
    TBOrgDao tbOrgDao;
    @Autowired
    TBStoreBindAccountDao tbStoreBindAccountDao;
    @Autowired
    TbStoreBindOrgDao tbStoreBindOrgDao;
    @Autowired
    TBStoreExtendDao tbStoreExtendDao;
    @Autowired
    TbOrderDao tbOrder;
    @Autowired
    MinshengService minshengService;

    public Map<String,String> loginByOpenId(String code,String appId){
        logger.info("#ManagerService.loginByOpenId# code={},appId={}",code,appId);
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_ERROR);
        try {
            String openId = wxService.getOpenId(code,appId);
            if(StringUtils.isEmpty(openId)){
                resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_ERROR);
                return resultMap;
            }
            AccountBean accountBean = accountService.getByOpenId(openId);
            if(accountBean == null){
                resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.NO_REGISTER);
                resultMap.put(ResultStatus.RESULT_VALUE,openId);
                return resultMap;
            }
            resultMap.put(ResultStatus.RESULT_KEY,ResultStatus.RESULT_SUCCESS);
            resultMap.put(ResultStatus.RESULT_VALUE,openId);
        }catch (Exception e){
            logger.error("#ManagerService.loginByOpenId# code={}",code,e);
        }
        return resultMap;
    }

    public String realnameauth(String storeName,String storeAccount,String storeNo,String auth){
        logger.info("#ManagerService.realnameauth# storeName={},storeAccount={},storeNo={},auth={}"
                , storeName, storeAccount, storeNo, auth);
        String result = "";
        try {
            //检查storeNo是否已经使用
            StoreBean storeBean = storeService.getStoreByNO(storeNo);
            if(storeBean !=null){
                return "商家编号已经使用";
            }
            //检查账号是不已经使用
            StoreAccountBean storeAccountBean = tbStoreAccountDao.getByAccount(storeAccount);
            if(storeAccountBean != null){
                String data = tbStoreExtendDao.getDataByStoreNo(storeNo,1);
                if(StringUtils.isEmpty(data)){
                    return "登录帐号已经使用";
                }
                MSStoreBean msStoreBean = JSONObject.parseObject(data,MSStoreBean.class);
                if(msStoreBean.getStep() == 1){
                    return "PROC:1";
                }else if(msStoreBean.getStep() == 2){
                    return "PROC:2";
                }else {
                    return "error";
                }

            }
            long saleId = LoginUserUtil.getLoginUser().getId();
            int num = tbStoreNoDao.updateStatusByStoreNo(saleId,storeNo);
            if(num != 1){
                return "商家编号不存在或是已经使用";
            }
            Long orgId = tbAccountBindOrgDao.getOrgIdByAccountId(saleId);
            OrgBean orgBean = tbOrgDao.getById(orgId);

            //保存帐号，密码为电话，保存商家，
            storeAccountBean = new StoreAccountBean();
            storeAccountBean.setAccount(storeAccount);
            storeAccountBean.setPassword(Md5.encrypt(storeAccount));
            storeAccountBean.setMobile("");
            tbStoreAccountDao.insert(storeAccountBean);
            String roleId = MtConfig.getProperty("STORE_MANAGER_ROLE", "");
            tbStoreAccountBindRoleDao.insert(storeAccountBean.getId(),Long.parseLong(roleId));

            //商家信息
            storeBean = new StoreBean();
            storeBean.setParentNo("0");
            storeBean.setStoreNo(storeNo);
            storeBean.setOrgIds(orgBean.getOrgIds() + "," + orgBean.getId());
            storeBean.setSaleId(saleId);
            storeBean.setName(storeName);
            tbStoreDao.insert(storeBean);

            tbStoreBindAccountDao.insert(storeNo, storeAccountBean.getId());
            tbStoreBindOrgDao.insert(storeNo, orgBean.getId(), orgBean.getOrgIds() + "," + orgBean.getId());
            MSStoreBean msStoreBean = new MSStoreBean();
//            msStoreBean.setReg_contact_tel(storePhone);
//            msStoreBean.setFiled1(storePhone);
            msStoreBean.setMer_name(storeName);
            msStoreBean.setMer_short_name(storeName);
            msStoreBean.setStep(1);
            String json = JSONObject.toJSON(msStoreBean).toString();
            tbStoreExtendDao.insert(storeNo,1,json);
            return "SUCCESS";
        }catch (Exception e){
            logger.error("#ManagerService.realnameauth# storeName={},storeAccount={},storeNo={},auth={}"
                    ,storeName,storeAccount,storeNo,auth,e);
        }
        return "ERROR";
    }

    public String settlement(String cityCode,String address,String businessType,String auth,String storeNo){
        logger.info("#ManagerService.settlement# cityCode={},address={},businessType={},auth={},storeNo={}",cityCode,address,businessType,auth,storeNo);
        try{
            //更新商家地址
            int code = Integer.parseInt(cityCode);
            tbStoreDao.updateAddress(address,code,storeNo);

            String data = tbStoreExtendDao.getDataByStoreNo(storeNo,1);
            MSStoreBean msStoreBean = JSONObject.parseObject(data,MSStoreBean.class);
//            msStoreBean.setLegal_person(name);
//            msStoreBean.setLegal_person_id(idCard);
            msStoreBean.setWx_business_type(businessType);
            msStoreBean.setAli_business_type("2016062900190196");
//            msStoreBean.setWx_rate(rate);
//            msStoreBean.setAli_rate(rate);
//            msStoreBean.setSettlement_type(settlementType);
            msStoreBean.setStep(2);
            String json = JSONObject.toJSON(msStoreBean).toString();
            //更新扩展
            tbStoreExtendDao.updateByStoreNo(storeNo,1,json,2);
            return "SUCCESS";
        }catch (Exception e){
            logger.error("#ManagerService.settlement# cityCode={},address={},businessType={},auth={},storeNo={})",cityCode,address,businessType,auth,storeNo,e);
        }
        return "ERROR";
    }


    public String auditStatus(String saName,String saNum,String saBankName,String merEmail,String auth
    		,String storeNo,String settlementType,String rate,String idCard,String phone) {
        logger.info("#ManagerService.auditStatus# saName={},saNum={},saBankName={},merEmail={},auth={},storeNO={},settlementType={},rate={},idCard={},phone={}",
                saName,saNum,saBankName,merEmail,auth,storeNo,settlementType, rate, idCard, phone);
        try{
        	 Integer status = tbStoreNoDao.getStatusByStoreNo(storeNo);
             if(status == null){
                 return "商家编号不存在";
             }
             if(status == 2){
                 return "SUCCESS";
             }
            String data = tbStoreExtendDao.getDataByStoreNo(storeNo,1);
            MSStoreBean msStoreBean = JSONObject.parseObject(data,MSStoreBean.class);
            msStoreBean.setSa_name(saName);
            msStoreBean.setSa_num(saNum);
            msStoreBean.setSa_bank_name(saBankName);
            msStoreBean.setMer_email(merEmail);
            msStoreBean.setAgent_no(MtConfig.getProperty("agent_no", "95272016121410000062"));
            msStoreBean.setSa_bank_type("00");
            msStoreBean.setUser_pid("");
            msStoreBean.setLegal_person(saNum);
            msStoreBean.setStoreNo(storeNo);
            msStoreBean.setLegal_person_id(idCard);
            msStoreBean.setWx_rate(rate);
            msStoreBean.setAli_rate(rate);
            msStoreBean.setSettlement_type(settlementType);
            msStoreBean.setReg_contact_tel(phone);
            msStoreBean.setFiled1(phone);
            
            String json = JSONObject.toJSON(msStoreBean).toString();
            //更新扩展
            int num = tbStoreExtendDao.updateByStoreNoAndStep(storeNo, 1, json,3,2);
            if(num <=0){
            	return "不要重复提交";
            }
            //如果已经提交，不再提交
            String result = minshengService.openAccount(msStoreBean);
            if("SUCCESS".equals(result)){
                long saleId = LoginUserUtil.getLoginUser().getId();
                tbStoreNoDao.updateStatusByStoreNoAndSaleId(saleId, storeNo);
                tbStoreDao.updateStatus(1,storeNo);
            }else{
            	tbStoreExtendDao.updateByStoreNo(storeNo,1,json,2);
            }
            return result;
        }catch (Exception e){
            logger.error("#ManagerService.auditStatus# error saName={},saNum={},saBankName={},merEmail={},auth={},storeNO={}",
                    saName,saNum,saBankName,merEmail,auth,storeNo,e);
        }
        return "ERROR";
    }

    public CountDealBean countDeal(String auth){
        logger.info("#ManagerService.countDeal# auth={}",auth);
        try{
            long saleId = LoginUserUtil.getLoginUser().getId();
            int startTime = DateUtil.getTodayStartTime();
            int endTime = startTime + 86400;
            return tbOrder.countDealBySaleId(saleId,startTime,endTime);
        }catch (Exception e){
            logger.error("#ManagerService.countDeal# auth={}",auth,e);
        }
        return new CountDealBean();
    }

    public static void main(String[] args){
//        MSStoreBean msStoreBean = new MSStoreBean();
//        msStoreBean.setReg_contact_tel("3");
//        msStoreBean.setFiled1("3");
//        msStoreBean.setMer_name("333");
//        msStoreBean.setMer_short_name("ddd");
//        msStoreBean.setStep(1);
//        String str = JSONObject.toJSON(msStoreBean).toString();
//        MSStoreBean bean = JSONObject.parseObject(str,MSStoreBean.class);
//        System.out.print(bean.getAgent_no());
        ;
        System.out.println(new String(Base64.decodeBase64("oFvcxwfZrQxlisYN4yIPbxmOT8KM")));
    }
}
