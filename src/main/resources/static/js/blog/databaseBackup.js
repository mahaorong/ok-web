//函数事件
function databasebackup() {
    $.ajax({
        url: "/admin/exportDatabase",
        type: "post",
        datatype: "json",
        success: function(result){
            if(result.message == "success"){
               console.log('Success','数据库备份成功！');
                iziToast.success({
                    position: 'topRight',
                    title: '数据库备份成功',
                    message: '请使用 WinSCP 传输到本地系统!',
                });
            }
            else{
                console.log('Error','数据库备份失败！');
            }
        }
    })
}
