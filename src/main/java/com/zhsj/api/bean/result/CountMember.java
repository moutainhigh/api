package com.zhsj.api.bean.result;
/**
 * 会员统计
 *
 */
public class CountMember implements Comparable{

	private String headImg;
	private String nick;
	private int count;
	private Double sum;
	private long time;
	//
	private long userId;
	
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Double getSum() {
		return sum;
	}
	public void setSum(Double sum) {
		this.sum = sum;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Override
	public int compareTo(Object o) {
		CountMember cm = (CountMember)o;
		return this.getTime() > cm.getTime()?-1:(this.getTime() == cm.getTime()?0:1);
	}
	
	
}
