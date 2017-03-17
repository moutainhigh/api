package com.zhsj.api.bean.result;

import com.zhsj.api.bean.StoreAccountBean;

public class StaffMsg implements Comparable<StaffMsg>{

	private StoreAccountBean sab;
	private int roleId;
	private String roleName;
	
	
	
	public StoreAccountBean getSab() {
		return sab;
	}
	public void setSab(StoreAccountBean sab) {
		this.sab = sab;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public int compareTo(StaffMsg smg) {
	    return (!"".equals(this.sab.getOpenId())) && (!"".equals(smg.sab.getOpenId()))?0:(!"".equals(this.sab.getOpenId()))?1: 
	    	(!"".equals(smg.sab.getOpenId()))?1:-1;
	}
	
	
}
