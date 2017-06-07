package com.zhsj.api.bean.jpush;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.util.Arith;
import com.zhsj.api.util.DateUtil;

public class PaySuccessBean {
	private int cmd;
	private String no;
	private String time;
	private String pt;//支付方式
	private String am;//实收
	private String pm;//应收
	private String url; //详情
	private String st;
	private String nt; //播放内容
	private String code;
	private String qr;//二维码
	private String dc;

	public int getCmd() {
		return cmd;
	}
	public void setCmd(int cmd) {
		this.cmd = cmd;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
	public String getAm() {
		return am;
	}
	public void setAm(String am) {
		this.am = am;
	}
	public String getPm() {
		return pm;
	}
	public void setPm(String pm) {
		this.pm = pm;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getNt() {
		return nt;
	}
	public void setNt(String nt) {
		this.nt = nt;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getQr() {
		return qr;
	}
	public void setQr(String qr) {
		this.qr = qr;
	}
	
	public String getDc() {
		return dc;
	}
	public void setDc(String dc) {
		this.dc = dc;
	}
	public PaySuccessBean toBean(OrderBean bean,String qrcode,String uri){
		if(bean == null){
			return null;
		}
		PaySuccessBean pb = new PaySuccessBean();
		pb.setCmd(1);
		pb.setNo(bean.getOrderId());
		pb.setTime(DateUtil.getTime(bean.getCtime()*1000));
		pb.setPt("1".equals(bean.getPayMethod())?"微信":"支付宝");
		pb.setPm(bean.getPlanChargeAmount()+"");
		pb.setAm(bean.getActualChargeAmount()+"");
		pb.setUrl(uri+"cashier/orderDetail?id="+bean.getId());
		pb.setSt(bean.getStatus()==1?"成功":"");
		pb.setCode(bean.getAccountId()+""+((int)Arith.mul(bean.getPlanChargeAmount(),100)));
		pb.setQr(qrcode);
		pb.setNt(pb.getPt()+"收款"+bean.getPlanChargeAmount()+"元");
		pb.setDc("");
		return pb;
	}
	
	@Override
	public String toString() {
		return "PaySuccessBean [cmd=" + cmd + ", no=" + no + ", time=" + time
				+ ", pt=" + pt + ", am=" + am + ", pm=" + pm + ", url=" + url
				+ ", st=" + st + "]";
	}
	
}
