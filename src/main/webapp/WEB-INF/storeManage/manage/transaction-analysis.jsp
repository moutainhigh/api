<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>交易分析</title>
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" id="viewport" name="viewport">
    <meta name="apple-mobile-web-app-status-bar-style" content="black-translucent">
    <meta content="telephone=no,email=no" name="format-detection">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <link rel="stylesheet" href="../resource/css/app/base.css"/>
    <link rel="stylesheet" href="../resource/css/app/store-manager.css"/>
    <link rel="stylesheet" href="../resource/css/app/people-list.css"/>
    <link rel="stylesheet" href="../resource/css/app/add-people.css"/>
    <link rel="stylesheet" href="../resource/css/app/worker-info.css"/>
    <link rel="stylesheet" href="../resource/css/app/member-consumption.css"/>
    <script src="../resource/js/jquery-3.1.1.min.js"></script>
    <script src="../resource/js/storeManage/echarts.min.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <script>
        (function(win,doc){
            function change(){
                doc.documentElement.style.fontSize=doc.documentElement.clientWidth*20/375+'px';
            }
            change();
            win.addEventListener('resize',change,false);
        })(window,document);

    </script>
    <style type="text/css">
        #count_day, #money_day, #store_day{
          margin:0 auto;
          width:80%;
          font-size:.75rem;
          text-align:center;
          height:1.5rem;	
        }
        #count_day ul, #money_day ul, #store_day ul {
          list-style: none;
        }
        #count_day ul li, #money_day ul li, #store_day ul li{
           float:left;
           width:30%;
           padding:5px 0;
           margin:0;
           border:1px solid #CCC;
        }
        .active{
           background: #199ed8;
           color:#FFF;
        }
        #store_day{
          width:60% !important;
        }
        #store_day ul li{
           width:48%;
        }
        
        #money_total_show, #count_total_show, #store_total_show{
            margin:.5rem 0;
		    font-size: .8rem;
		    text-align: center;
        }
        .select_color{
          color: #199ed8;
        }
    </style>
</head>
<body>
<section>
    <div class="member-add">
        <h3 class="member-tit">今日交易金额（元）</h3>
        <p class="member-tail">${orderSta.am }</p>
        <div class="clearfix member-summary">
            <div class="fl member-oldbox">
                <p class="old-member">交易笔数（笔）</p>
                <p class="old-num">${orderSta.count }</p>
            </div>
            <div class="fr member-actbox">
                <p class="active-member">笔单价（笔）</p>
                <p class="active-num">${orderSta.average }</p>
            </div>
        </div>
    </div>

    <div class="foot">
        <!--交易金额分析-->
        <h2>
            <span class="member-sale"></span>
            交易金额分析
        </h2>
        <div id="money_day">
           <ul>
              <li class="active" data-val="3"><span>近3天趋势</span></li>
              <li data-val="7"><span>近7天趋势</span></li>
              <li data-val="30"><span>近30天趋势</span></li>
           </ul>
        </div>
        <div id="money" style="width: 95%;height: 16rem;"></div>
        <div id="money_total_show">
          <span id="money_title">近三天</span>交易总额:
          <span class="select_color" id="money_sum">0</span>元,平均金额:<span class="select_color" id="money_average">0</span>元
        </div>
         <!--交易笔数分析-->
        <h2>
            <span class="member-sale"></span>
            交易笔数分析
        </h2>
        <div id="count_day">
           <ul>
              <li class="active" data-val="3"><span>近3天趋势</span></li>
              <li data-val="7"><span>近7天趋势</span></li>
              <li data-val="30"><span>近30天趋势</span></li>
           </ul>
        </div>
        <div id="count" style="width: 95%;height: 16rem;"></div>
        <div id="count_total_show">
          <span id="count_title">近三天</span>总交易量:
          <span class="select_color" id="count_sum">0</span>元,平均交易量:<span class="select_color" id="count_average">0</span>笔
        </div>
        <!--多商户交易分析-->
        <div id="more_store">
	        <h2>
	            <span class="member-sale"></span>
	                        多商户交易分析
	        </h2>
	        <div id="store_day">
	           <ul>
	              <li class="active" data-val="1"><span>昨日统计</span></li>
	              <li data-val="2"><span>近3天统计</span></li>
	           </ul>
	        </div>
	        <div id="store" style="width: 95%;height: 16rem;"></div>
	        <div id="store_total_show">
	          <span id="store_title">昨日</span>门店交易总额:
	          <span class="select_color" id="store_sum">0</span>元,平均金额:<span class="select_color" id="store_average">0</span>元
	        </div>
        </div>
    </div>
</section>
<script>
    var store;
    if(${childStoreCount} == 0){
    	$("#more_store").remove();
    }else{
    	store = echarts.init(document.getElementById('store'));//
    	var store_option = {
        		title : {
                    x:'center'
                },
                legend: {
                	selectedMode:false,
                	orient: 'horizontal',
                    left: '15%',
                    bottom : "0",
                    data: []
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)",
                },
    	        series : [
    	            {
    	                name: '消费比例',
    	                type: 'pie',
    	                radius : '55%',
    	                center: ['50%', '45%'],
    	                data:[],
    	                itemStyle: {
    	                    emphasis: {
    	                        shadowBlur: 10,
    	                        shadowOffsetX: 0,
    	                        shadowColor: 'rgba(0, 0, 0, 0.5)'
    	                    }
    	                }
    	            }
    	
    	        ]
        };
    	store.setOption(store_option);
    	loadStoreByDay(1);
        function loadStoreByDay(day){
        	store.showLoading();
        	$.post("./getStoreStaByDay",{
        		day:day
        	}).done(function(result){
        		console.log(result);
        		store.hideLoading();
        		if(result.code == 0){
        			var _title = "昨日";
        			if(3== day){
        				_title = "近3天";
        			}
        			if(result.data == null){
        				$("#store_sum").text(0);
            			$("#store_average").text(0);
            			return;
        			}
        			var _obj = result.data.list;
        			$("#store_title").text(_title);
        			$("#store_sum").text(result.data.sum);
        			$("#store_average").text(result.data.average);
        			var _data = [],_sd = [];
        			for(var i = 0,len = _obj.length; i < len; i++){
        				_data.push({
        					value:_obj[i].sum,
        					name:_obj[i].name
        				});
        				_sd.push(_obj[i].name);
        			}
        			store.setOption({
        				legend: {
        		            data: _sd
        		        },
        				series : [{data:_data}]
        			});
        		}else{
        			jalert.show('出错了额');
        		}
        	});
        }
    	
    }
    var money = echarts.init(document.getElementById('money'));//
    var count = echarts.init(document.getElementById('count'));//
    var money_option={
        tooltip: {
            trigger: 'axis',
            formatter: '{a} <br/>{b} : {c}'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            name: '日期',
            type: 'category',
            boundaryGap: false,
            splitLine: {show: false},
            data: []
        },
        yAxis: {
            name:'金额',
            type: 'value'
        },
        series: [
            {
                name:'消费金额',
                type:'line',
                data:[],
            }
        ]
    };
    
    var count_option={
            tooltip: {
                trigger: 'axis'
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },

            xAxis: {
            	name: '日期',
                type: 'category',
                boundaryGap: false,
                splitLine: {show: false},
                data: []
            },
            yAxis: {
                name:'笔数',
                type: 'value'
            },
            series: [
                {
                    name:'交易笔数',
                    type:'line',
                    data:[],
                }
            ]
        };

    money.setOption(money_option);
    count.setOption(count_option);
    
    loadMoneyByDay(3);
    function loadMoneyByDay(day){
    	money.showLoading();
    	$.post("./getMoneyStaByDay",{
    		day:day
    	}).done(function(result){
    		money.hideLoading();
    		if(result.code == 0){
    			var _title = "近3天";
    			if(7 == day){
    				_title = "近7天";
    			}else if(30 == day){
    				_title = "近30天";
    			}
    			$("#money_title").text(_title);
    			if(result.data == null){
    				$("#money_sum").text(0);
        			$("#money_average").text(0);
        			return;
    			}
    			$("#money_sum").text(result.data.sum);
    			$("#money_average").text(result.data.average);
    			var _xdata = [];
    			var _sdata = [];
    			var _obj = result.data.list;
    			for(var i=0,len=_obj.length;i<len;i++){
    				_xdata.push(_obj[i].time);
    				_sdata.push(_obj[i].actualSumAmount);
    			}
    			money.setOption({
    				xAxis: {
    					splitNumber:_xdata.length,
    		            data: _xdata
    		        },
    		        series: [
    		            {
    		                data:_sdata
    		            }
    		        ]
    			});
    		}else{
    			jalert.show('出错了额');
    		}
    	});
    }
    loadCountByDay(3);
    function loadCountByDay(day){
    	count.showLoading();
    	$.post("./getCountStaByDay",{
    		day:day
    	}).done(function(result){
   			count.hideLoading();
    		if(result.code == 0){
    			var _title = "近3天";
    			if(7 == day){
    				_title = "近7天";
    			}else if(30 == day){
    				_title = "近30天";
    			}
    			$("#count_title").text(_title);
    			if(result.data == null){
    				$("#count_sum").text(0);
        			$("#count_average").text(0);
        			return;
    			}
    			$("#count_sum").text(result.data.sum_count);
    			$("#count_average").text(result.data.average_count);
    			var _xdata = [], _sdata = [];
    			var _obj = result.data.list;
    			for(var i =0,len =_obj.length;i <len;i ++){
    				_xdata.push(_obj[i].time);
    				_sdata.push(_obj[i].orderCount);
    			}
    			count.setOption({
    				xAxis: {
    					splitNumber: _xdata.length,
    		            data: _xdata
    		        },
    		        series: [
    		            {
    		                data:_sdata
    		            }
    		        ]
    			});
    		}else{
    			jalert.show('出错了额');
    		}
    	});
    }
    
    $("#money_day ul li").on("click",function(){
    	$("#money_day ul li").removeClass("active");
    	$(this).addClass("active");
    	var _val = $(this).attr("data-val");
    	loadMoneyByDay(_val);
    });
    $("#count_day ul li").on("click",function(){
    	$("#count_day ul li").removeClass("active");
    	$(this).addClass("active");
    	var _val = $(this).attr("data-val");
    	loadCountByDay(_val);
    });
    $("#store_day ul li").on("click",function(){
    	 $("#store_day ul li").removeClass("active");
    	 $(this).addClass("active");
    	 var _val = $(this).attr("data-val");
    	 if(1 == _val){
    		 loadStoreByDay(1);
    	 }else if(2 == _val){
    		 loadStoreByDay(3);
    	 }
    });
</script>
</body>
</html>