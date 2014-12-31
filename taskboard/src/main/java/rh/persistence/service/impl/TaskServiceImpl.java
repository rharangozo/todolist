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

        Integer orderToInsert = after.getOrder() + 1;
        task.setOrder(orderToInsert);
        try{
            update(task);
        }catch(RuntimeException re){
            
            //TODO: It is built upon RuntimeException that can be caused some
            //other reason not just that the next order is reserved! It as is can hide
            //other exceptions!
            //
            //TODO: The order distribution normalized including the task which will be updated after that
            
            normalizeOrderDistribution(task.getUserId());
            update(task);
        }
    }
    
    //TODO: Extract order distribution management

    private Integer beforeHeadOrder(String userId) {
        
        Task head = taskDAO.getHead(userId);
        
        Integer freeOrderVal;
        try {
            freeOrderVal = beforeHeadOrderHelper(head);
            
        } catch (NoFreeOrderException nfoe) {
            
            normalizeOrderDistribution(userId);
            //Retry to find free order value before the head item
            head = taskDAO.getHead(userId); //TODO: can normalizeOrderDist return with this instead of query it again...
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
