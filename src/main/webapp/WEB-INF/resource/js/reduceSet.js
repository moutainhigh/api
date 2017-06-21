/**
 * Created by hctym on 2017/2/13.
 */

$(function(){
    //开始时间和结束时间
    var option = {
        theme: 'ios',
        lang: 'zh',
        display: 'bottom',
        max: new Date(2030, 1, 1),

    };
    //开始时间
    $('#startTime').mobiscroll().datetime($.extend({
        onClose:function(){
            if($('#startTime').val() != ''){
                $('#startTime').css({
                    "font-size":".8em",
                    "line-height":"2.5"
                });
            }
        }
    },option));
    //结束时间
    $('#endTime').mobiscroll().datetime($.extend({
        onClose:function(){
            if($('#endTime').val() != ''){
                $('#endTime').css({
                    "font-size":".8em",
                    "line-height":"2.5"
                });
            }
        }
    },option));
    //遮罩层点击隐藏
    $(".wwt-select-store-wrapper").on("click",function(){
    	$(".wwt-select-store-true").css({
            top:'-250px'
        });
        var that =this;
        var t = setTimeout(function(){
            $(that).hide();
            clearTimeout(t);
        },500);
    });
    //点击适用门店
    $(".select-store").on("click",function(){
        $(".wwt-select-store-wrapper").show();
        var t = setTimeout(function(){
            $(".wwt-select-store-true").css({
                top:'30%'
            });
            clearTimeout(t);
        },100);
    });
    //重置
    $(".wwt-store-reset").on("click",function(e){
        $(".wwt-storelist").find("p").removeClass("wwt-selectStore").addClass("wwt-defaultStore");
        $(".select-store").find("span").text("请选择门店");
        e.stopPropagation();
    });
    var storeList = [];//定义全局的门店数组(用于当保存优惠)
    //确定
    $(".wwt-store-confirm").on("click",function(e){
        $(".wwt-select-store-true").css({
            top:'-250px'
        });
        var t = setTimeout(function(){
            $(".wwt-select-store-wrapper").hide();
            clearTimeout(t);
        },500);
        
        var ps = $(".wwt-storelist").find("p"),//所有的门店
            len = ps.length,
            count = 0,//用于判断显示
            storeShow = $(".select-store").find("span");//根据storeShow显示 门店(全部门店或者是部门门店)
        storeList = [];//赋空值
        for(var i =0;i < len;i++){
            var pspan = $($(".wwt-storelist").find("p")[i]);
            if(pspan.attr("class").indexOf("wwt-selectStore") > -1){
                count++;
                storeList.push(pspan.attr("data-id"));
            }
        }
        if(count == len){
            storeShow.text("全部门店");
        }else if(count == 0){
            storeShow.text("请选择门店");
        }else if(count == len-1 && $("#_all").attr("class").indexOf("wwt-defaultStore") > -1){
        	storeShow.text("全部门店");
        }else{
            storeShow.text("部分门店");
        }
        e.stopPropagation();
//        console.log(storeList);
    });
    //选择门店
    $(".wwt-storelist").find("p").on("click",function(e){
    	if($(this).attr("id") == "_all"){
    		if($(this).attr("class").indexOf("wwt-selectStore") > -1){
                $($(".wwt-storelist").find("p")).removeClass("wwt-selectStore").addClass("wwt-defaultStore");
            }else{
                $($(".wwt-storelist").find("p")).addClass("wwt-selectStore").removeClass("wwt-defaultStore");
            }
    	}else{
    		 if($(this).attr("class").indexOf("wwt-selectStore") > -1){
    	            $(this).removeClass("wwt-selectStore").addClass("wwt-defaultStore");
    	            $("#_all").removeClass("wwt-selectStore").addClass("wwt-defaultStore");
    	        }else{
    	            $(this).addClass("wwt-selectStore").removeClass("wwt-defaultStore");
    	        }
    	}
    	
        e.stopPropagation();
    });
    //定义优惠类型type
    var type = 0,
        reduceList = [];//优惠列表
    //点击选中
    $(".radio").on("click",function(){
        if($(this).attr("class").indexOf("sel") != -1){//如果选中。需要再次点击 。(用于重新选择)
            $(".radio").removeClass("default").removeClass("sel").addClass("no");
            $("#newRules").empty();
        }else if($(this).attr("class").indexOf("no") != -1){//开始选择。让选中的红色 。其他的至灰
            $(".radio").removeClass("sel").removeClass("no").addClass("default");
            $(this).removeClass("default").removeClass("no").addClass("sel");
            var index = $(".radio").index($(this));
//            console.log(index+1);
            type = index+1;
        }
        reduceList = [];
    });

    //添加规则
    $("#add").on("click",function(){
        var selrule = $(".sel").parent();
        var htm = selrule.html();
        if(htm == undefined){
            jalert.show('请选择一个优惠规则');
            return false;
        }else{
            var checkbox = $("<input>").attr("type","checkbox").attr("class","delete");
            checkbox.on("click",function(){
                $(this).parent().remove();
            });
            var s = "";
            if(selrule.attr('class') == 'lj') {
                s = ' <span class="fm">满</span> '
                    + '<input type="number"> '
                    + '<span>元，立减</span> '
                    + '<input type="number"> '
                    + '<span>元</span>';

            }else if(selrule.attr('class') == 'sj'){
                s = ' <span class="fm">满</span> '
                    + '<input type="number"> '
                    + '<span>元，随机减</span> '
                    + '<input type="number"> '
                    + '<span>至</span> '
                    + '<input type="number"> '
                    + '<span>元</span>';
            }else if(selrule.attr('class') == 'zk'){
                s = ' <span class="fm">满</span> '
                    + '<input type="number"> '
                    + '<span>元，</span> '
                    + '<input type="number"> '
                    + '<span>折</span>';
            }
            $("#newRules").append($("<p>").append(checkbox).append(s));
        }
    });
    //确定按钮
    $("#confirm").on("click",function(){
        //alert('确定');

//        console.log($("#aname").val())//
        if($("#aname").val() == ''){
            jalert.show("请输入活动名称");
            return false;
        }
        if(storeList.length == 0){
            jalert.show("请选择门店");
            return false;
        }
//        console.log(storeList);
        if($("#startTime").val() == ''){
            jalert.show("请选择开始时间");
            return false;
        }
        if($("#endTime").val() == ''){
            jalert.show("请选择结束时间");
            return false;
        }
        reduceList = [];//清空
        var rulesp = $("#newRules").find("p");
        len = rulesp.length;
        for(var i = 0;i<len;i++){
            var inputs = $(rulesp[i]).find("input[type=number]");
            //定义规则的对象
            var reduce = {
                type:type,//规则类型1、立减2、随机减3、折扣
                rule:[]//满多少。(数组p1、满多少p2、第一个参数限制p3、第二个参数限制)
            };
            for(var j =0,inputLen = inputs.length;j<inputLen;j++){
                if($(inputs[j]).val() == ''){
                    jalert.show('请填写完整');
                    reduceList = [];
                    return false;
                }else{
                    reduce.rule[j] = $(inputs[j]).val();
                }
            }
            reduceList.push(reduce);
        }
        if(reduceList.length == 0){
            jalert.show("请添加优惠规则");
            return false;
        }
//        console.log(reduceList);//优惠规则list
        var ruleList=[];
        for(var i=0;i<reduceList.length;i++){
        	var obj = reduceList[i].rule;
        	var rule = {};
        	rule.expendAmount = obj[0];
        	rule.discount1 = obj[1];
        	if(type==2){
        		rule.discount2 = obj[2];
        	}else{
        		rule.discount2 = "0";
        	}
        	ruleList.push(rule);
        }
//        console.log(ruleList);
        var data = {
            "name":$("#aname").val(),//活动名称
            "storeNos":storeList.toString(),//选中门店
            "startTime":$("#startTime").val(),//开始时间
            "endTime":$("#endTime").val(),//结束时间
            "type":type,
            "auth":$("#auth").val(),
            "rule":JSON.stringify(ruleList)//规则
        };
//        console.log(data);
        $.post("./addDiscount",data,function(obj){
        	if(obj.code == 0){
        		location.href = "../discount/discountActivity?auth="+$("#auth").val();
        	}else{
        		jalert.show(obj.msg);
        	}
         });

    });
});
