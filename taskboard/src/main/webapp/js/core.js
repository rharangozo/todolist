$(document).ready(function () {

    var user = window.location.pathname.split('/')[1];
        
        
    //TODO: ITS UGLY AND MUST BE FIXED!!! IT DOES NOT WORK BY THE WAY...
    var taskPrototype = function(id, desc, tag) {
        var template = $('#task-template').clone();
        template.find("li [data-id]").attr('data-id', id);
        template.find(".desc").text(desc);
        return template.html();
    };
    

    $("#smart-btn").click(function () {
        var newTask = {
            value: $('#smart-input').val()
        };
        $.ajax({
            type: "POST",
            url: "/" + user + "/task",
            data: '{"description":"' + newTask.value + '"}',
            dataType: "text",
            contentType: "application/json",
            success: function (data, textStatus, jqXHR) {
                $('#smart-input').val('');
                newTask.id = jqXHR.getResponseHeader('Location').split('/').pop();
                
                $("#main-content ul").append(taskPrototype(newTask.id, newTask.value));
            }
        });
    });

    $(document).on('blur', 'span[contenteditable]', function () {
        var changedTask = {
            desc: $(this).html().trim(),
            id: $(this).parent().data('id')
        };
        $.ajax({
            type: "PUT",
            url: '/' + user + '/task/' + changedTask.id,
            data: '{"description":"' + changedTask.desc + '"}',
            dataType: 'text',
            contentType: "application/json"
                    //TODO: if update fails show a message to the user
        });
    });

    $(document).on('click', '.rm-btn', function () {
        
        var task = $(this).parent();
        
        $.ajax({
            type: 'DELETE',
            url: '/' + user + '/task/' + task.data('id'),
            dataType: 'text',
            success: function() {
                task.remove();
            }
        });
    });

});