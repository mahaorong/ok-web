$(function(){

    		
    		$("#save").click(
    			function(){
    				$.ajax({
    					url:"/admin/addTag",
    					data:$("#addForm").serialize(),
    					type:"POST",
    					success:function(data){ //回调函数
    						//alert(data['username']);
                            if(null != data){
                                $.each(data,function(field,message){
                                    console.log(field);
                                    console.log(message);
                                    $("#"+field+"").html(message);
                                    setTimeout(function(){
                                        $("#"+field+"").html("");
                                    },4000);
                                });
                            }
    						if("success" == data){
    							window.location.href = "/admin/findAllTag";
    						}
    					}
    					
    				});
    			}		
    		);
    		
    	});