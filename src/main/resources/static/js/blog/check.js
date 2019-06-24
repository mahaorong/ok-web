$(function () {
	
	Array.prototype.remove = function (array){   //自定义数组(Array)原型函数remove用于移除数组中的一个指定元素
		var index = array.indexOf(array);
		if(index > -1){
			this.splice(index,1);
		}
	}




	checkArticle = function(){

	}


	checkCategory = function(obj){
		var categoryId = obj.value;
		/*alert(categoryId)
		alert(obj.checked);*/
		if(obj.checked){
			$("#categorySize").fadeIn("5000");
			categoryIds.push(categoryId);
			// alert(categoryIds);
			checkOrder++;
		}else{
			categoryIds = $.grep(categoryIds,function (value) {
				return value != categoryId;
			})
			// categoryIds.remove(categoryId);
			checkOrder--;
			// alert(categoryIds);

			if(checkOrder <= 0){
				checkOrder = 0;
				$("#categorySize").fadeOut();
			}
		}

	}

	
	
	$(document).on("click","[data-files-delete]",function(){
		  var url = "/product/deletes";
		  var json_id = JSON.stringify(ids);
          console.log(json_id);
          var data = {
        		  json_id:json_id
          }
          $.post(url,data,function(e){
        	  if(e != null && e == "success"){
        		  for(var i=0;i<ids.length;i++){
                	  var pro_div = $('[data-id="' +ids[i]+ '"]');
                	  pro_div.hide("slow");
                  }
                  $(".hide").hide("fast");
 
                  setTimeout(function(){
                	  location.reload();
                  },1500);
        	  }else{
        		  alert("传输错误");
        	  }
          });
       
	});
	
		
})
