"use strict";

$(function () {
    enroo_slider.init();
    $(".full_search").mouseover(function () {
        $(".product_search").show();
    });

    $(".nav-item").mouseover(function () {
        $(this).find(".drop_down_list").show();
    }).mouseout(function () {
        $(this).find(".drop_down_list").hide();
    });

    $(".tab_link").on("mouseover", function () {
        var _this_id = $(this).attr("id");
        $(this).parent().find(".active").removeClass("active");
        $(this).addClass("active");
        $("#v-pills-tabContent").find(".active").removeClass("active");
        $("#v-pills-tabContent").find(".show").removeClass("show");
        $("[aria-labelledby=" + _this_id + "]").addClass("active");
        $("[aria-labelledby=" + _this_id + "]").addClass("show");
    });
    $(".back-content").on("click", function () {
        $('html , body').animate({
            scrollTop: 0
        }, 'slow');
    })
})

$(document).ready(function () {
    if (!isie()) {
        $("<div class='ie_black_bg'><div class='ie_white_bg'><h3>您使用的浏览器版本过低</h3><div style='margin-top: 15px;'>为保障您的浏览体验，建议您立即升级浏览器！</div></div></div>").appendTo($("body"));
    }
    setIframeHeight(document.getElementById('external-frame'));
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

    if (scroll_top > 0) {
        $(".new_enroo_nav").addClass("backgrouond_000")
    } else {
        $(".new_enroo_nav").removeClass("backgrouond_000")
    }


    $(window).scroll(function () {
        var scroll_top = $(window).scrollTop();
        // let dir_wrap_hei = $(".direction_wrapper").offset().top;
        if (scroll_top >= 550) {
            $(".page").removeClass("page_p_a");
            $(".page").addClass("page_p_f");
            windows_width();
        } else {
            $(".page").addClass("page_p_a");
            $(".page").removeClass("page_p_f");
        }

        if (scroll_top > 0) {
            // $(".new_enroo_nav").animate({
            //     backgroundColor: '#000'
            // }, 200)
            $(".new_enroo_nav").addClass("backgrouond_000");
            $(".navbar-collapse").css({
                "background-color": "#000"
            })
        } else {
            $(".new_enroo_nav").removeClass("backgrouond_000")
            $(".navbar-collapse").css({
                "background-color": "transparent"
            })
        }
    });

    $(window).resize(function () {
        var Width = $(window).width();
        var container_Width = $(".about_body").children(".container").width();
        var c_width = (Width - container_Width) / 2;
        $(".page_p_f").css("right", c_width + "px");

        // console.log(c_width);
    });

    var herf_loac = window.location.href;

    for (var a = 0; a < $(".tab_a_pro").length; a++) {
        var a_href = $(".tab_a_pro")[a].href;
        if (a_href == herf_loac) {
            $(".change_active")[a].className = "change_active li_active_menu";
            $(".tab_a_pro")[a].className = "tab_a_pro tab_a_active";
            return;
        }
    }
})

function windows_width() {
    var Width_b = $(window).width();
    var container_Width = $(".about_body").children(".container").width();
    var c_width = (Width_b - container_Width) / 2;
    $(".page_p_f").css("right", c_width + "px")
    // console.log(c_width);
}
// document.domain = "caibaojian.com";
var setIframeHeight = function(iframe) {
    if (iframe) {
        var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
        if (iframeWin.document.body) {
            iframe.height = iframeWin.document.documentElement.offsetHeight + 20 || iframeWin.document.body.offsetHeight + 20;
        }
    }
};

function isie() {
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
    var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器  
    var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器  
    var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
    if (isIE) {
        var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
        reIE.test(userAgent);
        var fIEVersion = parseFloat(RegExp["$1"]);
        if (fIEVersion < 10) {
            return false;
        } else {
            return true;
        }
    } else {
        return true;
    }
}
var enroo_slider = {
    //当前滚动张数
    slider_num: 3,
    //每屏展示张数
    index_num: 3,
    //图片数量
    len: 0,
    //right当前可否移动,
    right_status: true,
    //left当前可否移动
    left_status: true,
    //图片大小
    img_width: 0,
    init: function(){
        if (!$(".b_div").length && !$(".b_div img").length) {
            return;
        }
        var len = $(".b_div").children().length;
        var that = this;
        this.len = len;
        var img_width = $(".w_div")[0].offsetWidth / this.index_num - 22;
        this.img_width = img_width;
        $(".b_div img").css({
            "width": img_width + "px"
        });
        $(".b_div").get(0).style.width = this.len * ($(".b_div img")[0].offsetWidth + 22) + "px";
        $(".left_btn").get(0).style.top = ($(".w_div")[0].offsetHeight - 23) / 2 + "px";
        $(".right_btn").get(0).style.top = ($(".w_div")[0].offsetHeight - 23) / 2 + "px";
        $(".b_div img").on('click', function(){
            that.img_click(this);
        });
    },
    img_click: function(a){
        $(".product_detail_img img").attr('src', a.src);
    },
    right_btn_fun: function(){
        var that = this;
        if (this.slider_num >= this.len) {
            return;
        }
        if (this.right_status) {
            this.right_status = false;
            var now_left = $(".b_div")[0].offsetLeft;
            $(".b_div").animate({
                'left': now_left - this.img_width - 22 + "px"
            }, function () {
                that.right_status = true;
            });
            this.slider_num++;
        }
    },
    left_btn_fun: function(){
        var that = this;
        if (this.slider_num <= this.index_num) {
            return;
        }
        if (this.left_status) {
            this.left_status = false;
            var now_left = $(".b_div")[0].offsetLeft;
            $(".b_div").animate({
                'left': now_left + this.img_width + 22 + "px"
            }, function () {
                that.left_status = true;
            });
            this.slider_num--;
        }
    }
}