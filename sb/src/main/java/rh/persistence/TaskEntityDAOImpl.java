package rh.persistence;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import rh.domain.TaskEntity;

@Component
public class TaskEntityDAOImpl implements TaskEntityDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TaskEntity get(int id) {

        //TODO: remove this 'mock' data
        TaskEntity te = new TaskEntity();
        te.setDescription("Hello World Id: " + id);
        te.setId(id);

        return te;
    }

    @Override
    public void saveOrUpdate(TaskEntity taskEntity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(TaskEntity taskEntity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<TaskEntity> list() {
        
        //TODO: remove mock
        TaskEntity e1 = new TaskEntity();
        e1.setDescription("Entity 1");
        e1.setId(1);

        TaskEntity e2 = new TaskEntity();
        e2.setDescription("Entity 2");
        e2.setId(2);

        return Arrays.asList(e1, e2);

    }
}
