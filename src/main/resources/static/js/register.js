$(document).ready(function(){
	$("#bzc").click(function(){
		var n2="[data-error='password']";
		var n3="[data-error='qpassword']";
		var n4="[data-error='username']";
		$(n2).empty();
		$(n3).empty();
		$(n4).empty();
		var form=$(this).parents("form");
		var data=form.serialize();
		var url=form.attr("action");
		$.post(url,data,function(json){
			result=1;
			$.each(json,function(code,message){
				console.log(json);
				if(code!="tourl"){
					result=2;
					var name="[data-error='"+code+"']";
					if($(name).length>0){
						$(name).html("*"+message).css({"color":"red","fontSize":"14px"});
					}
				}
			});
			console.log(json.tourl);
			if(result==1&&json.tourl!=""){
				window.location.href=json.tourl;
			}
		},"json");
	});


    $("#bxx").click(function () {
        var username = $("#username").val();
        var password = $("#password").val();
        var qpassword = $("#qpassword").val();
        $.ajax({
			url:"/registerAuto",
			data:{username:username,password:password,qpassword:qpassword},
			type:"get",
			success:function (data) {
				if (data != "success"){
					$.each(data,function(code,message){
						var name="[data-"+code+"]";
						if($(name).length>0){
							$(name).html("*"+message).css({"color":"red","fontSize":"14px"});
						}
					});
				}
				if (data == "success"){
                    window.location.reload();
				}
            }
		})

    });
});