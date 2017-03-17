<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>员工详情</title>
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
        .content{
            background: #FFF;
        }
        .row{
            border-bottom:1px solid #EEE;
        }
        .row_detail{
            margin: 0 1rem;
            padding: .6rem 0;
            font-size: .9rem;
        }
        .operate{
            background: #fc324a;
            padding: .8rem 1rem;
        }
        .operate span:first-child{
            color: #FFF;
            float: left;
            display: inline-block;
            font-size:.9rem;
        }
        .operate span:last-child{
            padding:.05rem 1rem;
            background: #FFF;
            color: #fc324a;
            float: right;
            display: inline-block;
            font-size:.75rem;
            font-weight: 600;
            border-radius: 2px;
        }
        .row_detail span:first-child{
            float: left;
            color:#333;
        }
        .row_detail span:last-child{
            float: right;
            font-size: .75rem;
        }
        .footer .item{
            width: 90%;
            margin: 0 auto;
            background: #fc324a;
            color: #FFF;
            border-radius: 5px;
        }
        .item span{
            padding: .5rem 0;
            display: block;
            margin: .5rem 0;
        }
        .item:first-child span{
            margin:1rem 0 .5rem;
        }
        .item:last-child span{
            margin:.5rem 0 1rem;
         }
        #cancel,#modifyPw,#unbind{
            background: #CCC;
        }
        footer{
            box-shadow: 0 0 10px #999;
            -webkit-transition:all .5s ease-in-out;
            -webkit-transform:translateY(300px);
            transition:all .5s ease-in-out;
            transform:translateY(300px);
        }
        .unbind_tips{
            position: fixed;
            top:30%;
            left:5%;
            width:90%;
            border-radius: 5px;
            box-shadow: 0 0 20px #999;
            text-align: center;
            color:#FFF;
            font-size: .9rem;
            display: none;
        }
        .tip_content{
            background: #666;
            border-radius: 5px;
        }
        .tip_title{
            padding: .6rem 0;
            border-bottom: 1px solid rgba(255,255,255,.2);
            margin: 0 1rem;
        }
        .tip_msg{
            padding:2rem 0;
            font-size: .8rem;
        }
        .unbing-btn{
            padding: 1rem 0;
            font-size: .8rem;
            color: #FFF;
            background: #FFF;
        }
        #unbind_cancle{
            padding:5px 1.25rem;
            background: #CCC;
            margin-right:1rem;
            border-radius: 2px;
        }
        #unbind_confirm{
            padding: 5px 1.25rem;
            background: #fc324a;
            margin-left:1rem;
            border-radius: 2px;
        }
    </style>
</head>
<body>
   <input type="hidden" id="auth" value="${auth}">
   <div class="container">
        <section class="content">
              <div class="row">
                     <div class="operate clearfix">
                           <span id="status"></span>
                           <span id="manage">管理</span>
                     </div>
              </div>
              <div class="row">
                    <div class="row_detail clearfix">
                        <span>员工姓名</span>
                        <span>${StoreAccount.name}</span>
                    </div>
              </div>
            <div class="row">
                <div class="row_detail clearfix">
                    <span>员工手机</span>
                    <span>${StoreAccount.mobile}</span>
                </div>
            </div>
            <div class="row">
                <div class="row_detail clearfix">
                    <span>用户名</span>
                    <span>${StoreAccount.account}</span>
                </div>
            </div>
<!--             <div class="row"> -->
<!--                 <div class="row_detail clearfix"> -->
<!--                     <span>退款权限</span> -->
<!--                     <span>不可退款</span> -->
<!--                 </div> -->
<!--             </div> -->
            <div class="row">
                <div class="row_detail clearfix">
                    <span>员工角色</span>
                    <span>收银员</span>
                </div>
            </div>
        </section>
   </div>
   <footer>
       <div class="footer">
           <div class="btn_container">
               <div class="item" id="modify">
                     <span>修改</span>
               </div>
               <div class="item" id="modifyPw">
                   <span>修改密码</span>
               </div>
               <div class="item" id="unbind">
                   <span>解绑</span>
               </div>
               <div class="item" id="cancel">
                   <span>取消</span>
               </div>

           </div>
       </div>
   </footer>

   <div class="unbind_tips">
        <div class="tip_content">
             <div class="tip_title">
                     <span>解绑</span>
             </div>
            <div class="tip_msg">
                  <span>确认解绑?解绑后该员工将无法登录!</span>
            </div>
        </div>
        <div class="unbing-btn">
            <div class="un_btn">
                   <span id="unbind_cancle">取消</span>
                   <span id="unbind_confirm">确定</span>
            </div>
        </div>
   </div>
</body>
</html>
<script>
    $(function(){
    	//状态
    	  if('${StoreAccount.openId}' == ''){
    		  $("#status").text("未激活");
    		  $("#modifyPw").hide();
    		  $("#unbind").hide();
    	  }else{
    		  $("#status").text("已激活");
    	  }
    	//auth
    	  var auth = $("#auth").val(),id = ${StoreAccount.id};
          $("#manage").on("click",function(){
//                console.log($("footer").css("transform"));

               if($("footer").css("transform").indexOf("300") != -1){
                   $("footer").css({
                       "-webkit-transform":"translateY(0)",
                       "transform":"translateY(0)"
                   });
               }else{
                   $("footer").css({
                      "-webkit-transform":"translateY(300px)",
                      "transform":"translateY(300px)"
                  });
               }
          });
          
          //修改
          $("#modify").on("click",function(){
        	 location.href="./toModifyStaffPage?auth="+auth+"&id="+id; 
          });
          //修改密码
          $("#modifyPw").on("click",function(){
        	  location.href="./toModifyPassWordPage?auth="+auth+"&id="+id;
          });
          //取消
          $("#cancel").on("click",function(){
              $("footer").css({
                  "-webkit-transform":"translateY(300px)",
                  "transform":"translateY(300px)"
              });
          });
          //解绑提示
          $("#unbind").on("click",function(){
               $(".unbind_tips").fadeIn("slow");
              $("footer").css({
                  "-webkit-transform":"translateY(300px)",
                  "transform":"translateY(300px)"
              });
          });
          //取消解绑
          $("#unbind_cancle").on("click",function(){
              $(".unbind_tips").fadeOut("slow");
          });
          //确定解绑
          $("#unbind_confirm").on("click",function(){
              $.post("./unbindStoreAccount",{
                 auth:auth,
                 id:id
              },function(result){
                  if(result.code == 0){
                     jalert.show(result.msg);
                     $(".unbind_tips").fadeOut("slow");
                     location.href="./toManageStaffPage?auth="+auth;
                  }else{
                     jalert.show(result.msg);
                  }
                 
              });
              
          });
    });

</script>