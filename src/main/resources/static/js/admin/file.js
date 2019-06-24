var fileArr = [];
var cateArr = [];
$(document).on("click","input[name='fileIds']",function () {
    let id = $(this).val();
    let new_arr = [];
    if ($(this).prop("checked")){
        fileArr.push(id);
    } else {
        for (var i in fileArr){
            if (fileArr[i] != id) {
                new_arr.push(fileArr[i]);
            }
        }
        fileArr = new_arr;
    }
    console.log(fileArr)
})

$("[data-delete]").on("click",function () {
    alert("删除")
})

$(document).on("click","input[name='cateids']",function () {
    let id = $(this).val();
    let new_arr = [];
    if ($(this).prop("checked")){
        cateArr.push(id);
    } else {
        for (var i in cateArr){
            if (cateArr[i] != id) {
                new_arr.push(cateArr[i]);
            }
        }
        cateArr = new_arr;
    }
    console.log(cateArr)
})

$(document).on("click","#addCategory",function () {
    let fids = JSON.stringify(fileArr);
    let cids = JSON.stringify(cateArr);
    $.post("/file/addFileCategory",{"fileIds":fids,"cateids":cids},function(e){
        window.location.reload();

    })
});

$(function () {
    $("[data-delete]").on("click",function(){
        $.get("/file/batchDeleteFile?fileIds="+fileArr,function(data){
            console.log(data)
            window.location.reload();
        });
    });
})

