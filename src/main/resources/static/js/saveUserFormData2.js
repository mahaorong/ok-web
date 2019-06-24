function TimeDifference(time1 , time2) {
    if(time1>time2)
    {
        console.log("开始时间不能大于结束时间！");
        return 0;
    }
    var begin1=time1.substr(0,10).split("-");
    var end1=time2.substr(0,10).split("-");
    var date1=new Date(begin1[1] + - + begin1[2] + - + begin1[0]);
    var date2=new Date(end1[1] + - + end1[2] + - + end1[0]);
    var m=parseInt(Math.abs(date2-date1)/1000/60);
    var min1=parseInt(time1.substr(11,2))*60+parseInt(time1.substr(14,2));
    var min2=parseInt(time2.substr(11,2))*60+parseInt(time2.substr(14,2));
    var n=min2-min1;
    var minutes=m+n;
    return minutes;
}
Date.prototype.format = function (format) {
    var args = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),  //quarter
        "S": this.getMilliseconds()
    };
    if (/(y+)/.test(format))
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var i in args) {
        var n = args[i];
        if (new RegExp("(" + i + ")").test(format))
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
    }
    return format;
};
$(function () {
    var time = 1800000;
    var item = sessionStorage.getItem("number");
    if (item == null || item == 0) {
        sessionStorage.setItem("number", 0);
    }
    var number = sessionStorage.getItem("number");
    var time2 = sessionStorage.getItem("time1")
    if (time2 != null) {
        var time3 = new Date().format("yyyy-MM-dd hh:mm");
        console.log("time2" + time2);
        var count = TimeDifference(time2, time3);
        console.log(count);
        if (count != 0) {
            if (count >= 30) {
                sessionStorage.setItem("number", 0);
                $("#updateFormItem").removeAttr("disabled", "disabled");
                createCode();
                document.getElementById("J_codetext").setAttribute("placeholder", "");
                document.getElementById("J_codetext").setAttribute("placeholder", "验证码");
            } else {
                $("#updateFormItem").attr("disabled", "disabled");
                document.getElementById("J_codetext").setAttribute("placeholder", "");
                document.getElementById("J_codetext").setAttribute("placeholder", "30分后重试");
            }
        }
    }
    $(".updateFormItem").click(function () {
        number = sessionStorage.getItem("number");
        console.log(number);
        let form = $(this).data("form");
        console.log("form 名字 = " + form);

        if (number >= 30) {
            var time2 = new Date().format("yyyy-MM-dd hh:mm");
            sessionStorage.setItem("time1", time2);
            //alert(time2);
            $(this).attr("disabled", "disabled");
            document.getElementById("J_codetext").setAttribute("placeholder", "");
            document.getElementById("J_codetext").setAttribute("placeholder", "30分后重试");
            setTimeout(function () {
                sessionStorage.setItem("number", 0);
                $("#updateFormItem").removeAttr("disabled");
                createCode();
                document.getElementById("J_codetext").setAttribute("placeholder", "");
                document.getElementById("J_codetext").setAttribute("placeholder", "验证码");
            }, time);
        } else {
           /* let formData = new FormData($("#" + form)[0]);
            for(let pair of formData.entries()) {
                console.log(pair[0]+ ', '+ pair[1]);
                formData.set(pair[0], $.trim(pair[1]));
            }*/

            $.ajax({
                url: "/form/saveForm",
                type: "POST",
                data: $("#" + form).serialize(),
                success: function (data) {
                    if (null != data && data.message != "success") {
                        $.each(data, function (field, message) {
                            console.log("fileId==" + field);
                            console.log("message==" + message);
                            if (field == "checkbox") {
                                $(".checkb").html(message);
                            } else if (field == "radio") {
                                $(".radio").html(message);
                            } else {
                                $("#" + field + "").html(message);
                            }
                            setTimeout(function () {
                                $(".error").html("");
                            }, 6000);
                        });
                    }
                    if (data.code == 0) {
                        sessionStorage.setItem("number", 0);
                        window.location.reload();
                    }
                    if (data.code == 1) {
                        number++;
                        sessionStorage.setItem("number", number);
                        createCode();
                        document.getElementById("J_codetext").value = "";
                        document.getElementById("inputCode").value = "";
                        document.getElementById("J_codetext").setAttribute("placeholder", data.message);
                    }
                }
            });
        }
    });
});

function trim(str){
    return str.replace(/^(\s|\u00A0)+/,'').replace(/(\s|\u00A0)+$/,'');
}
