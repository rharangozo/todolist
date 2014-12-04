package rh.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import rh.persistence.TaskEntityDAO;

@Controller
public class TaskDeskController {

    @Autowired
    private TaskEntityDAO taskEntityDAO;

    @RequestMapping("/")
    public ModelAndView getTaskDesk() {
        ModelAndView modelAndView = new ModelAndView("desk");
        modelAndView.addObject("tasks", taskEntityDAO.list());
        return modelAndView;
    }
}
