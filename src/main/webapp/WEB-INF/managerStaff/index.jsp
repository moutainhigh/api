<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>员工管理</title>
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no,minimal-ui">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="no">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <link href="../resource/css/manager/common.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="../resource/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../resource/js/wechatCommon.js"></script>
    <script type="text/javascript" src="../resource/js/jquery.alert.js"></script>
    <style>
       .staff_item{
           background: #FFF;
       }
       .staffContent{
           margin-bottom:4rem;
       }
       .staff_item:not(:first-child){
            margin-top:.5rem;
       }
       .staff_item > .staff_detail{
           padding:.6rem 0;
           margin:0 1rem;
           font-size:.85rem;
           position: relative;
       }
       .detail p:first-child{
           padding-bottom:.25rem;
       }
       .detail p:first-child span:last-child{
           background: #fc324a;
           color: #FFF;
           font-size: .8rem;
           font-weight: 100;
           margin-left: .5rem;
           border-radius: 3px;
       }
       .detail p:last-child{
           font-size:.7rem;
       }
       .staff_detail .operate{
           position: absolute;
           right:0;
           top:35%;
           color: #666;
       }
        .unactive{
            background: #dcdcdc;
            color: #999;
        }
        .unactive .detail p:first-child span:last-child{
            background: #999;
        }
        .unactive .operate{
            color: #fc324a;
        }
       .footer {
           background-color: #fc324a;
           text-align: center;
           color:#FFF;
       }
       .add{
           padding:.6rem 0;
           background: url(../../image/other/add.png) no-repeat 35% center;
           background-size: 18px;
       }
       .footer{
           border-color:#fc324a;
           -webkit-box-shadow:0 0 5px #fc324a;
       }
       .shoper{
          padding: 2px 10px;
		  background: #fc324a;
		  color: #FFF;
		  border-radius: 2px;
       }
       .shoper_row{
          margin-left:3.2rem !important;
       }
       .head_img {
            position: absolute;
            top: .3rem;
            left:0;
       }
       .head_img img{
         width: 2.75rem;
         height: 2.75rem;
         border-radius: 50%;
       }
    </style>
</head>
<body>
   <input type="hidden" value="${auth}" id="auth">
   <div class="container">
       <section class="staffContent">
           <div class="staffList">
<!--                <div class="staff_item"> -->
<!--                    <div class="staff_detail"> -->
<!--                        <div class="detail"> -->
<!--                            <p> -->
<!--                                <span>王大龙</span> -->
<!--                                <span>收银员</span> -->
<!--                            </p> -->
<!--                            <p>123456789</p> -->
<!--                        </div> -->
<!--                        <div class="operate"> -->
<!--                            <span class="unbind">解除</span> -->
<!--                        </div> -->
<!--                    </div> -->
<!--                </div> -->

<!--                <div class="staff_item"> -->
<!--                    <div class="staff_detail"> -->
<!--                        <div class="detail"> -->
<!--                            <p> -->
<!--                                <span>王大龙</span> -->
<!--                                <span>收银员</span> -->
<!--                            </p> -->
<!--                            <p>123456789</p> -->
<!--                        </div> -->
<!--                        <div class="operate"> -->
<!--                            <span class="unbind">解除</span> -->
<!--                        </div> -->
<!--                    </div> -->
<!--                </div> -->

<!--                <div class="staff_item unactive"> -->
<!--                    <div class="staff_detail"> -->
<!--                        <div class="detail"> -->
<!--                            <p> -->
<!--                                <span>王大龙</span> -->
<!--                                <span>收银员</span> -->
<!--                            </p> -->
<!--                            <p>123456789</p> -->
<!--                        </div> -->
<!--                        <div class="operate"> -->
<!--                            <span class="unbind">未激活</span> -->
<!--                        </div> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
<!--            <div class="staff_item"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">解除</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->

<!--            <div class="staff_item unactive"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">未激活</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
<!--            <div class="staff_item"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">解除</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->

<!--            <div class="staff_item unactive"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">未激活</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
<!--            <div class="staff_item"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">解除</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->

<!--            <div class="staff_item unactive"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">未激活</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
<!--            <div class="staff_item"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">解除</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->

<!--            <div class="staff_item unactive"> -->
<!--                <div class="staff_detail"> -->
<!--                    <div class="detail"> -->
<!--                        <p> -->
<!--                            <span>王大龙</span> -->
<!--                            <span>收银员</span> -->
<!--                        </p> -->
<!--                        <p>123456789</p> -->
<!--                    </div> -->
<!--                    <div class="operate"> -->
<!--                        <span class="unbind">未激活</span> -->
<!--                    </div> -->
<!--                </div> -->
<!--            </div> -->
       </section>

   </div>
   <footer>
       <section class="footer" id="add">
           <div class="add">添加员工</div>
       </section>
   </footer>
</body>
</html>
<script>
  $(function(){
	  var auth = $("#auth").val();
	  load(auth);
	  
	  $("#add").click(function(){
		 location.href = "./toAddStaffPage?auth="+auth; 
	  });
	  
	  
	  
  });
  
  function load(auth){
	  var staffList = $(".staffList");
	  $.post("./getStoreAccountList",{
		  auth:auth
	  },function(result){
		  console.log(result);
		  if(result.code == 0){
			  //data-type = 1店长  data-type = 2收银员
			  var manager = result.data.manager;
			  var staff = result.data.staff;
			  for(var i =0,len=manager.length;i<len;i++){
				  var sf = '<div class="staff_item" data-type="1">'
							  +'<div class="staff_detail">'
							  +'<span class="head_img"><img src="'+manager[i].sab.headImg+'" onerror="this.src=\'../resource/img/cat.png\'"></span>'
							  +'          <div class="detail shoper_row">'
							  +'              <p>'
							  +'                  <span>'+manager[i].sab.name+'</span>'
							  +'                  <span></span>'
							  +'              </p>'
							  +'              <p>'+manager[i].sab.account+'</p>'
							  +'          </div>'
							  +'          <div class="operate">'
							  +'              <span class="shoper">'+manager[i].roleName+'</span>'
							  +'          </div>'
							  +'      </div>'
							  +'  </div>';
				  staffList.append(sf);
			  }
			  for(var i =0,len=staff.length;i<len;i++){
				      var sf = "";
					  if(staff[i].sab.openId != '' && staff[i].sab.openId != null){
						   sf = '<div class="staff_item" data-type="2" data-id="'+staff[i].sab.id+'">'
							  +'<div class="staff_detail">'
							  +'          <div class="detail">'
							  +'              <p>'
							  +'                  <span>'+staff[i].sab.name+'</span>'
							  +'                  <span>'+staff[i].roleName+'</span>'
							  +'              </p>'
							  +'              <p>'+staff[i].sab.account+'</p>'
							  +'          </div>'
							  +'          <div class="operate">'
							  +'              <span class="unbind">解除</span>'
							  +'          </div>'
							  +'      </div>'
							  +'  </div>';
					  }else{
						  if(staff[i].sab.valid == 1){
							  sf =  '<div class="staff_item unactive" data-type="2" data-id="'+staff[i].sab.id+'">';
						  }else{
							  sf =  '<div class="staff_item unactive" data-type="3" data-id="'+staff[i].sab.id+'">';
						  }
						 sf += '<div class="staff_detail">'
							 +'  <div class="detail">'
							 +'     <p>'
							 +'         <span>'+staff[i].sab.name+'</span>'
							 +'         <span>'+staff[i].roleName+'</span>'
							 +'      </p>'
							 +'     <p>'+staff[i].sab.account+'</p>'
							 +' </div>'
							 +'  <div class="operate">';
							 if(staff[i].sab.valid == 1){
								 sf += '      <span class="unbind">未激活</span>';
							 }else{
								 sf += '      <span class="unbind">离职</span>';
							 }
							 sf += '  </div>'
							 +'</div>'
							 +'</div>';
					  }
		         staffList.append(sf);
		         
		         $(".staff_item").on("click",function(){
// 			   		  console.log($(this).text());
			   		  if($(this).attr("data-type") == 2){
			   			  location.href = "./toStaffDetailPage?auth="+auth+"&id="+$(this).attr("data-id");
			   		  }
			   	  });
			  };
		  }else{
			  jalert.show('错误了');
		  };
	  });
  };
</script>