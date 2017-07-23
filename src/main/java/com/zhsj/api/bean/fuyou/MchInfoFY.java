package com.zhsj.api.bean.fuyou;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class MchInfoFY {
	private String storeNo;
	private String step;
	
	private String trace_no ;
	private String ins_cd ;//机构号 
	private String link_mchnt_cd;//挂靠商户号
	private String mchnt_name ; //商户名称
	private String mchnt_shortname ; //商户简称
	private String real_name ; //商户真实名称
	private String certif_id ; //法人证件号
	private String certif_id_expire_dt; //格式YYYYMMDD
	private String acnt_type ;//入账账户类型
	private String contact_person ; //联系人
	private String contact_mobile ; //联系人手机号
	private String contact_email ;
	private String contact_phone ; //联系电话
	private String city_cd ; //城市编号
	private String county_cd ;
	private String inter_bank_no ;//联行号
	private String iss_bank_nm ;
	private String acnt_nm ;//入账户名
	private String acnt_no ;//入账账号
	private String set_cd ; //手续费扣率套餐代码
	private String business ;  
	private String settle_amt ; //小额清算金额（单位分）
	private String sign ;
	
	private String tx_set_cd ;
	private String settle_tp ;
	private String tx_flag ;
	
	private String artif_nm ;//法人姓名
	private String acnt_artif_flag ;//法人入账标识(0:非法人入账,1:法人入账)
	private String acnt_certif_tp ;//入账证件类型("0":"身份证","1":"护照","2":"军官证","3":"士兵证","4":"回乡证","5":"户口本","6":"外国护照","7":"其它")
	private String acnt_certif_id ;//入账证件号
//	private String contact_addr ;
	private String th_flag ; //退货标识(0:不能退货,1:可以退货)
	private String wx_flag ;  //微信支付标识(0：关闭微信,1：开通微信)
	private String ali_flag ; //支付宝支付标识(0：关闭支付宝,1：开通支付宝)
	private String wx_set_cd ;  //微信支付扣率
	private String ali_set_cd ; //支付宝支付扣率
	
	private String qpay_flag ; //qq钱包标识
	private String qpay_set_cd ;  //QQ钱包扣率
	private String jdpay_flag ;  //京东支付标识
	private String jdpay_set_cd ; //京东支付扣率
	private String wxapp_flag ; //微信app支付标识
	private String wxapp_set_cd ;  //微信app支付扣率
	private String wxapp_link_mchnt_cd ;//微信app支付挂接商户号
	public String getTrace_no() {
		return trace_no;
	}
	public void setTrace_no(String trace_no) {
		this.trace_no = trace_no;
	}
	public String getIns_cd() {
		return ins_cd;
	}
	public void setIns_cd(String ins_cd) {
		this.ins_cd = ins_cd;
	}
	public String getLink_mchnt_cd() {
		return link_mchnt_cd;
	}
	public void setLink_mchnt_cd(String link_mchnt_cd) {
		this.link_mchnt_cd = link_mchnt_cd;
	}
	public String getMchnt_name() {
		return mchnt_name;
	}
	public void setMchnt_name(String mchnt_name) {
		this.mchnt_name = mchnt_name;
	}
	public String getMchnt_shortname() {
		return mchnt_shortname;
	}
	public void setMchnt_shortname(String mchnt_shortname) {
		this.mchnt_shortname = mchnt_shortname;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getCertif_id() {
		return certif_id;
	}
	public void setCertif_id(String certif_id) {
		this.certif_id = certif_id;
	}
	public String getCertif_id_expire_dt() {
		return certif_id_expire_dt;
	}
	public void setCertif_id_expire_dt(String certif_id_expire_dt) {
		this.certif_id_expire_dt = certif_id_expire_dt;
	}
	public String getAcnt_type() {
		return acnt_type;
	}
	public void setAcnt_type(String acnt_type) {
		this.acnt_type = acnt_type;
	}
	public String getContact_person() {
		return contact_person;
	}
	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}
	public String getContact_mobile() {
		return contact_mobile;
	}
	public void setContact_mobile(String contact_mobile) {
		this.contact_mobile = contact_mobile;
	}
	public String getContact_email() {
		return contact_email;
	}
	public void setContact_email(String contact_email) {
		this.contact_email = contact_email;
	}
	public String getContact_phone() {
		return contact_phone;
	}
	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}
	public String getCity_cd() {
		return city_cd;
	}
	public void setCity_cd(String city_cd) {
		this.city_cd = city_cd;
	}
	public String getCounty_cd() {
		return county_cd;
	}
	public void setCounty_cd(String county_cd) {
		this.county_cd = county_cd;
	}
	public String getInter_bank_no() {
		return inter_bank_no;
	}
	public void setInter_bank_no(String inter_bank_no) {
		this.inter_bank_no = inter_bank_no;
	}
	public String getIss_bank_nm() {
		return iss_bank_nm;
	}
	public void setIss_bank_nm(String iss_bank_nm) {
		this.iss_bank_nm = iss_bank_nm;
	}
	public String getAcnt_nm() {
		return acnt_nm;
	}
	public void setAcnt_nm(String acnt_nm) {
		this.acnt_nm = acnt_nm;
	}
	public String getAcnt_no() {
		return acnt_no;
	}
	public void setAcnt_no(String acnt_no) {
		this.acnt_no = acnt_no;
	}
	public String getSet_cd() {
		return set_cd;
	}
	public void setSet_cd(String set_cd) {
		this.set_cd = set_cd;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getSettle_amt() {
		return settle_amt;
	}
	public void setSettle_amt(String settle_amt) {
		this.settle_amt = settle_amt;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getTx_set_cd() {
		return tx_set_cd;
	}
	public void setTx_set_cd(String tx_set_cd) {
		this.tx_set_cd = tx_set_cd;
	}
	public String getSettle_tp() {
		return settle_tp;
	}
	public void setSettle_tp(String settle_tp) {
		this.settle_tp = settle_tp;
	}
	public String getTx_flag() {
		return tx_flag;
	}
	public void setTx_flag(String tx_flag) {
		this.tx_flag = tx_flag;
	}
	public String getArtif_nm() {
		return artif_nm;
	}
	public void setArtif_nm(String artif_nm) {
		this.artif_nm = artif_nm;
	}
	public String getAcnt_artif_flag() {
		return acnt_artif_flag;
	}
	public void setAcnt_artif_flag(String acnt_artif_flag) {
		this.acnt_artif_flag = acnt_artif_flag;
	}
	public String getAcnt_certif_tp() {
		return acnt_certif_tp;
	}
	public void setAcnt_certif_tp(String acnt_certif_tp) {
		this.acnt_certif_tp = acnt_certif_tp;
	}
	public String getAcnt_certif_id() {
		return acnt_certif_id;
	}
	public void setAcnt_certif_id(String acnt_certif_id) {
		this.acnt_certif_id = acnt_certif_id;
	}
	public String getTh_flag() {
		return th_flag;
	}
	public void setTh_flag(String th_flag) {
		this.th_flag = th_flag;
	}
	public String getWx_flag() {
		return wx_flag;
	}
	public void setWx_flag(String wx_flag) {
		this.wx_flag = wx_flag;
	}
	public String getAli_flag() {
		return ali_flag;
	}
	public void setAli_flag(String ali_flag) {
		this.ali_flag = ali_flag;
	}
	public String getWx_set_cd() {
		return wx_set_cd;
	}
	public void setWx_set_cd(String wx_set_cd) {
		this.wx_set_cd = wx_set_cd;
	}
	public String getAli_set_cd() {
		return ali_set_cd;
	}
	public void setAli_set_cd(String ali_set_cd) {
		this.ali_set_cd = ali_set_cd;
	}
	public String getQpay_flag() {
		return qpay_flag;
	}
	public void setQpay_flag(String qpay_flag) {
		this.qpay_flag = qpay_flag;
	}
	public String getQpay_set_cd() {
		return qpay_set_cd;
	}
	public void setQpay_set_cd(String qpay_set_cd) {
		this.qpay_set_cd = qpay_set_cd;
	}
	public String getJdpay_flag() {
		return jdpay_flag;
	}
	public void setJdpay_flag(String jdpay_flag) {
		this.jdpay_flag = jdpay_flag;
	}
	public String getJdpay_set_cd() {
		return jdpay_set_cd;
	}
	public void setJdpay_set_cd(String jdpay_set_cd) {
		this.jdpay_set_cd = jdpay_set_cd;
	}
	public String getWxapp_flag() {
		return wxapp_flag;
	}
	public void setWxapp_flag(String wxapp_flag) {
		this.wxapp_flag = wxapp_flag;
	}
	public String getWxapp_set_cd() {
		return wxapp_set_cd;
	}
	public void setWxapp_set_cd(String wxapp_set_cd) {
		this.wxapp_set_cd = wxapp_set_cd;
	}
	public String getWxapp_link_mchnt_cd() {
		return wxapp_link_mchnt_cd;
	}
	public void setWxapp_link_mchnt_cd(String wxapp_link_mchnt_cd) {
		this.wxapp_link_mchnt_cd = wxapp_link_mchnt_cd;
	}
	public String getStoreNo() {
		return storeNo;
	}
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	public String getStep() {
		return step;
	}
	public void setStep(String step) {
		this.step = step;
	}
	
	public Map<String,String> toMap(){
        Map<String,String> map = new HashMap<String, String>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
        	if("storeNo".equals(field.getName()) || "step".equals(field.getName())){
        		continue;
        	}
            String obj;
            try {
                obj = (String)field.get(this);
                if(obj!=null && !"".equals(obj)){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
	@Override
	public String toString() {
		return "MchInfoFY [storeNo=" + storeNo + ", mchnt_name=" + mchnt_name
				+ ", real_name=" + real_name + "]";
	}
	
	
	
}
