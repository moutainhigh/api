<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>会员消费</title>
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
        })(window,document)

    </script>
    <style type="text/css">
       #member{
          margin-top:1rem;
          text-align:center;
       }
        #member ul{
          list-style: none;
        }
        #member ul li{
           float:left;
           width:33.3%;
           padding:5px 0;
           margin:0;
           border:0;
           color:#FFF;
        }
        #member ul li:first-child{
          background: #008000;
        }
        #member ul li:nth-child(2){
          background: #ff0000;
        }
        #member ul li:last-child{
          background: #52a4db;
        }
        #day{
          margin:0 auto;
          width:80%;
          font-size:.75rem;
          text-align:center;
          height:1.5rem;	
        }
        #day ul {
          list-style: none;
        }
        #day ul li{
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
    </style>
</head>
<body>
<section>
    <div class="member-add">
        <h3 class="member-tit">今日新增会员人数（人）</h3>
        <p class="member-tail">${memberMap['addCount'] }</p>
        <div class="clearfix member-summary">
            <div class="fl member-oldbox">
                <p class="old-member">老会员（人）</p>
                <p class="old-num">${memberMap['oldCount'] }</p>
            </div>
            <div class="fr member-actbox">
                <p class="active-member">活跃会员（人）</p>
                <p class="active-num">${memberMap['activeCount'] }</p>
            </div>
        </div>
    </div>

    <div class="foot">
        <h2>
            <span class="member-sale"></span>
                    会员分析
        </h2>
        <div id="day">
            <ul>
              <li class="active" data-val="3"><span>近三天趋势</span></li>
              <li data-val="7"><span>近七天趋势</span></li>
              <li data-val="30"><span>近三十天趋势</span></li>
            </ul>
        </div>
        <div id="main" style="width: 100%;height: 16rem;"></div>
    </div>
</section>
<script>
   $(function(){
	    $("#day ul li").on("click",function(){
	    	$("#day ul li").removeClass("active");
	    	$(this).addClass("active");
	    	var _val = $(this).attr('data-val');
	    	loadDay(_val);
	    });
   });

    var myChart = echarts.init(document.getElementById('main'));
    myChart.setOption({
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
                name: '会员趋势',
                type: 'pie',
                radius : '55%',
                center: ['50%', '45%'],
                data:[
                ],
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }

        ]
    });
    
    
    loadDay(3);
    
    function loadDay(day){
    	myChart.showLoading();
	    $.get("./getMemberStaByDay",{
	    	day:day
	    }).done(function(result){
	    	console.log(result);
	    	if(result.code == 0){
		        myChart.hideLoading();
	    		myChart.setOption({
	    			    legend: {
	    		            data: ['老会员','活跃会员','新增会员']
	    		        },
		    	        series : [{ 
			    	                data:[
			    	                    {value:result.data.oldCount, name:'老会员'},
			    	                    {value:result.data.activeCount, name:'活跃会员'},
			    	                    {value:result.data.addCount, name:'新增会员'}
			    	                ]
			    	            }]
				    	    });
	        }else{
	        	jalert.show('出错了额');
	        }
	    });
	    
    }
</script>
</body>
</html>