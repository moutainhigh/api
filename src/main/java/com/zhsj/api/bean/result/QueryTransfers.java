package com.zhsj.api.bean.result;

import java.util.HashMap;
import java.util.Map;

import com.zhsj.api.util.Md5;
import com.zhsj.api.util.MtConfig;
import com.zhsj.api.util.XMLBeanUtils;

public class QueryTransfers {

	private String nonce_str;//随机字符串
	private String partner_trade_no;//商户订单号
	private String mch_id = MtConfig.getProperty("mchid", "");//商户号
	private String appid = MtConfig.getProperty("transfer_appId", "");//APPid
	private String key = MtConfig.getProperty("key", "");//key
	
	
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getPartner_trade_no() {
		return partner_trade_no;
	}
	public void setPartner_trade_no(String partner_trade_no) {
		this.partner_trade_no = partner_trade_no;
	}
	
	@Override
	public String toString(){
		String param = "appid="+this.appid+"&mch_id="+this.mch_id+"&nonce_str="+this.getNonce_str()+"&partner_trade_no="+this.getPartner_trade_no()
				+"&key="+this.key;
		String sign = Md5.encrypt(param).toUpperCase();
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", this.appid);
		map.put("mch_id", this.mch_id);
		map.put("nonce_str", this.getNonce_str());
		map.put("partner_trade_no", this.getPartner_trade_no());
		map.put("sign", sign);
		return XMLBeanUtils.mapToXml(map);
	}
	
}
