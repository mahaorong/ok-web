$(function () {
      var checkName = null;
      checkTemplate = function(templateId,type){
         var value = $("#"+type+templateId + "").val();
         checkName = value;
         console.log("checkName==="+checkName);
      }

     editTemplate = function (templateId,type) {
             var value = $("#"+type+templateId + "").val();
             console.log(value)
         if(checkName != value){
             $.ajax({
                 url:"/template/editTemplate",
                 data:{
                     "templateId":templateId,
                     "value":value,
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
