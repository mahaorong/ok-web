/*$(function () {
     uploadFile = function () {
         var formData = new FormData($("#imageForm")[0]);
             $.ajax({
                 url:"/image/save",
                 data:formData,
                 type:"POST",
                 processData : false,  //必须false才会避开jQuery对 formdata 的默认处理
                 contentType : false,  //必须false才会自动加上正确的Content-Type
                 success:function (data) {

                     iziToast.success({
                         position: 'topRight',
                         title: '上传成功',
                         message: 'Successfully!',
                         timeout: 3000,
                     },setTimeout(function(){
                         window.location.href="/image/findAll"
                     },3000));
                 }
             });
     }
});*/


var imgArr = [];
var cateArr = [];
$(document).on("click","input[name='imageIds']",function () {
    let id = $(this).val();
    let new_arr = [];
    if ($(this).prop("checked")){
        imgArr.push(id);
    } else {
        for (var i in imgArr){
            if (imgArr[i] != id) {
                new_arr.push(imgArr[i]);
            }
        }
        imgArr = new_arr;
    }
    console.log(imgArr)
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
    let mids = JSON.stringify(imgArr);
    let cids = JSON.stringify(cateArr);
    /*ajax("/image/addImageCategory","post",{"imageIds":mids,"cateids":cids}).then((mes) => {
    });*/
    $.post("/image/addImageCategory",{"imageIds":mids,"cateids":cids},function(e){
        window.location.reload();
        /*setTimeout(function(){
        iziToast.success({
            position: 'topRight',
            title: '添加成功',
            message: 'Successfully!',
            timeout: 1000,
        }),1000});*/
    })
});
