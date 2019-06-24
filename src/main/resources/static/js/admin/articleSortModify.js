$(function () {
    $("[data-sort]").on("blur", function () {
        var num = $(this).text();
        var id = $(this).data("id");
        var url = "/admin/editArticleSort";

        var data = {"id": id, "sortNum": num};
        $.get(url, data, function (resp) {
            if (resp.message === 'success') {
                iziToast.success({
                    position: 'topRight',
                    title: '修改序号成功，请刷新！',
                    message: 'Successfully!'
                });
            } else {
                iziToast.error({
                    position: 'topRight',
                    title: '修改失败',
                    message: "Fail,您没有修改权限!",
                });

            }

        });
    });
});