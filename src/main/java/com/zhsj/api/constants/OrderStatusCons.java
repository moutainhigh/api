package com.zhsj.api.constants;

public enum OrderStatusCons {
	SUCCESS(1, "SUCCESS"),  //支付成功
	USERPAYING(0, "USERPAYING"),
	REFUND(3, "REFUND"), //转入退款
	REFUND_SUCCESS(4, "REFUND_SUCCESS"), //退款成功
	REFUND_FAIL(5, "REFUND_FAIL"), //退款失败
	PAYERROR(2, "PAYERROR");  //支付失败
	


    private int type;

    private String desc;

    OrderStatusCons(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static OrderStatusCons of(int type) {
        for (OrderStatusCons elem : OrderStatusCons.values()) {
            if (type == elem.getType()) {
                return elem;
            }
        }
        return null;
    }
}
