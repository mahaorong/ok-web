setInterval(function(){
    if($(document).scrollTop() > 0 ) {
        $("#topdh").addClass("addClass_back")
    }else {
        $("#topdh").removeClass("addClass_back")
    }
}, 100);