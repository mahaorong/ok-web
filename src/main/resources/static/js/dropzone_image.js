Dropzone.autoDiscover = false;
$("#dropzone").dropzone({
    url: "save",
    method: "post",
    maxFiles: 100,
    maxFilesize: 100, //MB
    createImageThumbnails: false,
    acceptedFiles: ".png,.jpg,.gif,.bmp,.jpeg,.JPG,.PNG",
    clickable: "#dropzone_btn",
    dictMaxFilesExceeded: "您一次最多只能上传{{maxFiles}}个文件",
    dictFileTooBig: "文件过大({{filesize}}MB). 上传文件最大支持: {{maxFilesize}}MB.",
    addRemoveLinks: true,
    init: function () {

        this.on("success", function (file, response) {
            console.log(response);
            iziToast.success({
                position: 'topRight',
                title: '上传成功',
                message: 'Successfully! 2秒后进行刷新！',
            });
            setTimeout(function () {
                window.location.reload();
            }, 2000);
            /*var current_date = getNowFormatDate();
            console.dir(file);
            console.dir(response);*/
            // window.location.reload();
           /* if ($(".lightbox").length == 0 || current_date != $(".ptb-title-box").first().find(":checkbox").data("date")) {
                var tpl = new Template($('#tpl2').html());
                // var s = tpl.render({
                //     name: file.name,
                //     id: file.xhr.responseText,
                //     id: file.xhr.responseText,
                //     // date: current_date
                //     date: current_date
                // });
                // $("#dropzone").after(s);
            } else {
                var tpl = new Template($('#tpl').html());
                var s = tpl.render({
                    name: file.name,
                    id: file.xhr.responseText,
                    date: current_date

                });
                $(".lightbox").first().prepend(s);
            }*/
            // componentHandler.upgradeDom();
        });

        this.on("removedfile", function (file) {
            $("[data-new='" + file.name + "']").first().find(".file_delete").click();
        });
        this.on("queuecomplete", function () {
            setTimeout(function () {
                window.location.reload();
            }, 3000);

        })
    }
});