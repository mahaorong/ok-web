 var txt = 0;
var id = null;
var conut = 0;
function appendContent() {
    var txt1="<tr id='"+txt+"'><th scope='row'><input name='option' id='option"+txt+"' class='form-control' type='text'></th><td class='table_fun'><button id='"+txt+"' onclick='delect(this)' class='btn btn-danger'>删除</button></td></tr>"
    $("#NewContent").after(txt1)
    txt++;
}
function delect(obj) {
    $("#"+obj.id+"").remove();
}
findOption =function (formItemId) {
    $.ajax({
        url: "/formItem/findOption",
        type: "POST",
        data: {"formItemId": formItemId},
        success: function (data) {
            id = data.id;
            conut = data.option.length;
            /*循环添加下拉列表的内容 */
            var number = null;
            $("#id").val(id)
            if (null != data.option.length) {
                for (var i = 0; i < data.option.length; i++) {
                    var txt1 = "<tr id='" + data.id + i + "'><th scope='row'><input name='option' value='" + data.option[i] + "' class='form-control' type='text'></th><td class='table_fun'><button id='" + data.id + i + "' onclick='delect(this)' class='btn btn-danger'>删除</button></td></tr>";
                    // 以 HTML 创建新元素
                    if (i == 0) {
                        console.log("【i为0】=" + data.id)
                        number = data.id;
                        $("#NewContent").after(txt1) // 追加新元素
                    } else {
                        console.log('【number+i】=' + number + (i - 1));
                        $("#" + number + (i - 1) + "").after(txt1)
                        number = data.id;
                        txt++
                    }
                }
            }
        }
    });
}

$(function () {
    $("#exampleModal").on('hidden.bs.modal',function () {
        for (var z = 0; z < conut; z++) {
            $("#" + id + z + "").remove();
        }
        for (var z = 0; z < txt; z++) {
            $("#" + z + "").remove();
        }
    })
})


var isCheckNaem = null;
 checkName = function (formItemId, flag) {
     var checkNames = $("#formItem" + flag + formItemId + "").val();
     isCheckNaem = checkNames;
 };
 saveName = function (formItemId, flag) {
     var formName = $("#formItem" + flag + formItemId + "").val();
     console.log("变化前==" + isCheckNaem);
     console.log("变化后==" + formName);
     var data;
     if (flag == "Name") {
         data = {id: formItemId, name: formName, "operating": "saveName", flag: flag}
     }else {
         data = {id: formItemId, remarks: formName, "operating": "saveName", flag: flag}
     }
     if (isCheckNaem != formName) {
         $.ajax({
             url: "/formItem/saveFormItem",
             data: data,
             type: "POST",
             success: function (data) {
                 iziToast.success({
                     position: 'topRight',
                     title: '修改成功',
                     message: 'Successfully!'
                 });
             }
         });
     } else {
         console.log("文字内容未发生改变");
     }
 };

saveType = function (formItemId) {
    $("#options"+formItemId+"").change(function () {
        var option = $("#options"+formItemId+" option:selected").val();
        $.ajax({
            url:"/formItem/saveFormItem",
            data:{"type":option,"id":formItemId,"operating":"saveType"},
            type:"POST",
            success:function (data) {
                window.location.reload();
            }
        });
    });
}