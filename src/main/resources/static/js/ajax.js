const ajax = function(url, type, data = {}){
    return new Promise(function(succ, erro){
        $.ajax({
            url: url,
            type: type,
            data: data,
            processData:false,
            contentType:false,
            success: (mes) => {
                succ(mes);
            },
            error: (err) => {
                erro(err);
            }
        })
    })
}
window.ajax = ajax;