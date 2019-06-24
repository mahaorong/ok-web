$(function () {

    //删除模板
    deleteById = function(templateId)  {
        if (confirm("确认删除嘛！")){
            $.ajax({
                url: "/template/delete",
                type: "GET",
                data: {"templateId":templateId},
                success: function (data) {
                    // alert("删除成功");
                    window.location.href = "/template/findAll";
                },
                error: function (data) {
                    alert("删除失败");
                }
            });
        }
    }

    hiddenFile = function () {
        $("#fileUpload").hide();
    }

    showFile = function () {
        $("#fileUpload").show();
    }


    $(".md_editor_btn").on("click", function(){
        var tid = $(this).data("id");
        // alert(tid);
        ajax("/template/readHtmlContent?id="+tid,"get").then(function (data) {
            $("#htmlContent").val(data);
            $("#templateId").val(tid);

        })

    })


    $(".me_group_btn").on("click", function () {
        var tid = $(this).data("id");
        $("#templateId").val(tid);
    });

    $("[data-group]").click(function () {
        // var value = $("#editgroup").find("option:selected").val();
        var value = $("#editgroup option:selected").val();
        var id = $("#templateId").val();
        $.post("/template/templateGroup", {"id":id, "tmpGroup":value}, function (result) {
            if (result.code == 0) {
                iziToast.success({
                    position: 'topRight',
                    title: '分组成功',
                    message: 'Successfully!',
                });
            } else if (result.code == 1) {
                iziToast.error({
                    position: 'topRight',
                    title: '分组失败',
                    message: 'Failure!',
                });
            } else {
                iziToast.error({
                    position: 'topRight',
                    title: '权限不足',
                    message: 'Failure!',
                });
            }


        });
    });

})

