$(document).ready(function () {

    var user = window.location.pathname.split('/')[1];

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
                $("#main-content ul").append(
                        '<li contenteditable="true" data-id="' + newTask.id + '">' + newTask.value + "</li>");
            }
        });
    });

    $(document).on('blur', 'li[contenteditable]', function () {
        var changedTask = {
            desc : $(this).html().trim(),
            id : $(this).data('id')
        };
        $.ajax({
           type : "PUT" ,
           url : '/' + user + '/task/' + changedTask.id,
           data : '{"description":"' + changedTask.desc + '"}',
           dataType : 'text',
           contentType: "application/json"
           //TODO: if update fails show a message to the user
        });
    });
});