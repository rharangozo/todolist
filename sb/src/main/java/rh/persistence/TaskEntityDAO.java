package rh.persistence;

import java.util.List;
import rh.domain.TaskEntity;

public interface TaskEntityDAO {

    public TaskEntity get(int id);

    public void update(TaskEntity taskEntity);

    public void delete(TaskEntity taskEntity);

    public List<TaskEntity> list();

    public int save(TaskEntity taskEntity);
}
