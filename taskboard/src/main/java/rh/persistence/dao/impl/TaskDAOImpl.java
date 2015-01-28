package rh.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;

@Repository
public class TaskDAOImpl implements TaskDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Task get(int id) {
        return jdbcTemplate.queryForObject("Select * from TASK where id = ?", 
                new TaskEntityRowMapper(), id);
    }

    @Override
    public Task getHead(String userId) {
        Task task;
        try {
            task = jdbcTemplate.queryForObject(
                "select * from TASK where user_id = ? AND TASKORDER = "
                        + "(select min(taskorder) from TASK where user_id = ?)",
                new TaskEntityRowMapper(), userId, userId);
        
        } catch (EmptyResultDataAccessException exception) {
            
            task = Task.Special.NULL.getInstance();
        }
        return task;
    }
    
    @Override
    public void update(Task task) {

        jdbcTemplate.update("update TASK SET DESCRIPTION=?, TASKORDER = ?, COMPLETE=? "
                + "WHERE ID=?",
                task.getDescription(),
                task.getOrder(),
                task.isComplete(),
                task.getId());
    }

    @Override
    public int save(Task task) {

        //TODO 2: validate taskEntity!
        
        jdbcTemplate.update("insert into TASK(DESCRIPTION, TASKORDER, USER_ID, COMPLETE) "
                + "values(?, ?, ?, ?)",
                task.getDescription(),
                task.getOrder(),
                task.getUserId(),
                task.isComplete());

        //TODO 2: IT WORKS FOR HSQLDB BUT DOES NOT FOR ANY OTHER DB!!!!
        return jdbcTemplate.queryForObject("select TOP 1 ID from TASK ORDER BY ID DESC", Integer.class);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM TASK WHERE ID = ?", id);
    }

    @Override
    public List<Task> list(String userId) {
        if(userId == null) {
            throw new NullPointerException("userId parameter must be not null!");
        }

        return jdbcTemplate.query("select * from TASK "
                + "WHERE USER_ID = ? ORDER BY TASKORDER ASC",
                new TaskEntityRowMapper(),
                userId);
    }

    @Override
    public Task next(Task task) {
        //TODO 2 : validate task
        Task nextTask;
        try {
            nextTask = jdbcTemplate.queryForObject(
                    "select TOP 1 * from TASK where TASKORDER > ? and USER_ID = ? ORDER BY TASKORDER ASC",
                    new Object[]{task.getOrder(), task.getUserId()},
                    new TaskEntityRowMapper());

        } catch (EmptyResultDataAccessException exception) {

            nextTask = Task.Special.NULL.getInstance();
        }
        return nextTask;
        
    }
    
    @Override
    public boolean isAllocatable(Task task) {
        return Objects.equals(
                jdbcTemplate.queryForObject(
                        "select count(*) from TASK where taskorder = ? and USER_ID = ?",
                        Integer.class,
                        task.getOrder(),
                        task.getUserId()),
                0);
    }

    private static class TaskEntityRowMapper implements RowMapper<Task> {

        @Override
        public Task mapRow(ResultSet rs, int i) throws SQLException {
            Task task = new Task();
            task.setId(rs.getInt("ID"));
            task.setDescription(rs.getString("DESCRIPTION"));
            task.setOrder(rs.getInt("TASKORDER"));
            task.setUserId(rs.getString("USER_ID"));
            task.setComplete(rs.getBoolean("COMPLETE"));
            return task;
        }

    }
}
