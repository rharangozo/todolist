package rh.persistence.service.impl;

import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.NoFreeOrderException;
import rh.persistence.service.OrderDistManager;

//TODO 2 : Defect: The user can add the maximum number of tasks but then
//the order of the tasks is not modifiable, no free space error is thrown

@Service
@Transactional
public class UniformOrderDistMgr implements OrderDistManager {

    @Autowired
    private TaskDAO taskDAO;
    
    @Value("${order.limit.low}")
    private Integer lowLimit;
    
    @Value("${order.limit.high}")
    private Integer highLimit;
    
    @Override
    public Integer getOrderForHead(String userId) {
        Task head = taskDAO.getHead(userId);
        if(head == Task.Special.NULL.getInstance()) {
            return (highLimit - lowLimit) / 2 + lowLimit;
        } else {
            if(head.getOrder() <= lowLimit) {                
                return normalizationForHead(userId);
            } else {
                return (head.getOrder() - lowLimit) / 2 + lowLimit;
            }
        }
    }

    @Override
    public Integer getOrderAfter(Task task) {

        Integer nextOrder = nextOrderOrMaxLimit(task);
        if(task.getOrder() + 1 < nextOrder) {
            return (nextOrder - task.getOrder()) / 2 + task.getOrder();
        } else {
            return normalizationAndInjectionAfter(task);
        }
    }

    @Override
    public void normalization(String userId) {
        
        List<Task> tasks = taskDAO.list(userId);
        
        if(tasks.isEmpty()) {
            return;
        }
        
        OrderDistGenerator generator = new OrderDistGenerator(tasks.size());
        
        for(int i = 0; generator.hasNext(); ++i) {
            updateTasksItemWithNextOrderValue(tasks, i, generator);
        }
    }

    void setTaskDAO(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    void setLowLimit(Integer lowLimit) {
        this.lowLimit = lowLimit;
    }

    void setHighLimit(Integer highLimit) {
        this.highLimit = highLimit;
    }
    
    private Integer normalizationForHead(String userId) {
        
        List<Task> tasks = taskDAO.list(userId);
        
        checkCapacity(tasks);
        
        Iterator<Integer> generator = new OrderDistGenerator(tasks.size() + 1);
        Integer freeOrder = generator.next();
        
        for(int i = 0; generator.hasNext(); ++i) {
            updateTasksItemWithNextOrderValue(tasks, i, generator);
        }
        return freeOrder;        
    }

    private Integer normalizationAndInjectionAfter(Task afterTask) {
        
        List<Task> tasks = taskDAO.list(afterTask.getUserId());
        
        checkCapacity(tasks);
        
        Iterator<Integer> generator = new OrderDistGenerator(tasks.size() + 1);
        Integer freeOrder = null;
        
        for(int i = 0; generator.hasNext(); ++i) {
            Task task = updateTasksItemWithNextOrderValue(tasks, i, generator);
            
            if(freeOrder == null && task.getId() == afterTask.getId()) {
                freeOrder = generator.next();
            }
        }
        
        return freeOrder;
    }
    
    private void checkCapacity(List<Task> tasks) throws RuntimeException {
        if(tasks.size() >= (highLimit - lowLimit + 1)) {
            throw new NoFreeOrderException();
        }
    }

    private Task updateTasksItemWithNextOrderValue(List<Task> tasks, int i, Iterator<Integer> generator) {
        Task task = tasks.get(i);
        task.setOrder(generator.next());
        taskDAO.update(task);
        return task;
    }

    private Integer nextOrderOrMaxLimit(Task task) {
        Task nextTask = taskDAO.next(task);
        if(Task.Special.NULL.getInstance().equals(nextTask)) {
            return this.highLimit;
        }
        return nextTask.getOrder();
    }
    
    private class OrderDistGenerator implements Iterator<Integer> {

        private final Integer numOfElements;
        private Integer index;
        private final Integer step;
        private final Integer minPlusHalfStep;
        
        OrderDistGenerator(int numOfElements) {
            this.index = 0;
            this.numOfElements = numOfElements;
            this.step = (highLimit + 1 - lowLimit) / numOfElements;
            this.minPlusHalfStep = lowLimit + step / 2;
        }
        
        @Override
        public boolean hasNext() {
            return index < numOfElements;
        }

        @Override
        public Integer next() {
            return minPlusHalfStep + index++ * step;
        }
    }
}
