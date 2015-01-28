$(document).ready(function () {

    var user = window.location.pathname.split('/')[1];

    function Task() {
        this.id = null;
        this.description = '';
        this.tags = [];

        this.applyEditor = function () {
            $('li[data-id=' + this.id + '] .editor input').val(this.toString());
        };

        this.applyNormal = function () {
            $('li[data-id=' + this.id + '] .normal span.desc').text(this.description);
            $('li[data-id=' + this.id + '] .normal span.tag').remove();
            this.tags.sort();
            for (i = this.tags.length - 1; i >= 0; --i) {

                $('li[data-id=' + this.id + '] .normal span.desc')
                        .after($('#tag-template').html().replace('${tag}', this.tags[i]));
            }
        };

        this.initFromString = function (taskStr) {
            taskStr.split(' ').forEach(function (token) {
                if (token.indexOf('#') === 0) {
                    this.tags.push(token.substring(1));
                } else {
                    this.description += token + ' ';
                }
            }, this);
            this.tags.sort();
            this.description = this.description.trim();
        };

        this.toString = function () {
            var str = this.description;
            str = str.trim();
            this.tags.sort();
            this.tags.forEach(function (tag) {
                str += ' #' + tag;
            }, this);
            return str.trim();
        };

        this.createNewDOMEntryWithoutTags = function () {
            $('#main-content ul')
                    .prepend($('#task-template').html())
                    .find('li[data-id=\\$\\{id\\}]').attr('data-id', this.id);
        };
        
        //TODO 2: add view swapping method
    }

    function taskFromNormalView(id) {
        var task = new Task();
        task.id = id;
        task.description = $('li[data-id=' + id + '] .desc').text();
        task.tags = [];
        $('li[data-id=' + id + '] .tag').each(function (index, tag) {
            task.tags.push($(tag).text());
        });
        task.tags.sort();
        return task;
    }

    $(document).on('click', 'li', function () {
        $(this).find('.normal').addClass("hidden");
        $(this).find('.editor').removeClass("hidden");
        taskFromNormalView($(this).data('id')).applyEditor();
        $(this).find('input').focus();
    });

    $(document).on('blur', 'li', function () {
        closeTaskEdit(this);
    });

    closeTaskEdit = function (editedTask) {
        var task = new Task();
        task.id = $(editedTask).data('id');
        task.initFromString($('li[data-id=' + task.id + '] .editor input').val());
        task.applyNormal();
        $(editedTask).find('.editor').addClass("hidden");
        $(editedTask).find('.normal').removeClass("hidden");
        
        $.ajax({
            type: "PUT",
            url: '/' + user + '/task/' + task.id,
            data: JSON.stringify(task),
            dataType: 'text',
            contentType: "application/json"
                //TODO 2: if update fails show a message to the user
        });
    };

    $(document).on('keyup', '.editor > input', function (event) {
        if (event.keyCode === 13) {
            closeTaskEdit($(this).closest('li'));
        }
    });


    $(document).on('keyup', '#smart-input', function (event) {
        if (event.keyCode === 13) {
            addNewTask();
        }
    });

    $("#smart-btn").click(function () {
        addNewTask();
    });

    addNewTask = function () {
        newTask = new Task();
        newTask.initFromString($('#smart-input').val());
        $('#smart-input').val('');

        $.ajax({
            type: "POST",
            url: "/" + user + "/task",
            data: JSON.stringify(newTask),
            dataType: "text",
            contentType: "application/json",
            success: function (data, textStatus, jqXHR) {
                newTask.id = jqXHR.getResponseHeader('Location').split('/').pop();
                
                newTask.createNewDOMEntryWithoutTags();
                newTask.applyNormal();
                $('li[data-id=' + newTask.id + ']').removeClass('hidden');
            }
        });
    };
    
    $(document).on('click', '.rm-btn', function (event) {
        
        event.stopPropagation();
        var taskLi = $(this).closest('li');
        
        $.ajax({
            type: 'DELETE',
            url: '/' + user + '/task/' + taskLi.data('id'),
            dataType: 'text',
            success: function() {
                taskLi.remove();
            }
        });
    });
    
    $(document).on('click', '.done-btn', function (event) {
        
        event.stopPropagation();
        var taskLi = $(this).closest('li');
        var task = taskFromNormalView(taskLi.data('id'));
        
        //TODO 0: the completeness should be stored! SEE BELOW!
        task.complete = true;
        
        $.ajax({
            type: "PUT",
            url: "/" + user + "/task/" + task.id,
            data: JSON.stringify(task),
            dataType: "text",
            contentType: "application/json",
            success: function () {
                //TODO 0: INSTEAD OF REMOVAL, CROSS IT WITH A LINE
                //OR HIDE IT IN ASSOCIATION WITH THE STATE OF THE VIEW
                //WHICH CAN BE SHOW/HIDE TASKS COMPLETED
                //IMPLEMENTATION: STORE THE COMPLETENESS AND
                //IF IT CHANGES IN THE DOM, UPDATE THE VIEW ACCORDINGLY!
                taskLi.remove();
            }
        });
    });

    $('#main-content ul').sortable({
        update: function (event, ui) {
            
            if(ui.item.index() === 0) {
                console.log('put ' + ui.item.data('id') + ' to top');
                
                $.ajax({
                    type: 'PUT',
                    url: '/' + user + '/task/' + ui.item.data('id') + '/top',
                    dataType: 'text'
                    //TODO 2: show a message if it fails
                });
                
            } else {
                console.log('insert ' + ui.item.data('id') + ' after ' +
                        ui.item.prev().data('id'));
                
                $.ajax({
                    type: 'PUT',
                    url: '/' + user + '/task/' + ui.item.data('id') 
                            + '/insertAfter/' + ui.item.prev().data('id'),
                    dataType: 'text'
                    //TODO 2: show a message if it fails
                });
            }
        }
    });
});
