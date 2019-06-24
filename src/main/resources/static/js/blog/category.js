$(function () {

    $("#cateSubmit").click(function (e) {
        // $("#cateSubmit").preventDefult();
        var value = $("input[name='text']").val();
        // alert(value)
        if (value.indexOf("/") != -1){
            // alert("存在")
            iziToast.error({
                position: 'topRight',
                title: '创建分类失败',
                message: 'Failure!不能含有 / 符号',
            });
        }else {
            // alert("不存在");
            var formData = $("#cateForm").serialize();
            var url = "/admin/create_category";
            $.post(url, formData, function (data) {
                // alert("成功");
                if (data.message == "success") {
                    window.location.reload("/admin/index");
                } else {
                    iziToast.error({
                        position: 'topRight',
                        title: '创建分类失败',
                        message: '该分类名称已存在，请换个名称',
                    });
                }
            });
        }

    });

})



