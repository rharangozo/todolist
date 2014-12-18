$(document).ready(function () {

    var user = window.location.pathname.split('/')[1];
    
    function Task() {
        this.id = null;
        this.desc = '';
        this.tags = [];
        
        this.initFromStr = function(taskStr) {
            this.id = null;
            this.desc = taskStr;
            //TODO: determine tags
            this.tags = [];
        }
      
        this.toHtml = function() {
      
            template = $('#task-template').clone().html()
                                .replace("${taskId}", this.id)
                                .replace("${taskDescription}", this.desc)
            //TODO: add tag replacements
            return template;
        };
    }   

    $("#smart-btn").click(function () {

        newTask = new Task();
        newTask.initFromStr($('#smart-input').val());
        
        $.ajax({
            type: "POST",
            url: "/" + user + "/task",
            data: '{"description":"' + newTask.desc + '"}',
            dataType: "text",
            contentType: "application/json",
            success: function (data, textStatus, jqXHR) {
                $('#smart-input').val('');
                newTask.id = jqXHR.getResponseHeader('Location').split('/').pop();
                
                $("#main-content ul").append(newTask.toHtml());
            }
        });
    });

    $(document).on('blur', 'span[contenteditable]', function () {

        changedTask = new Task();
        changedTask.id = $(this).parent().data('id');
        changedTask.desc = $(this).html().trim();
        
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
        
        var taskLi = $(this).parent();
        
        $.ajax({
            type: 'DELETE',
            url: '/' + user + '/task/' + taskLi.data('id'),
            dataType: 'text',
            success: function() {
                taskLi.remove();
            }
        });
    });

});