package rh.persistence.service.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.OrderDistManager;
import rh.persistence.service.TagService;
import rh.persistence.service.TaskService;

//TODO 3 : it should be validated that the resource is not modified on update and removal

@Service
public class TaskServiceImpl implements TaskService {

    @Resource(name = "taskDAO")
    private TaskDAO taskDAO;
    
    @Resource(name = "nativeTaskDAO")
    private TaskDAO nativeTaskDAO;
    
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
    public void update(Task taskEntity) {
        taskDAO.update(taskEntity);
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
    
    //TODO 0 : This TaskServiceImpl should take care of the data integrity instead of that
    //the DAO does it. And so the new distribution manager can modify the orders
    //bypassing the constraints on orders which are enfored by this task service
    //
    //DAO - do the operation without taking care of integrity
    //Service - check the data integrity before delegate the call to the DAO, implements
    //          operations on DAO
    //Distribution management - Provides free orders, normalize orders 

}
