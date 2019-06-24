var oldFileName = null;
findName = function(id){
    var oldName = $("#" + id + "").text();
    oldFileName = oldName;
}
saveName = function (id) {
    var fileName = $("#"+id+"").text();
    console.log(fileName);
    console.log(id);
    if (fileName != oldFileName){
        $.ajax({
            url:"/product/addName",
            data:{"id":id,"name":fileName},
            type:"POST",
            success:function (data) {
                iziToast.success({
                    position: 'topRight',
                    title: '修改成功',
                    message: 'Successfully!',
                });
            }
        })
    }
}
//根据ID查询信息
function deleteById(id,wid)  {
    $.ajax({
        type: "POST",
        data: {id:id},
        url: "/product/deleteProduct",
        success: function () {
            window.location.href = "/admin/findProductDetail?wid="+wid;
        },
        error: function (data) {
            alert("删除失败");
        }
    });
}

var findJson = null;
checkJson = function(productDataId){
    var checkName = $("#" + productDataId + "").text();
    findJson = checkName;
}
saveJson = function (proId,proDataId) {
    var jsonText = $("#" + proDataId + "").text();
    console.log("findJson==" + findJson);
    if (findJson != jsonText) {
        $.ajax({
            url: "/product/addName",
            data: {"id": proId, "proDataId": proDataId, "name": jsonText},
            type: "POST",
            success: function (data) {
                if (data == "success") {
                    iziToast.success({
                        position: 'topRight',
                        title: '修改成功',
                        message: 'Successfully!',
                    });
                }
            }
        });
    } else {
        console.log("未改变")
    }
}