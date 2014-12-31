package rh.persistence.service.impl;

import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.FreeOrderLookup;
import rh.persistence.service.NoFreeOrderException;
import rh.persistence.service.TagService;
import rh.persistence.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Resource(name = "taskDAO")
    private TaskDAO taskDAO;
    
    @Resource(name = "nativeTaskDAO")
    private TaskDAO nativeTaskDAO;
    
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

        Integer order = beforeHeadOrder(task.getUserId());
        task.setOrder(order);
        
        taskDAO.update(task);
    }

    @Override
    public void insertAfter(Task task, Task after) {

        Integer orderToInsert = after.getOrder() + 1;
        task.setOrder(orderToInsert);
        try{
            update(task);
        }catch(RuntimeException re){
            
            //TODO 1: It is built upon RuntimeException that can be caused some
            //other reason not just that the next order is reserved! It as is can hide
            //other exceptions!
            
            normalizeOrderDistribution(task.getUserId());
            update(task);
        }
    }
    
    //TODO 1 : Extract order distribution management. Idea: task integrity decorator
    //should be removed and the logic implemented in it and the distribution management
    //logic implemented in this service should be extracted into a new class
    //which can be another service.
    //This TaskServiceImpl should take care of the data integrity instead of that
    //the DAO does it. And so the new distribution manager can modify the orders
    //bypassing the constraints on orders which are enfored by this task service
    //
    //DAO - do the operation without taking care of integrity
    //Service - check the data integrity before delegate the call to the DAO, implements
    //          operations on DAO
    //Distribution management - Provides free orders, normalize orders 

    private Integer beforeHeadOrder(String userId) {
        
        Task head = taskDAO.getHead(userId);
        
        Integer freeOrderVal;
        try {
            freeOrderVal = beforeHeadOrderHelper(head);
            
        } catch (NoFreeOrderException nfoe) {
            
            normalizeOrderDistribution(userId);
            //Retry to find free order value before the head item
            head = taskDAO.getHead(userId); //TODO 2: normalizeOrderDist can return with this instead of query it again...
            freeOrderVal = beforeHeadOrderHelper(head);
        }
        
        return freeOrderVal;
    }

    private Integer beforeHeadOrderHelper(Task head) {
        return fol.freeOrderBetween(FreeOrderLookup.HEAD, head.getOrder());
    }

    private void normalizeOrderDistribution(String userId) {
        List<Task> tasks = taskDAO.list(userId);
        Iterator<Integer> newOrdersIterator = 
                fol.orderReDistribution(
                        FreeOrderLookup.HEAD, 
                        FreeOrderLookup.TAIL, 
                        tasks.size());
        
        tasks.forEach(task -> {
            task.setOrder(newOrdersIterator.next());
            nativeTaskDAO.update(task);
        });
    }

}
