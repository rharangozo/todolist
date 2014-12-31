package rh.persistence.service.impl;

import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.OrderDistManager;

@Service
public class UniformOrderDistMgr implements OrderDistManager {

    @Autowired
    private TaskDAO taskDAO;
    
    //TODO 1: make it configurable
    private Integer lowLimit = 0;
    
    //TODO 1: make it configurable
    private Integer highLimit = 10;
    
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
        //TODO 0: If next does not exist, 
        //the null object's order property should be the highLimit! 
        Task next = taskDAO.next(task);
        if(task.getOrder() + 1 < next.getOrder()) {
            return (next.getOrder() - task.getOrder()) / 2 + task.getOrder();
        } else {
            return normalizationAndInjectionAfter(task);
        }
    }

    @Override
    public void normalization(String userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        Integer ret = generator.next();
        
        for(int i = 0; generator.hasNext(); ++i) {
            updateTask(tasks, i, generator);
        }
        return ret;        
    }

    private Integer normalizationAndInjectionAfter(Task afterTask) {
        
        List<Task> tasks = taskDAO.list(afterTask.getUserId());
        
        checkCapacity(tasks);
        
        Iterator<Integer> generator = new OrderDistGenerator(tasks.size() + 1);
        Integer ret = null;
        
        for(int i = 0; generator.hasNext(); ++i) {
            Task task = updateTask(tasks, i, generator);
            
            if(ret == null && task.getId() == afterTask.getId()) {
                ret = generator.next();
            }
        }
        
        return ret;
    }
    
    private void checkCapacity(List<Task> tasks) throws RuntimeException {
        if(tasks.size() >= (highLimit - lowLimit + 1)) {
            //TODO 1 - No free space
            throw new RuntimeException();
        }
    }

    private Task updateTask(List<Task> tasks, int i, Iterator<Integer> generator) {
        Task task = tasks.get(i);
        task.setOrder(generator.next());
        taskDAO.update(task);
        return task;
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
