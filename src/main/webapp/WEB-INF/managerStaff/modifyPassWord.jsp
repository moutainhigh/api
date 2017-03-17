<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>修改密码</title>
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
        .f{
            background: #FFF;
        }
        .f .row:last-child .input_row{
            border-bottom:none;
        }
        .f:not(:first-child){
            margin-top:.5rem;
        }
        .row .input_row{
            margin:0 1rem;
            padding:.75rem 0;
            display: flex;
            font-size: .85rem;
            border-bottom:1px solid #EEE;
        }
        .input_row > label{
            width:4.3rem;
            color: #666;
        }
        .input_row .input_text{
            flex:1;
            position: relative;
        }
        .input_text input[type=text]{
            width:100%;
            height: 100%;
            position: absolute;
            left: 0;
            top: 0;
            font-size:.7rem;
        }

        .input_text > label{
            position: absolute;
            left:0;
            top:.15rem;
            font-size: .7rem;
        }
        .input_text > label:last-child {
            left:50%;
        }
        .f1{
            margin-top: .9rem;
            text-align: center;
            background: #fc324a;
            color: #FFF;
        }
        .f1 .btn{
            padding:.6rem 0;
        }

    </style>
</head>
<body>
    <input type="hidden" name="auth" value="${auth}">
    <input type="hidden" name="id" value="${id}">
    <div class="container">
        <section class="f">
            <div class="row">
                <div class="input_row">
                    <label>修改密码</label>
                    <div class="input_text">
                        <input type="text" placeholder="输入旧密码" id="oldPw" autofocus>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="input_row">
                    <label>新密码</label>
                    <div class="input_text">
                        <input type="text" placeholder="输入新密码" id="newPw">
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="input_row">
                    <label>确认密码</label>
                    <div class="input_text">
                        <input type="text" placeholder="再次输入新密码" id="twoNewPw">
                    </div>
                </div>
            </div>
        </section>
    </div>

    <section class="f1" id="modify">
        <div class="btn">
            <span>确认修改</span>
        </div>
    </section>
</body>
</html>
<script>
    $(function(){
    	var auth = $("#auth").val(),id = $("#id").val();
        $("#modify").on("click",function(){
//             jalert.show("modify");
            var oldPw = $("#oldPw").val(),
                newPw = $("#newPw").val(),
                twoNewPw = $("#twoNewPw").val();
            if(oldPw == '' || newPw == '' || twoNewPw == ''){
            	jalert.show('请填写完整');
            	return false;
            }
            if(oldPw == newPw){
            	jalert.show('新密码不能与旧密码相同');
            	return false;
            }
            if(newPw != twoNewPw){
            	jalert.show('俩次输入的密码不同');
            	return false;
            }
            $.post("./updateStoreAccountPw",{
            	auth:auth,
            	id:id,
            	password:oldPw,
            	newPassWord:newPw
            },function(result){
            	console.log(result);
            	if(result.code == 2){
            		jalert.show(result.msg);
            	}else if(result.code == 0){
            		jalert.show('修改成功');
       			    location.href = "./toStaffDetailPage?auth="+auth+"&id="+id;
            	}else{
            		jalert.show("error");
            	}
            });
        });
    });

</script>