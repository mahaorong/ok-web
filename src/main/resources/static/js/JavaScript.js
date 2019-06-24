/* nav start*/
setInterval(function(){
    if($(document).scrollTop() > 0 ) {
        $("#topdh").addClass("addClass_back")
    }else {
        $("#topdh").removeClass("addClass_back")
    }
}, 100);
/**/
$(document).ready(function($){
    // 浏览器窗口滚动（以像素为单位），后面显示“返回到顶部”链接
    var offset = 300,
        //浏览器窗口滚动（像素）后，“回顶”链接透明度降低。
        offset_opacity = 1200,
        //duration of the top scrolling animation (in ms)
        scroll_top_duration = 700,
        //顶部滚动动画的持续时间（毫秒）
        $back_to_top = $('.cd-top');
    //隐藏或显示“返回到顶部”链接
    $(window).scroll(function(){
        ( $(this).scrollTop() > offset ) ? $back_to_top.addClass('cd-is-visible') : $back_to_top.removeClass('cd-is-visible cd-fade-out');
        if( $(this).scrollTop() > offset_opacity ) {
            $back_to_top.addClass('cd-fade-out');
        }
    });
    //平滑滚动到顶部
    $back_to_top.on('click', function(event){
        event.preventDefault();
        $('body,html').animate({
                scrollTop: 0 ,
            }, scroll_top_duration
        );
    });

});


