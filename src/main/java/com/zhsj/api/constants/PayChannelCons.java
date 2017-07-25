package com.zhsj.api.constants;

public enum PayChannelCons {
	WSY_CHANNEL(5, "d7772b0674a318d5"),  //
	YDC_CHANNEL(6, "6fb5e6087eb1b2dc");  //

    private int type;

    private String desc;

    PayChannelCons(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static PayChannelCons of(int type) {
        for (PayChannelCons elem : PayChannelCons.values()) {
            if (type == elem.getType()) {
                return elem;
            }
        }
        return null;
    }
}
