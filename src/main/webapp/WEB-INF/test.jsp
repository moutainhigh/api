<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>test</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="resource/js/jquery-3.1.1.min.js"></script>
	<style type="text/css">
	body{
	  background-color:#efefed;
	}
	         div{
	           margin:10px auto;
	           width:1080px;
	           border:1px solid #CCC;
	         }
	</style>

  </head>
  
  <body>
    
    
    <div>
       <h1>第一级</h1>
       <select autofocus="autofocus" id="first">
          <!-- <option value="">请选择</option> -->
       </select>
    </div>
    <div>
       <h1>第er级</h1>
       <select id="two">
           <!-- <option value="">请选择</option> -->
       </select>
    </div>
    <div>
       <h1>第san级</h1>
       <select id="thrid">
           <!--<option value="">请选择</option> -->
       </select>
    </div>
    <div>
       <h1>添加下属类型</h1>
       <input type="text" id="name">
       <input type="button" value="添加" id="add">
    </div>
  </body>
</html>

<script type="text/javascript">
   $(function(){
	   $.post("getListByParentId",{id:0},function(result){
		   console.log(result);
		   $("#first").empty();
		   if(result.code == 0){
			   var list = result.data;
			   for(var i =0;i<list.length;i++)
			   $("#first").append($("<option>").val(list[i].id).text(list[i].name+" "+list[i].id));
		   }
	   });
	   
	   $("#first").on("change",function(){
		   $.post("getListByParentId",{
			   id:$(this).val()
		   },function(result){
			   console.log(result);
			   $("#two").empty();
			   if(result.code == 0){
				   var list = result.data;
				   for(var i =0;i<list.length;i++)
				   $("#two").append($("<option>").val(list[i].id).text(list[i].name+" "+list[i].id));
			   }
		   });
	   });
	   
	   
	   $("#two").on("change",function(){
		   $.post("getListByParentId",{
			   id:$(this).val()
		   },function(result){
			   console.log(result);
			   $("#thrid").empty();
			   if(result.code == 0){
				   var list = result.data;
				   for(var i =0;i<list.length;i++)
				   $("#thrid").append($("<option>").val(list[i].id).text(list[i].name+" "+list[i].id));
			   }
		   });
	   });
	   
	   
	   $("#add").on("click",function(){
		   if("" == $("#two").val() || "" == $("#name").val()){
			   alert("不能有空值");
			   return false;
		   };
		   $.post("addBusinessType",{
			   parentId:$("#two").val(),
			   name:$("#name").val()
		   },function(result){
			   console.log(result);
			   if(result.code == 0){
				   if(result.data == 1){
					   alert("添加成功!");
				   }
			   }
		   });
	   });
   });
</script>
