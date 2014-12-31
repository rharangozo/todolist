package rh.persistence.dao.impl;

import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;

@Repository("taskDAO")
public class TaskIntegrityDecorator implements TaskDAO {

    @Resource(name = "nativeTaskDAO")
    private TaskDAO taskDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Task get(int id) {
        return taskDAO.get(id);
    }

    @Override
    public Task getHead(String userId) {
        return taskDAO.getHead(userId);
    }

    @Override
    public void update(Task task) {
        //TODO: validate that the taskorder to use is free for consistency!!!
        Task persistedTask = get(task.getId());
        if(!Objects.equals(persistedTask.getOrder(), task.getOrder()) && 
                !isFreeOrder(task)) {
            throw new RuntimeException("Order of the task to update is already in use:" + task);
        }
        
        if(task.getOrder() == null) {
            //The order is optional, copy if it is missing
            task.setOrder(persistedTask.getOrder());
        }
        taskDAO.update(task);
    }

    @Override
    public void delete(int id) {
        taskDAO.delete(id);
    }

    @Override
    public List<Task> list(String userId) {
        return taskDAO.list(userId);
    }

    @Override
    public int save(Task task) {
        
        if(!isFreeOrder(task)) {
            throw new RuntimeException("Order of the task to save is already in use:" + task);
        }
        
        return taskDAO.save(task);
    }

    private boolean isFreeOrder(Task task) {
        return Objects.equals(
                jdbcTemplate.queryForObject(
                        "select count(*) from TASK where taskorder = ? and USER_ID = ?",
                        Integer.class,
                        task.getOrder(),
                        task.getUserId()),
                0);
    }
}
