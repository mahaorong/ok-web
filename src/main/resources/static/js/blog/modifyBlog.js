function releaseArticle() {

    var formData = new FormData($("#addBlogForm")[0]);
    var htmlContent = UE.getEditor('editor').getContent();
    var content = UE.getEditor('editor').getContentTxt();
    formData.append('cateids', checkbox_arr.toString());
    formData.append('htmlContent', htmlContent);
    var bids = JSON.stringify(diffArticles);
    formData.append('bids', bids);
    formData.set('content', content)

    console.log(formData);
    $.ajax({
        url : "/admin/modify",
        type : 'POST',
        data : formData,
        cache: false,
        async: false,
        processData : false,  //必须false才会避开jQuery对 formdata 的默认处理
        contentType : false,  //必须false才会自动加上正确的Content-Type

        success: function (data) {
            console.log("成功");

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
                window.location.href = "/admin/findBlog?status=0&review=1";
            }
        },
        error: function (data) {
            console.log("失败");
        }
    });
}