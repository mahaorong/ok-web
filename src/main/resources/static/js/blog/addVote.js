$(function () {

    $("#addVote").click(function () {
        var blogId = $("#blogId").val();
        var userId = $("#userId").val();

        $.ajax({
            url:"/addVote/"+blogId,
            data: {"userId":userId},
            type: "POST",
            success:function () {
                alert("点赞成功");
                window.location.href = '/blog/detail/'+blogId;
            },
            error:function () {
                alert("已点赞");
            }
        });
    })


});
