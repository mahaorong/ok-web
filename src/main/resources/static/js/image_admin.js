$(function () {
    let img_count = new Array();
    let check_count = 0;
    $("[name='imageIds']").on("click", function () {
        let that = this;
        let file_id = $(this).data("id");
        if (that.checked) {
            $(this).parents(".checkbox").addClass("block");
            check_count++;
            img_count.push(file_id);
            $(".enroo_img_top").addClass("open")
        } else {
            console.log(check_count)
            $(this).parents(".checkbox").removeClass("block");
            check_count--;
            img_count.slice(file_id);
        }
        if (check_count == 0) {
            $(".enroo_img_top").removeClass("open")
        }
        // console.log(img_count)
    });

    $(".close_icon").on("click", function () {
        $(".enroo_img_top").removeClass("open");
        let checkboxa = document.getElementsByClassName("checkboxs");
        for (let i = 0; i < checkboxa.length; i++) {
            checkboxa[i].parentNode.className = "checkbox"
            checkboxa[i].checked = false;
        }
        check_count = 0;
    });

    $(".add_sort").on("click", function () {
        $(".img_sort_a").removeClass("display");
        $(".sort").addClass("tra_one")
    });
    $(".add_sort2").on("click", function () {
        $(".img_sort_a2").removeClass("display");
        $(".sort2").addClass("tra_one");
        tid = $(this).data("id");
        // alert(tid);
    });
    $(".add_sort3").on("click", function () {
        $(".img_sort_a2").removeClass("display");
        $(".sort2").addClass("tra_one");
        tid = $(this).data("id");
        $.ajax({
            async:false,
            url: "/admin/findOneBlog",
            type:"get",
            data:{id:tid},
            success:function (data) {
                $("#editor").val(data);
            }

        });
        UE.getEditor("editor");
    });

    $("[data-delete]").on("click",function () {
        $.get("/image/batchDelete?id="+imgArr,function (data) {
            if (data == "success"){
                window.location.reload();
            }
        })
    })

    $(".sort_close").on("click", function () {
        $(".img_sort_a").addClass("display");
        $(".sort").removeClass("tra_one")
    });

    $(".sort_close2").on("click", function () {
        $(".img_sort_a2").addClass("display");
        $(".sort2").removeClass("tra_one");
    });
    
    $(document).on("click", "#update_name", function(){
        $(this).parents(".img_box").find(".img_name").addClass("display");
        // $(this).parents(".img_box").find(".updata_name").removeClass("display");
        $(this).parents(".img_box").find(".updata_name").show();
        var aa = $(this).parents(".img_box").find(".img_sort");
        var realWidth = aa.width() - 64;
        console.log(realWidth);
        $(this).parents(".img_box").find(".update_name_input").css({
            width: realWidth
        });
    });

    $(document).on("click", "#img_description", function(){
        $(this).parents(".img_box").find(".img_description").show();
       /* $(this).parents(".img_box").find(".img_description_input").css({
            width: realWidth
        });*/
    });

    $(document).on("click", "#img_url", function(){
        $(this).parents(".img_box").find(".image_url").show();
    })

    $(document).on("click", "#href_url", function(){
        $(this).parents(".img_box").find(".img_hrefUrl").show();
    })

})