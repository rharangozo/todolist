package rh.persistence.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.FreeOrderLookup;
import rh.persistence.service.TagService;
import rh.persistence.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private FreeOrderLookup fol;
    
    @Override
    public Task getTaskBy(int id) {
        return taskDAO.get(id);
    }

    @Override
    public void deleteTaskBy(int id) {
        tagService.removeTagsOf(id);
        taskDAO.delete(id);
    }

    @Override
    public int save(Task task) {
        
        if(task.getUserId() == null) {
            throw new NullPointerException("User to which the task to save needs to be defined");
        }
        
        Integer order = beforeHeadOrder(task.getUserId());
        task.setOrder(order);   
        
        return taskDAO.save(task);
    }

    @Override
    public int saveWithTags(Task task) {
        
        int taskId = save(task);
        tagService.persist(task.getTags(), taskId);
        
        return taskId;
    }
    
    @Override
    public void update(Task taskEntity) {
        //TODO: modify to update the tags!!!
        taskDAO.update(taskEntity);
    }

    @Override
    public List<Task> listTasksWithTagsFor(String user) {
        
        List<Task> tasks = taskDAO.list(user);
        tagService.loadTagsFor(tasks);
        
        return tasks;
    }

    @Override
    public void deepUpdate(Task task) {
        tagService.removeTagsOf(task);
        update(task);
        tagService.persist(task.getTags(), task);
    }

    @Override
    public void moveTop(Task task) {

        Integer order = beforeHeadOrder(task.getUserId());
        task.setOrder(order);
        
        taskDAO.update(task);
    }

    @Override
    public void insertAfter(Task task, Task after) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Integer beforeHeadOrder(String userId) {
        Task head = taskDAO.getHead(userId);
        
        //TODO: catch exception and ensure the consistency of task order
        
        if(head == null) {
            return fol.freeOrderBetween(FreeOrderLookup.HEAD, FreeOrderLookup.TAIL);
        } else {
            return fol.freeOrderBetween(FreeOrderLookup.HEAD, head.getOrder());
        }
    }

}
