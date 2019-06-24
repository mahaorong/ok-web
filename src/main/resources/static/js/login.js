$(document).ready(function(){
	$("#bdl").click(function(){
		if (result_x != x && result_y != y) {
			alert("请拖动验证！");
		}else {

			var n1 = "[data-error='lusername']";
			var n2 = "[data-error='lpassword']";
			$(n1).empty();
			$(n2).empty();
			var form = $(this).parents("form");
			var data = form.serialize();
			var url = form.attr("action");
			$.post(url, data, function (json) {
				result = 1;
				$.each(json, function (code, message) {
					console.log(json);
					if (code != "tourl") {
						result = 2;
						var name = "[data-error='" + code + "']";
						if ($(name).length > 0) {
							$(name).html("*" + message).css({"color": "red", "fontSize": "14px"});
						}
					}
				});
				console.log(json.tourl);
				if (result == 1 && json.tourl != "") {
					window.location.href = json.tourl;
				}
			}, "json");
		}
	});
});

//判断当前窗口是否有顶级窗口，如果有就让当前的窗口的地址栏发生变化，
function loadTopWindow(){
    if (window.top!=null && window.top.document.URL!=document.URL){
        window.top.location= document.URL; //这样就可以让登陆窗口显示在整个窗口了
    }
}