$(document).ready(function () {

    var user = window.location.pathname.split('/')[1];
    
    function Task() {
        this.id = null;
        this.desc = '';
        this.tags = [];
        
        this.initFromStr = function (taskStr) {

            taskStr.split(' ').forEach(function (token) {
                if (token.indexOf('#') === 0) {
                    this.tags.push(token);
                } else {
                    this.desc += token + ' ';
                }
            }, this);
            this.desc.trim();
        };
      
        this.append = function (selector) {
            
            $(selector).append($('#task-template').html()
                    .replace("${id}", this.id)
                    .replace("${description}", this.desc));

            tagTemplate = $('li[data-id=' + this.id + '] div.tag').html();
            
            this.tags.forEach(function (tag) {
                $('li[data-id=' + this.id + '] div.tag').append(
                        tagTemplate.replace('${tagName}', tag));
            }, this);
            
            $('li[data-id=' + this.id + '] span.tag:first').remove();
            //TODO: template should be hidden by default, it should become visible now
        };
    }   

    $("#smart-btn").click(function () {

        newTask = new Task();
        newTask.initFromStr($('#smart-input').val());
        
        $.ajax({
            type: "POST",
            url: "/" + user + "/task",
            data: '{"description":"' + newTask.desc + '","tags":'+
                    JSON.stringify(newTask.tags)+'}',
            dataType: "text",
            contentType: "application/json",
            success: function (data, textStatus, jqXHR) {
                $('#smart-input').val('');
                newTask.id = jqXHR.getResponseHeader('Location').split('/').pop();
                
                newTask.append("#main-content ul");
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