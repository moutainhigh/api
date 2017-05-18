package com.zhsj.api.bean;

/**
 * 第二版
 * @author lincg
 *
 */
public class ModuleBean {
	private long id;
	private String url;
	private long parentId;
	private String displayName;
	private int type;
	private String iconUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	@Override
	public String toString() {
		return "ModuleBean [id=" + id + ", url=" + url + ", parentId=" + parentId
				+ ", displayName=" + displayName + ", type=" + type + "]";
	}
	
	
}
