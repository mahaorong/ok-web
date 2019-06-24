let arr = [];
// var value = "";
$(function(){
    testEditor = editormd("test-editormd", {
        width   : "100%",
        height  : 640,
        syncScrolling : "single",
        path    : "/lib/",

        //为了能够提交表单，使用这个配置可以让构造出来的HTML代码直接在第二个隐藏的textarea域中，方便post提交表单。
        saveHTMLToTextarea: true,

        imageUpload : true,
        imageFormats : [ "jpg", "jpeg", "gif", "png", "bmp", "webp" ],
        imageUploadURL : "/admin/uploadImg",
        onload: function () {
            // console.log('onload', this);
            // this.fullscreen();
            // this.unwatch();
            // this.watch().fullscreen();
            this.width("100%");
            this.height(900);

            // this.resize("100%", 640);
        }

    });
    $(".tab_input").on("keydown",function(e) {
        if(e.keyCode == 13){
            let text = $(this).val();
            if(text == ""){
                return;
            }
            if(arr.indexOf(text) != -1){
                return;
            }
            $(".no_tab").hide();
            arr.push(text);


            console.log(arr);
            let a = $('<div class="tab"><span>'+ text +'</span><i class="fa fa-close"></i></div>').appendTo($(".tab_div"));
            i_click(a);
            $(this).val("");
        }
    });
})
var i_click = function(a){
    $(a).children("i").on("click",function(){
        $(this).parent().remove();
        if($(".tab_div").find(".tab").length === 0){
            $(".no_tab").show();
        }
        arr.remove($(this).parent().children("span").html());
    })
}
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);

    if (index > -1) {
        this.splice(index, 1);
    }
};
