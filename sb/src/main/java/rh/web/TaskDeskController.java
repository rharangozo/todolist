package rh.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskDeskController {

    @RequestMapping("/")
    String getTaskDesk() {
        return "desk";
    }
}
