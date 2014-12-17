package rh.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import rh.persistence.service.TaskService;

@Controller
public class TaskDeskController {

    @Autowired
    private TaskService taskService;

    @RequestMapping("/{user}")
    public ModelAndView getTaskDesk(@PathVariable String user) {
        ModelAndView modelAndView = new ModelAndView("desk");
        modelAndView.addObject("tasks", taskService.listTasksWithTagsFor(user));
        return modelAndView;
    }
}
