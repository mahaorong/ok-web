$(function () {
    $(".full_search").mouseover(function () {
        $(".product_search").show();
    });

    $(".nav-item").mouseover(function () {
        $(this).find(".drop_down_list").show();
    }).mouseout(function () {
        $(this).find(".drop_down_list").hide();
    });

    $(".tab_link").on("mouseover", function () {
        let _this_id = $(this).attr("id");
        $(this).parent().find(".active").removeClass("active");
        $(this).addClass("active");
        $("#v-pills-tabContent").find(".active").removeClass("active");
        $("#v-pills-tabContent").find(".show").removeClass("show");
        $("[aria-labelledby=" + _this_id + "]").addClass("active");
        $("[aria-labelledby=" + _this_id + "]").addClass("show");
    });
})

$(document).ready(function () {

    var scroll_top = $(window).scrollTop();
    if (scroll_top >= 550) {
        $(".page").removeClass("page_p_a");
        $(".page").addClass("page_p_f");
        windows_width();
    } else {
        $(".page").addClass("page_p_a");
        $(".page").removeClass("page_p_f");
        windows_width();
    }

    // if(scroll_top > 0){
    //     $(".new_enroo_nav").css({"background-color" : "#000"})
    // }else{
    //     $(".new_enroo_nav").css({"background-color" : "transparent"})
    // }


    $(window).scroll(function () {
        let scroll_top = $(window).scrollTop();
        // let dir_wrap_hei = $(".direction_wrapper").offset().top;
        if (scroll_top >= 550) {
            $(".page").removeClass("page_p_a");
            $(".page").addClass("page_p_f");
            windows_width();
        } else {
            $(".page").addClass("page_p_a");
            $(".page").removeClass("page_p_f");
        }

        // if(scroll_top > 0){
        //     // $(".new_enroo_nav").animate({
        //     //     backgroundColor: '#000'
        //     // }, 200)
        //     $(".new_enroo_nav").css({"background-color" : "#000"});
        //     $(".navbar-collapse").css({"background-color": "#000"})
        // }else{
        //     $(".new_enroo_nav").css({"background-color" : "transparent"})
        //     $(".navbar-collapse").css({"background-color": "transparent"})
        // }
    });

    $(window).resize(function () {
        let Width = $(window).width();
        let container_Width = $(".about_body").children(".container").width();
        let c_width = (Width - container_Width) / 2;
        $(".page_p_f").css("right", c_width + "px");

        // console.log(c_width);
    });

    let herf_loac = window.location.href;

    for (let a = 0; a < $(".tab_a_pro").length; a++) {
        let a_href = $(".tab_a_pro")[a].href;
        if(a_href == herf_loac){
            $(".tab_a_pro")[a].className = "tab_a_pro tab_a_active";
            return;
        }
    }
})

function windows_width() {
    let Width_b = $(window).width();
    let container_Width = $(".about_body").children(".container").width();
    let c_width = (Width_b - container_Width) / 2;
    $(".page_p_f").css("right", c_width + "px")
    // console.log(c_width);
}