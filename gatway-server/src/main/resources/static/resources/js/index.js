function barCode(code) {
    $("#barcode").html(code);
    $.ajax({
        type: "POST",
        url: "/shiro/list",
        data: {
            name:code
        },
        dataType: "json",
        success: function(data){
            $('#ul').empty();
            $.each(data,function (index,value) {
                $('#ul').append($("<li>").html(value.customer));
            })
        }
    });
}