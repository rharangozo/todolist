package rh.persistence.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void update(Task taskEntity) {

        jdbcTemplate.update("update TASK SET DESCRIPTION=? WHERE ID=?",
                taskEntity.getDescription(),
                taskEntity.getId());
    }

    @Override
    public int save(Task taskEntity) {

        //TODO: validate taskEntity!
        
        jdbcTemplate.update("insert into TASK(DESCRIPTION, USER_ID) values(?, ?)",
                taskEntity.getDescription(),
                taskEntity.getUserId());

        //TODO
        //FIXME: IT WORKS FOR HSQLDB BUT DOES NOT FOR ANY OTHER DB!!!!
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

        return jdbcTemplate.query("select * from TASK WHERE USER_ID = ?",
                new TaskEntityRowMapper(),
                userId);
    }

    private static class TaskEntityRowMapper implements RowMapper<Task> {

        @Override
        public Task mapRow(ResultSet rs, int i) throws SQLException {
            Task task = new Task();
            task.setId(rs.getInt("ID"));
            task.setDescription(rs.getString("DESCRIPTION"));
            task.setUserId(rs.getString("USER_ID"));
            return task;
        }

    }
}
