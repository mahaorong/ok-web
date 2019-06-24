$(function () {
    var data = null;
    var key1 = "new";
    var url = null;
    // alert("进入");
    checkAndSubmit = function (id,key,templateId,type) {
        if (key != "new"){
            // alert("11111")
           data  = {
                "jsonId":id,
                "templateId":templateId,
                "key":key
            };
        }else {
            // alert("22222")
            data = {
                "jsonId":id,
                "templateId":templateId,
                "key":key1
            };
        }

        if($("#"+id+"").prop('checked')==true){
            // alert("点击");
            // alert(data.key);
            if (type != "tag"){
                url = "/template/saveTemplateData";
            }else {
                url = "/template/saveTag";
            }

            $.post(url,data,function (result) {
                key1=result;
                alert("全局"+key1);
            });
        }else {
            url = "/template/deleteBlogData";
            alert("未点击");
            $.post(url,data,function (result) {
                alert("删除成功");
            });
        }
    }

});