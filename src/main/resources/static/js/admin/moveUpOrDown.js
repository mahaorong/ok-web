//需要导入jquery-ui.js此包
$(document).ready(function() {
    var fixHelperModified = function(e, tr) {
            //children() 方法返回返回被选元素的所有直接子元素
            var $originals = tr.children();
            //clone() 方法生成被选元素的副本，包含子节点、文本和属性
            var $helper = tr.clone();
            //each() 方法规定为每个匹配元素规定运行的函数
            $helper.children().each(function(index) {
                //width() 方法返回或设置匹配元素的宽度
                //eq() 方法将匹配元素集缩减值指定 index 上的一个
                $(this).width($originals.eq(index).width())
            });
            return $helper;
        },
        updateIndex = function(e, ui) {
            //存所有id
            var formId = [];
            //存所有排序
            var sort = [];
            //ui.item - 表示当前拖拽的元素
            //parent() 获得当前匹配元素集合中每个元素的父元素，使用选择器进行筛选是可选的
            $('td.index', ui.item.parent()).each(function(i) {
                //html() 方法返回或设置被选元素的内容 (inner HTML)
                $(this).html(i + 1);
                var sorts = $(this).html();
                sort.push(sorts)
            });
            $('td.idClass', ui.item.parent()).each(function(i) {
                //html() 方法返回或设置被选元素的内容 (inner HTML).
                var formIds = $(this).html();
                formId.push(formIds)
            });
            var sort = sort.join(',');
            var formItemId = formId.join(',');
            $.ajax({
                url:"/formItem/saveFormItem",
                data:{"sort":sort,"id":formItemId,"operating":"saveSort"},
                type:"POST",
                success:function (data) {
                    iziToast.success({
                        position: 'topRight',
                        title: '修改成功',
                        message: 'Successfully!',
                    });
                }
            })
        };
    //拖动id为sort表格下的tbody子元素
    $("#sort tbody").sortable({
        //设置拖动时,鼠标样式为小手型
        cursor: "pointer",
        opacity: 0.6,
        //设置是否在拖拽元素时,显示一个辅助的元素.可选值：'original', 'clone'
        helper: fixHelperModified,
        //当排序动作结束时触发此事件。
        stop: updateIndex,
    }).disableSelection();
    
    $("#optionSort tbody").sortable({
        cursor: "pointer",
        opacity: 0.6,
        helper: fixHelperModified
    }).disableSelection();
});

