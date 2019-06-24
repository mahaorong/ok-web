$(function () {

    $("[data-check]").click(function () {

        var id = $(this).val();
        var action;
        if($(this).prop('checked')){
            action = true;
        }else {
            action = false;
        }
        $.post("/user/banUser",{"id":id, "action":action},function (result) {
            if (result.data == undefined || result.data == ""){
                iziToast.error({
                    position: 'topRight',
                    title: "权限不足",
                    message: 'Successfully!',
                });
            }else {
                iziToast.success({
                    position: 'topRight',
                    title: result.data,
                    message: 'Successfully!',
                });
            }

        })
    });

})

//根据ID查询信息
function deleteById(id)  {
    var flag = confirm("您确认删除用户吗")
    if (flag) {
        $.ajax({
            type: "GET",
            data: {id:id},
            url: "/user/delete",
            success: function (data) {
                //  alert("删除成功");
                console.log(data);
                window.location.href = "/user/findAll";
            },
            error: function (data) {
                alert("删除失败");
            }
        });
    }

}