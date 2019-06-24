$(function () {

    deleteById = function(blogId)  {
        $.ajax({
            url: "/admin/delete/blog",
            type: "POST",
            data: {"blogId":blogId},
            success: function (data) {
                window.location.href = "/admin/findBlog";
                iziToast.success({
                    position: 'topRight',
                    title: '成功',
                    message: 'Successfully!',
                });
            },
            error: function (data) {
                iziToast.error({
                    position: 'topRight',
                    title: '失败',
                    message: 'Illegal operation',
                });
            }
        });
    }




});