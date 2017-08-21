package com.zhsj.api.constants;

public enum T1RateCons {
	M0186(0.30, "M0186"),
	M2002(0.31, "M2002"),
	M2003(0.32, "M2003"),
	M0261(0.33, "M0261"),
	M2004(0.34, "M2004"),
	M0136(0.35, "M0136"),
	M0340(0.36, "M0340"),
	M2005(0.37, "M2005"),
	M0325(0.38, "M0325"),
	M2006(0.39, "M2006"),
	M0022(0.40,"M0022"),
	M2007(0.41,"M2007"),
	M0493(0.42,"M0493"),
	M0408(0.43,"M0408"),
	Z0394(0.44,"Z0394"),
	M0068(0.45,"M0068"),
	M0355(0.46,"M0355"),
	M0074(0.47,"M0074"),
	M0072(0.48,"M0074"),
	M0356(0.49,"M0356"),
	M0049(0.50,"M0049"),
	M2008(0.51,"M2008"),
	M0079(0.52,"M0079"),
	M2009(0.53,"M2009"),
	Z0392(0.54,"Z0392"),
	M0069(0.55,"M0069"),
	M1082(0.56,"M1082"),
	M1074(0.57,"M1074"),
	M0406(0.58,"M0406"),
	Z0203(0.59,"Z0203"),
	M0027(0.60,"M0027");  // 待定

    private double type;

    private String desc;

    T1RateCons(double type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public double getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static T1RateCons of(double type) {
        for (T1RateCons elem : T1RateCons.values()) {
            if (type == elem.getType()) {
                return elem;
            }
        }
        return null;
    }
}
