var count = 0;
showForm = function (formId) {
    $.ajax({
        url:"/form/showForm",
        data:{formId:formId},
        type:"POST",
        success:function (data) {
            console.log("data=="+data)
            $.each(data,function (event,result) {
                count = event;
                var optionName = result.name;
                if (optionName == null){
                    optionName = "";
                }
                var optionResult = result.result.join(",");
                var text = "<tr id='"+event+"'><th scope='row'>"+optionName+"</th><td >"+optionResult+"</td></tr>";
                if (event == 0){
                    $("#showForm").after(text);
                } else {
                    var number = event - 1;
                    console.log(number);
                    $("#" + number + "").after(text);
                }
            })
        }
    })
}
$(function () {
    $("#exampleModal").on("hidden.bs.modal",function () {
        for (var z = 0; z <= count; z++) {
            $("#" + z + "").remove();
        }
    })
})
