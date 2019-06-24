
  /*  $.validator.setDefaults({
        submitHandler: function() { alert("提交事件!"); }
    });*/
  function changePassword () {
        $.ajax({
            url : "/changePassword",
            type : 'POST',
            data : $("#changeForm").serialize(),
            success: function (data) {
                console.log("成功");
                if(null != data){
                    $.each(data,function(field,message){
                        console.log(field);
                        console.log(message);
                        $("#"+field+"").html(message);
                        setTimeout(function(){
                            $("#"+field+"").html("");
                        },4000);
                    });
                }
                if (data.code == 0) {
                    // window.location.href = "/admin/index";
                    window.top.location="/login";
                }else if (data.code == 1){
                    $("#oldPassword").html(data.data);
                    setTimeout(function(){
                        $("#oldPassword").html("");
                    },4000);
                }else if (data.code == 2){
                    $("#confirmPassword").html(data.message);
                    setTimeout(function(){
                        $("#confirmPassword").html("");
                    },4000);
                }

            },
            error: function (data) {
                console.log("失败");
            },
            dataType:"json"
        });


       /* $.ajax({
            url:"/changePassword",
            data:{"oldPassword":oldPassword,"newPassword":newPassword},
            type:"POST",
            success:function (message) {
                console.log(message);
                if (message == "密码错误"){
                    $("#error").html("*"+message);
                }else if (message == "修改密码成功") {
                    window.location.href="/login";
                }else if (message == "原密码不能为空"){
                    $("#error").html("*"+message);
                }
            },
            error:function (message) {
                console.log("失败");
            }
        });*/

    }

   /* $("#changeForm").validate({
        rules:{
            newPassword:{
                required:true,
                minlength: 6
            },
            confirm_password:{
                required: true,
                minlength: 6,
                equalTo: "#newPassword"
            }
        },
        message:{
            newPassword:{
                required:"请输入新密码",
                minlength:"密码不能小于6位"
            },
            confirm_password:{
                required: "请输入密码",
                minlength: "密码不能小于6位",
                equalTo: "两次密码不一致"
            }
        }
    })*/

