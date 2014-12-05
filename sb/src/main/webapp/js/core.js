$(document).ready(function(){

    $("#smart-btn").click(function(){
        var newTask = {
            value : $('#smart-input').val()
        };
        $.ajax({
            type: "POST",
            url: "/rest/task",
            data: '{"description":"' + newTask.value + '"}',
            dataType: "text",
            contentType: "application/json",
            success: function(data, textStatus, jqXHR){
                $('#smart-input').val('');
                //TODO: Later the ID needs to be added to the DOM
                newTask.id = jqXHR.getResponseHeader('Location').split('/').pop()
                $("#main-content ul").append("<li>"+newTask.value+"</li>");
            }
        });
    });
});