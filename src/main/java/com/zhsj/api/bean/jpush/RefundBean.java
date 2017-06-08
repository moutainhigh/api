package com.zhsj.api.bean.jpush;

import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.util.DateUtil;

public class RefundBean {
	private int cmd;
	private String no;
	private String time;
	private String pt;//支付方式
	private String rm;//实收
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
	public String getRm() {
		return rm;
	}
	public void setRm(String rm) {
		this.rm = rm;
	}
	@Override
	public String toString() {
		return "RefundBean [cmd=" + cmd + ", no=" + no + ", time=" + time
				+ ", pt=" + pt + ", rm=" + rm + "]";
	}
	public RefundBean toBean(OrderBean bean){
		if(bean == null){
			return null;
		}
		RefundBean pb = new RefundBean();
		pb.setCmd(3);
		pb.setNo(bean.getOrderId());
		pb.setTime(DateUtil.getTime(bean.getCtime()*1000));
		pb.setPt("1".equals(bean.getPayMethod())?"微信":"支付宝");
		pb.setRm(bean.getRefundMoney()+"");
		return pb;
	}
}
