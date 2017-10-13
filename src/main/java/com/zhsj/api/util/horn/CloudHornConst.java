package com.zhsj.api.util.horn;

public interface CloudHornConst {

	String HOST = "http://101.201.55.12/";
	final static String BIND = HOST+"bind.php";
	final static String MSGSUBMIT= HOST+"add.php";//消息提交
	//id=SPEAKERID&price=PRICEVALUE&token=TOKEN
	//Content-Type: application/x-www-form-urlencoded;charset=utf-8
    final static String SETVOLUMNE = HOST+"add.php";//设置音量
	//Content-Type: application/x-www-form-urlencoded;charset=utf-8
	//id=SPEAKERID&vol=VOLVALUE&token=TOKEN
    final static String HISTROY = HOST+"list.php";
    
    final static String LAST = HOST+"get_last.php";
    
    final static String BINDLIST = HOST+"list_bind.php";
    //http:// 101.201.55.12/get_last.php?id=SPEAKERID&uid=USERID&token=TOKEN
    //http://101.201.55.12/get_last.php?id=3998
//    final static String TOKEN = "70095533";
//    final static String TOKEN = "100156953906";
    final static String TOKEN = "100176441368";
}
