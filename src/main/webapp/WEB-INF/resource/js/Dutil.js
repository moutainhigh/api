/**
 * Created by Administrator on 2016/5/7.
 */
Date.prototype.Format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
};
String.prototype.toDate=function () {
    var re = /^[0-9]*[1-9][0-9]*$/;
    if (re.test(this))
    {
        return new Date(parseInt(this));
    }else{
        if(this!=""){
            var da1 = this.replace(/-/g, "/");
            var date = new Date(da1);
            return date;
        }else{
            return new Date();
        }

    }
};
var DateUtil={
    strToDate: function (da) {
        var da1 = da.replace(/-/g, "/");
        var date = new Date(da1);
        return date;
    }
};

var time_range = function (beginTime, endTime, nowTime) {
    var strb = beginTime.split (":");
    if (strb.length != 2) {
        return false;
    }

    var stre = endTime.split (":");
    if (stre.length != 2) {
        return false;
    }
    var strn = nowTime.split (":");
    if (stre.length != 2) {
        return false;
    }
    var b = new Date ();
    var e = new Date ();
    var n = new Date ();

    b.setHours (strb[0]);
    b.setMinutes (strb[1]);
    e.setHours (stre[0]);
    e.setMinutes (stre[1]);
    n.setHours (strn[0]);
    n.setMinutes (strn[1]);

    if (n.getTime () - b.getTime () > 0 && n.getTime () - e.getTime () < 0) {
        return true;
    } else {
        return false;
    }
}

var time_compare = function(time1, time2){
    var t1Arr = time1.split(":");
    var t2Arr = time2.split(":");
    var t1 = new Date();
    var t2 = new Date();
    t1.setHours(t1Arr[0]);
    t1.setMinutes(t1Arr[1]);
    t2.setHours(t2Arr[0]);
    t2.setMinutes(t2Arr[1]);
    var diff = parseInt((t1.getTime() - t2.getTime())/1000/60) ;
    return diff;
};