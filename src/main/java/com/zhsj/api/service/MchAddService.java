package com.zhsj.api.service;

import com.alibaba.fastjson.JSONObject;
import com.zhsj.api.bean.*;
import com.zhsj.api.dao.*;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.login.LoginUserUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lcg on 17/1/28.
 * 商户进件专用
 */
@Service
public class MchAddService {
    Logger logger = LoggerFactory.getLogger(MchAddService.class);

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
    
    
    public String addMch(String storeName,String storeAccount,String storeNo,String auth){
        logger.info("#MchAddService.addMch# storeName={},storeAccount={},storeNo={},auth={}"
                , storeName, storeAccount, storeNo, auth);
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
                }else{
                	return "NEXT";
                }
            }
            long saleId = LoginUserUtil.getLoginUser().getId();
            int num = tbStoreNoDao.updateStatusByStoreNo(saleId,storeNo);
            if(num != 1){
                return "商家编号不存在或是已经使用";
            }
            Long orgId = tbAccountBindOrgDao.getOrgIdByAccountId(saleId);
            OrgBean orgBean = tbOrgDao.getById(orgId);

            //保存帐号，密码帐号，保存商家，
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
            
            MchInfoAddBean mchInfo = new MchInfoAddBean();
            mchInfo.setStoreName(storeName);
            mchInfo.setShortName(storeName);
            mchInfo.setStoreNo(storeNo);
            mchInfo.setStep(1);
            String json = JSONObject.toJSON(mchInfo).toString();
            tbStoreExtendDao.insert(storeNo,1,json);
            return "SUCCESS";
        }catch (Exception e){
            logger.error("#MchAddService.addMch# storeName={},storeAccount={},storeNo={},auth={}"
                    ,storeName,storeAccount,storeNo,auth,e);
        }
        return "ERROR";
    }
    
    
    public MchInfoAddBean getByStoreNo(String storeNo){
    	logger.info("#MchAddService.getByStoreNo# storeNo={}",storeNo);
    	MchInfoAddBean info = new MchInfoAddBean();
    	try{
    		String data = tbStoreExtendDao.getDataByStoreNo(storeNo,1);
            if(StringUtils.isEmpty(data)){
            	return info;
            }
            info = JSONObject.parseObject(data,MchInfoAddBean.class);
    	}catch(Exception e){
    		logger.error("#MchAddService.getByStoreNo# storeNo={}",storeNo,e);
    	}
    	return info;
    }
    
    

    public String updateMch(MchInfoAddBean info,String auth){
        logger.info("#MchAddService.updateMch# info={},auth={}",info,auth);
        try{
        	int num = tbStoreDao.updateStore(info);
        	if(num <= 0){
        		return "保存商家信息失败";
        	}
        	
            String data = tbStoreExtendDao.getDataByStoreNo(info.getStoreNo(),1);
            MchInfoAddBean mchInfo = JSONObject.parseObject(data,MchInfoAddBean.class);
            
            mchInfo.setProvince(info.getProvince());
            mchInfo.setCity(info.getCity());
            mchInfo.setCounty(info.getCounty());
            mchInfo.setStreet(info.getStreet());
            mchInfo.setAddress(info.getAddress());
            mchInfo.setLat(info.getLat());
            mchInfo.setLon(info.getLon());
            mchInfo.setBusinessType(info.getBusinessType());
            mchInfo.setContactsPeople(info.getContactsPeople());
            mchInfo.setPhone(info.getPhone());
            mchInfo.setEmail(info.getEmail());
            mchInfo.setIdCard(info.getIdCard());
            mchInfo.setIntro(info.getIntro());
        	mchInfo.setStep(2);
            String json = JSONObject.toJSON(mchInfo).toString();
            //更新扩展
            tbStoreExtendDao.updateByStoreNo(info.getStoreNo(),1,json,2);
            return "SUCCESS";
        }catch (Exception e){
            logger.error("#MchAddService.updateMch# info={},auth={}",info,auth,e);
        }
        return "ERROR";
    }
    
    public String settleMch(MchInfoAddBean info,String auth) {
        logger.info("#MchAddService.settleMch# info={},auth={}",info,auth);
        try{
        	 Integer status = tbStoreNoDao.getStatusByStoreNo(info.getStoreNo());
             if(status == null){
                 return "商家编号不存在";
             }
             if(status == 2){
                 return "SUCCESS";
             }
            String data = tbStoreExtendDao.getDataByStoreNo(info.getStoreNo(),1);
            
            MchInfoAddBean mchInfo = JSONObject.parseObject(data,MchInfoAddBean.class);
            mchInfo.setBankAccount(info.getBankAccount());
            mchInfo.setBankName(info.getBankName());
            mchInfo.setAccountName(info.getAccountName());
            mchInfo.setAccountIdCard(info.getAccountIdCard());
            mchInfo.setAccountPhone(info.getAccountPhone());
            mchInfo.setWxRate(info.getWxRate());
            mchInfo.setAliRate(info.getAliRate());
            
            String json = JSONObject.toJSON(mchInfo).toString();
            //更新扩展
            int num = tbStoreExtendDao.updateByStoreNoAndStep(info.getStoreNo(), 1, json,3,2);
            if(num <=0){
            	return "不要重复提交";
            }
            //如果已经提交，不再提交
            String result = minshengService.openAccountV2(mchInfo);
            if("SUCCESS".equals(result)){
                long saleId = LoginUserUtil.getLoginUser().getId();
                tbStoreNoDao.updateStatusByStoreNoAndSaleId(saleId, info.getStoreNo());
                tbStoreDao.updateStatus(1,info.getStoreNo());
            }else{
            	tbStoreExtendDao.updateByStoreNo(info.getStoreNo(),1,json,2);
            }
            return result;
        }catch (Exception e){
        	logger.error("#MchAddService.settleMch# info={},auth={}",info,auth,e);
        }
        return "ERROR";
    }
    
    
    public static void main(String[] args){
        System.out.println(new String(Base64.decodeBase64("oFvcxwfZrQxlisYN4yIPbxmOT8KM")));
    }
}
