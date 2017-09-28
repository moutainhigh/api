package com.zhsj.api.util.print;

public class CloudPrinter {

	
	/**
	 * 操作类型 state 查询状态 send 请求发送数据
	 */
	public static final String ACTION = "action";
	/**
	 * 查询
	 */
	public static final String STATE = "state";
	/**
	 * 发送数据
	 */
	public static final String SEND = "send";
	/**
	 * secretkey码
	 * 请参考打印机厂家提供的厂商密码
	 */
//	public static final String SECRETKEY = "rego-test";
	public static final String SECRETKEY = "rego-yn-20150826";
	/**
	 * 设备id 唯一的
	 * 
	 */
	public static final String DEVICE_ID = "device_id";
	/**
	 * 客户标识字符串，出厂时为每个用户分配一个字符串
	 */
	public static final String SECRET = "secretkey";
	/**
	 * 时间戳
	 */
	public static final String TIMESTAMP = "timestamp";
	/**
	 * 请求的url
	 */
//	public static final String URL = "http://cloudprint.easyprt.com/o2o-print/print.php?";
	public static final String URL = "http://121.199.68.96/o2o-print/print.php?";
	/**
	 * 字符编码
	 */
	public static final String CHARSET = "gb2312";
	/**
	 * 签名
	 */
	public static final String SIGN = "sign";
	
	// 发送打印数据
	
	/**
	 * 微信title 两次回车换行
	 */
	public static final String WEIXIN_TITLE = "微信支付凭证\r\n\r\n";
	/**
	 * 微信支付的url
	 */
	public static final String WEIXIN_PAY = "http://www.baidu.com?name=tom&age=18";
	/**
	 * 瑞工科技公司
	 */
	public static final String COMPANY_NAME = "北京瑞工科技有限公司\r\n";
	
	/**
	 * 停车券 两次回车换行
	 */
	public static final String  PARKING_TICKET = "智慧商街\r\n\r\n";
	/**
	 * 微信扫一扫
	 */
	public static final String  SWEEP = "微信扫一扫可保存本券\r\n";
	
	/**
	 * 二二维码 停车券url
	 */
	public static final String  PARKING_TICKET_URL = "http://www.baidu.com";
	/**
	 * 注:.....
	 */
	public static final String  DESCRIPTIONDESCRIPTION = "注:智慧商街test-demo智慧商街test-demo智慧商街test-demo智慧商街test-demo智慧商街test-demo智慧商街test-demo智慧商街test-demo\r\n\r\n\r\n\r\n\r\n";
	
	// 发送打印机指令
	
	/**
	 * 字体格式
	 * 设置title
	 * 1b6101 居中 
	 * 1b2128 倍宽倍高 
	 * 1b4501 加粗 
	 * 1d2111 字体大小
	 * 1b337f 行高
	 */
	public static final String  TITLESE_TTING = "1b401b61011B2108";
	public static final String  BOTTOM_TTING = "1b61011B2100";
	/**
	 * 设置内容格式
	 * 1b6101 居中 
	 * 1b2100 取消倍宽倍高 
	 * 1b4501 取消加粗 
	 * 1d2111 字体默认大小
	 * 1b332f 行高
	 */
	public static final String  CONTENT_SETTING = "1b61001B2100";
	/**
	 * 设置公司内容格式
	 * 1b6101 居中 
	 * 1b2100 取消倍宽倍高 
	 * 1b4501 加粗 
	 * 1d2111 字体默认大小
	 */
	public static final String  COMPANY_TNAME_SETTING = "1b61011b21281b45011d2100";
	/**
	 * 设置公司内容格式
	 * 1b6101 居中 
	 * 1d5a02 打印二维码
	 * 1b5a004c06 二维码类型 和 大小 
	 */
	public static final String  QRCODE_SETTING = "1b61011d5a021b5a004c06";
	
	/**
	 * 打印机格式初始化
	 */
	public static final String  PRINTER_INIT = "1b400d0a";
	/**
	 * 16进制数
	 */
	public static final String  HEXSTRING = "0123456789ABCDEF";
	
	public static void main(String[] args) {
		
	}
}
