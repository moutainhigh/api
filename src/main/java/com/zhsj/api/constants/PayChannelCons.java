package com.zhsj.api.constants;

public enum PayChannelCons{
	WSY_CHANNEL(5, "d7772b0674a318d5"),  //微收银
	YDC_CHANNEL(6, "6fb5e6087eb1b2dc"),  //商亿
	XY_CHANNEL(7, "5a36dedd4bea2543"),   //祥云软件
	SY_CHANNEL(8, "59f51fd0297e236d");   //盛宴软件

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
