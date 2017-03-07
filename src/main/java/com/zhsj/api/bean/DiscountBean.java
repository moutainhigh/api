package com.zhsj.api.bean;


/**
 * Created by lcg on 17/2/4.
 */
public class DiscountBean {
    private long id;
    private String name;
    private String content;
    private String storeNo;
    private int startTime;
    private int endTime;
    private int type;
    private int aType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

	public int getaType() {
		return aType;
	}

	public void setaType(int aType) {
		this.aType = aType;
	}
    

    
}
