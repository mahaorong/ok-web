var imgArr = [];
var cateArr = [];
$(document).on("click","input[name='worktableIds']",function () {
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
    $.post("/product/addWorkCategory",{"worktableIds":mids,"cateids":cids},function(e){
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
