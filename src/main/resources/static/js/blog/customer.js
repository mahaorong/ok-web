$(function () {
      var checkName = null;
      checkTemplate = function(id,type){
         var value = $("#"+type+id + "").val();
         checkName = value;
         console.log("checkName==="+checkName);
      }

     editTemplate = function (id,type) {
             var value = $("#"+type+id + "").val();
             console.log(value)
         if(checkName != value){
             $.ajax({
                 url:"/admin/customerEdit",
                 data:{
                     "id":id,
                     "val":value,
                     "type":type
                 },
                 type:"GET",
                 success:function (data) {
                     iziToast.success({
                         position: 'topRight',
                         title: '修改成功',
                         message: 'Successfully!',
                     });
                 }
             });
         }
     }
});
