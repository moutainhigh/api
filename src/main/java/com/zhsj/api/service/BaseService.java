package com.zhsj.api.service;

import com.zhsj.api.bean.CityCodeBean;
import com.zhsj.api.bean.BusinessTypeBean;
import com.zhsj.api.dao.TBCityCodeDao;
import com.zhsj.api.dao.TBBusinessTypeDao;
import com.zhsj.api.util.SSLUtil;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcg on 16/12/5.
 */
@Service
public class BaseService {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(BaseService.class);

    @Autowired
    TBCityCodeDao tbCityCodeDao;
    @Autowired
    TBBusinessTypeDao tbBusinessTypeDao;

    public List<CityCodeBean> getCityCode(String cityCode){
        logger.info("#BaseService.getCityCode# cityCode={}",cityCode);
        List<CityCodeBean> cityCodeBeanList= new ArrayList<>();
        try {
            cityCodeBeanList = tbCityCodeDao.getCityCode(cityCode);
        }catch (Exception e){
            logger.error("AbcService e={}",e.getMessage(),e);
        }

        return cityCodeBeanList;
    }
    
    
    public List<BusinessTypeBean> getBusinessTypeListByParentId(int id){
    	logger.info("#BaseService.getMgtListByParentId# id={}",id);
    	List<BusinessTypeBean> businessTypeBeanList = new ArrayList<>();
    	try {
    		businessTypeBeanList = tbBusinessTypeDao.getListById(id);
		} catch (Exception e) {
			logger.error("AbcService e={}",e.getMessage(),e);
		}
    	return businessTypeBeanList;
    }
    
    public int addBusinessType(BusinessTypeBean businessTypeBean){
    	logger.info("#BaseService.addBusinessType #businessTypeBean",businessTypeBean);
    	int index = 0;
    	try {
			index = tbBusinessTypeDao.insert(businessTypeBean);
		} catch (Exception e) {
			logger.error("AbcService e={}",e.getMessage(),e);
		}
    	return index;
    }




    public static void main(String[] args){
        BaseService abcService = new BaseService();
//        System.out.println(abcService.abc());
//        String token = "FnGbTVWamObKS8FdcRofrtLtK9Hv51PPbEGywRKx81iDlsJxQt0HlPUtzXe2fse9n2RiYhTv8ky-6vneefTYID_f9cMtVpx0GA4EhjzN72Nu35adMU-Oxx5sdONSRCqrMYZfAGAPBC";
//        System.out.println(abcService.getUserInfo(token, "oFvcxwdPHqk7HaHqSxrdSMg5lAKk"));
    }
}
