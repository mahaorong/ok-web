/*
$(function () {
    //发布博客
    $("#submitBlog").click(function () {
        var userId = $("#userId").val();
        alert(user)
        $.ajax({
            url: '/addBlog/'+userId,
            type: "POST",
            data: $("#addBlogForm").serialize(),
            success:function (data) {
                alert("success");

                if(null != data && data.message != "success"){
                    $.each(data,function(field,message){
                        console.log(field);
                        console.log(message);
                        $("#"+field+"").html(message);
                        setTimeout(function(){
                            $("#"+field+"").html("");
                        },6000);
                    });
                }
                if (data.code == 0) {
                    window.location.href = "/blog";
                }
            },
            error : function(data) {
            }
        });
    });
});*/

var diffArticles = [];

function releaseArticle() {
    var title = $("input[name='title']").val();
    if (title.indexOf("/") != -1) {
        // alert("存在")
        iziToast.error({
            position: 'topRight',
            title: '发布文章失败',
            message: 'Failure!不能含有 / 符号',
        });
    } else {
        var formData = new FormData($("#addBlogForm")[0]);
        var htmlContent = UE.getEditor('editor').getContent();
        var content = UE.getEditor('editor').getContentTxt();
        formData.append('cateids', checkbox_arr.toString());
        formData.append('htmlContent', htmlContent);
        formData.append('bids', diffArticles);
        formData.set('content', content)
        $.ajax({
            url : "/admin/addBlog",
            type : 'POST',
            data : formData,
            cache: false,
            async: false,
            processData : false,  //必须false才会避开jQuery对 formdata 的默认处理
            contentType : false,  //必须false才会自动加上正确的Content-Type

            success: function (result) {
                console.log("成功");

                if(null != result && result.message != "success"){
                    $.each(result,function(field,message){
                        console.log(field);
                        console.log(message);
                        $("#"+field+"").html(message);
                        setTimeout(function(){
                            $("#"+field+"").html("");
                        },6000);
                    });
                }
                if (result.code == 0) {
                    // window.location.href = "/admin/findBlog?status=0&review=0";
                    iziToast.success({
                        position: 'topRight',
                        title: '发布成功',
                        message: 'Successfully!',
                    });
                    // alert(result.data);
                    diffArticles = result.data;
                }

                if (result.code == 1) {
                    iziToast.error({
                        position: 'topRight',
                        title: '发布失败',
                        message: result.data,
                    });

                }

            },
            error: function (result) {
                console.log("失败");
                iziToast.error({
                    position: 'topRight',
                    title: '发布失败',
                    message: 'Illegal operation',
                });
            }
        });
    }

}