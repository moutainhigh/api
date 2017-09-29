package com.zhsj.api.util.horn;

public interface CloudHornConst {

	final static String BIND = "http://101.201.55.12/bind.php";
	final static String MSGSUBMIT="http://101.201.55.12/index.php";//消息提交
	//id=SPEAKERID&price=PRICEVALUE&token=TOKEN
	//Content-Type: application/x-www-form-urlencoded;charset=utf-8
    final static String SETVOLUMNE ="http://101.201.55.12/index.php";//设置音量
	//Content-Type: application/x-www-form-urlencoded;charset=utf-8
	//id=SPEAKERID&vol=VOLVALUE&token=TOKEN
    final static String HISTROY = "http://101.201.55.12/list.php";
    final static String TOKEN = "70095533";
}
