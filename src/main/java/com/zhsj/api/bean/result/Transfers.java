package com.zhsj.api.bean.result;


import java.util.HashMap;
import java.util.Map;





import com.zhsj.api.util.MD5Util;
import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.XMLBeanUtils;
/*
 * 
 *转账的msg_Param，以及签名的参数
 */
public class Transfers {

	private String mch_appid = MtConfig.getProperty("transfer_appId", "");//公众账号appid
	private String mchid = MtConfig.getProperty("mchid", "");//商户号
	private String device_info = "";//设备号
	private String nonce_str = "";//随机字符串
	private String sign = "";//签名
	private String partner_trade_no = "";//商户订单号
	private String openid = "";//用户openid
	private String check_name = "OPTION_CHECK";//校验用户姓名选项
	private String re_user_name = "";//收款用户姓名
	private int amount;//金额
	private String desc = "商户提现至零钱";//企业付款描述信息
	private String spbill_create_ip = "";//Ip地址
	private String key = MtConfig.getProperty("key", "");
	
	public String getMch_appid() {
		return mch_appid;
	}
	public void setMch_appid(String mch_appid) {
		this.mch_appid = mch_appid;
	}
	public String getMchid() {
		return mchid;
	}
	public void setMchid(String mchid) {
		this.mchid = mchid;
	}
	public String getDevice_info() {
		return device_info;
	}
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPartner_trade_no() {
		return partner_trade_no;
	}
	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getCheck_name() {
		return check_name;
	}
	public void setCheck_name(String check_name) {
		this.check_name = check_name;
	}
	public String getRe_user_name() {
		return re_user_name;
	}
	public void setRe_user_name(String re_user_name) {
		this.re_user_name = re_user_name;
	}
    
	
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	/**
	 * 
	 * private String mch_appid = "";//公众账号appid *
	private String mchid = "";//商户号  *
	private String device_info = "";//设备号  *
	private String nonce_str = "";//随机字符串  *
	private String sign = "";//签名
	private String partner_trade_no = "";//商户订单号    *
	private String openid = "";//用户openid   *
	private String check_name = "";//校验用户姓名选项   *
	private String re_user_name = "";//收款用户姓名    *
	private String amount = "";//金额    *
	private String desc = "";//企业付款描述信息   *
	private String spbill_create_ip = "";//Ip地址  *
	 */
	@Override
	public String toString() {
		String param = "amount="+this.getAmount()+"&check_name="+this.getCheck_name()+"&desc="+this.getDesc()+"&mch_appid="+this.getMch_appid()
				+"&mchid="+this.getMchid()+"&nonce_str="+this.getNonce_str()+"&openid="+this.getOpenid()+"&partner_trade_no="+this.getPartner_trade_no()
				+"&re_user_name="+this.getRe_user_name()+"&spbill_create_ip="+this.getSpbill_create_ip();
		String sign = (MD5Util.encode(param+"&key="+this.key)).toUpperCase();
		Map<String, String> map = new HashMap<String, String>();
		map.put("amount", String.valueOf(this.getAmount()));
		map.put("check_name", this.getCheck_name());
		map.put("desc", this.getDesc());
		map.put("mch_appid", this.getMch_appid());
		map.put("mchid", this.getMchid());
		map.put("nonce_str", this.getNonce_str());
		map.put("openid", this.getOpenid());
		map.put("partner_trade_no", this.getPartner_trade_no());
		map.put("re_user_name", this.getRe_user_name());
		map.put("spbill_create_ip", this.getSpbill_create_ip());
		map.put("sign", sign);
		return  XMLBeanUtils.mapToXml(map);
	}
	
  
	public static void main(String[] args) {
		String str = "amount=100&check_name=OPTION_CHECK&desc=提现测试&mch_appid=wx79bd044fd98536f4&mchid=1273081001&nonce_str=d23c086dfa6545c7a374a7cffbb08751&openid=o5pmesyob9P9Otj-jl-U3ETnArlY&partner_trade_no=28377075170326369232897&re_user_name=%E8%AE%B8%E6%9E%97%E5%88%9B&spbill_create_ip=114.215.223.220&key=wanwutongkejibeijing888888888888";
		System.err.println(Md5.encrypt(str).toUpperCase());
		System.err.println(MD5Util.encode(str).toUpperCase());
	}
	
}
