$(function () {
    addSubreview=function (commentId) {
        var blogId = $("#blogId").val();
        var userId = $("#userId").val();
            //alert("ssssss")
            $.ajax({
                url:"/addSubreview/comment/"+commentId+"/"+blogId+"/"+userId,
                type:"GET",
                data:$("#addCommentForm"+commentId+"").serialize(),
                success:function (data) {
                    // alert("success");
                    if (null != data && data.message != "success") {
                        $.each(data, function (field, message) {
                            console.log(field);
                            console.log(message);
                            $("#content1").html(message);
                            setTimeout(function () {
                                $("#content1").html("");
                            }, 6000);
                        });

                    }
                    if (data.code == 0 ) {
                        window.location.href = '/blog/detail/'+blogId;
                    }
                    if (data.code == 1) {
                        window.location.href = '/login';
                    }
                }
            })
    }
})