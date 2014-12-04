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
import rh.domain.TaskEntity;
import rh.persistence.TaskEntityDAO;

@RestController
@RequestMapping("/rest")
public class TaskController {

    @Autowired
    private TaskEntityDAO taskEntityDAO;

    @RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
    public TaskEntity getTask(@PathVariable int id) {
        return taskEntityDAO.get(id);
    }

    @RequestMapping(value = "/task", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void createTask(@RequestBody TaskEntity taskEntity, HttpServletRequest request, HttpServletResponse response) {

        int id = taskEntityDAO.save(taskEntity);
        response.setHeader("Location", request.getRequestURL().append("/").append(id).toString());
    }


    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public List<TaskEntity> getListOfTasks() {
        return taskEntityDAO.list();
    }
}
