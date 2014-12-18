package rh.persistence.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.TagService;
import rh.persistence.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;
    
    @Autowired
    private TagService tagService;
    
    @Override
    public Task getTaskBy(int id) {
        return taskDAO.get(id);
    }

    @Override
    public void deleteTaskBy(int id) {
        //TODO: remove tags referring to this task
        taskDAO.delete(id);
    }

    @Override
    public int save(Task taskEntity) {
        
        if(taskEntity.getUserId() == null) {
            throw new NullPointerException("User to which the task to save needs to be defined");
        }
        
        return taskDAO.save(taskEntity);
    }

    @Override
    public void update(Task taskEntity) {
        taskDAO.update(taskEntity);
    }

    @Override
    public List<Task> listTasksWithTagsFor(String user) {
        
        List<Task> tasks = taskDAO.list(user);
        tagService.loadTagsFor(tasks);
        
        return tasks;
    }
    
}
