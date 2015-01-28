package rh.persistence.service.impl;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.OrderDistManager;
import rh.persistence.service.TagService;
import rh.persistence.service.TaskService;

//TODO 3 : it should be validated that the resource is not modified on update and removal
//TODO 1 : Introduce transaction for persistence layer

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDAO taskDAO;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private OrderDistManager distMgr;
    
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
        
        checkAllocatability(task);
        
        Integer order = distMgr.getOrderForHead(task.getUserId());
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
    public void update(Task task) {
        Task persistedTask = taskDAO.get(task.getId());
        if(!Objects.equals(persistedTask.getOrder(), task.getOrder())) {
            checkAllocatability(task);
        }
        
        if(task.getOrder() == null) {
            //The order is optional, copy if it is missing
            task.setOrder(persistedTask.getOrder());
        }
        taskDAO.update(task);
    }

    @Override
    public List<Task> listTasksWithTagsFor(String user) {
        
        List<Task> tasks = taskDAO.list(user);
        tagService.loadTagsFor(tasks);
        
        return tasks;
    }

    @Override
    public void deepUpdate(Task task) { //TODO 3: rename cascaded update? integration with update method?
        tagService.removeTagsOf(task);
        update(task);
        tagService.persist(task.getTags(), task);
    }

    @Override
    public void moveTop(Task task) {

        Integer order = distMgr.getOrderForHead(task.getUserId());
        task.setOrder(order);
        
        taskDAO.update(task);
    }

    @Override
    public void insertAfter(Task task, Task after) {

        Integer order = distMgr.getOrderAfter(after);
        task.setOrder(order);
        update(task);        
    }
    
    private void checkAllocatability(Task task) throws RuntimeException {
        if(!taskDAO.isAllocatable(task)) {
            throw new RuntimeException("Order of the task to save is already in use: " + task);
        }
    }
}
