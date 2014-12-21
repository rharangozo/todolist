package rh.web.rest;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rh.domain.Task;
import rh.persistence.service.TaskService;

@RestController
@RequestMapping("/{user}")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //TODO: Is it used anywhere?
    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public Task getTask(@PathVariable int id) {
        return taskService.getTaskBy(id);
    }

    @RequestMapping(value = "/task/{id}", method = RequestMethod.DELETE)
    public void deleteTask(@PathVariable int id) {
        taskService.deleteTaskBy(id);
    }
    
    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody Task taskEntity,
            @PathVariable String user,
            HttpServletRequest request,
            HttpServletResponse response) {

        taskEntity.setUserId(user);
        //TODO: save the task along with the new tags
        int id = taskService.save(taskEntity);
        response.setHeader("Location", request.getRequestURL().append("/").append(id).toString());
    }

    @RequestMapping("task/{id}")
    public void updateTask(@RequestBody Task taskEntity, @PathVariable int id) {
        taskEntity.setId(id);
        taskService.update(taskEntity);
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public List<Task> getListOfTasks(@PathVariable String user) {
        return taskService.listTasksWithTagsFor(user);
    }
}
