package rh.web.rest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import rh.domain.TaskEntity;
import rh.persistence.TaskEntityDAO;

@RestController
@RequestMapping("/rest")
public class TaskController {

    @Autowired
    private TaskEntityDAO taskEntityDAO;

    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.GET)
    public TaskEntity getTask(@PathVariable int id) {
        return taskEntityDAO.get(id);
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public List<TaskEntity> getListOfTasks() {
        return taskEntityDAO.list();
    }
}
