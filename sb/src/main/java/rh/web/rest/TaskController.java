package rh.web.rest;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rh.domain.TaskEntity;

@RestController
public class TaskController {

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
    public TaskEntity getTask(@PathVariable int id) {
        
        //TODO: remove this 'mock' data
        TaskEntity te = new TaskEntity();
        te.setDescription("Hello World Id: " + id);
        te.setId(id);
        return te;
    }
    
    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public List<TaskEntity> getListOfTasks() {
        
        //TODO: remove mock
        TaskEntity e1 = new TaskEntity();
        e1.setDescription("Entity 1");
        e1.setId(1);
        
        TaskEntity e2 = new TaskEntity();
        e2.setDescription("Entity 2");
        e2.setId(2);
        
        return Arrays.asList(e1, e2);
    }
}
