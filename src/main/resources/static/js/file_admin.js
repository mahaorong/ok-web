$(function () {
    let img_count = new Array();
    let check_count = 0;
    $("[name='fileIds']").on("click", function () {
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

    $(".sort_close").on("click", function () {
        $(".img_sort_a").addClass("display");
        $(".sort").removeClass("tra_one")
    });

    $(".sort_close2").on("click", function () {
        $(".img_sort_a2").addClass("display");
        $(".sort2").removeClass("tra_one")
    });

    $(document).on("click", "#update_name", function(){
        $(this).parents(".img_box").find(".img_name").addClass("display");
        // $(this).parents(".img_box").find(".updata_name").removeClass("display");
        $(".updata_name").show();
        $("#img_url").hide();
        var aa = $(this).parents(".img_box").find(".img_sort");
        var realWidth = aa.width() - 64;
        console.log(realWidth);
        $(this).parents(".img_box").find(".update_name_input").css({
            width: realWidth
        });
    })
})