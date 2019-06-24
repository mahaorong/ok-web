var isCheckNaem = null;
checkName = function(formId){
    var checkNames = $("#formName" + formId + "").val();
    isCheckNaem = checkNames;
}
saveName = function(formId){
    var formName = $("#formName" + formId + "").val();
    console.log("变化前==" + isCheckNaem);
    console.log("变化后==" + formName);
    if (isCheckNaem != formName){
        $.ajax({
            url : "/form/addName",
            data: {formId:formId,formName:formName},
            type:"POST",
            success:function (data) {
                iziToast.success({
                    position: 'topRight',
                    title: '修改成功',
                    message: 'Successfully!',
                });
            }
        });
    }else {
        console.log("文字内容未发生改变")
    }
}