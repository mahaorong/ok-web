 $(function () {
    //发布评论
    $("#sumbitComment").click(function () {
        var blogId = $("#blogId").val();
        var userId = $("#userId").val();
        $.ajax({
            url:'/addComment/'+blogId+'/'+userId,
            type: "POST",
            data: $("#addCommentForm").serialize(),
            success:function (data) {
                // alert("success");
                if (null != data && data.message != "success") {
                    $.each(data, function (field, message) {
                        console.log(field);
                        console.log(message);
                            $("#content").html(message);
                        setTimeout(function () {
                            $("#content").html("");
                        }, 6000);
                    });

                }
                if (data.code == 0 ) {
                   window.location.href = '/blog/detail/'+blogId;
                }
                if (data.code == 1 ) {
                    window.location.href = '/login';
                }
            }
        });
    });
})