package com.zhsj.api.bean.result;

import com.zhsj.api.bean.StoreAccountBean;

public class StaffMsg {

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
	
	
	
}
