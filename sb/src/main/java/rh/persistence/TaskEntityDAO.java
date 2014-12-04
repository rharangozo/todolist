package rh.persistence;

import java.util.List;
import rh.domain.TaskEntity;

public interface TaskEntityDAO {

    public TaskEntity get(int id);

    public void saveOrUpdate(TaskEntity taskEntity);

    public void delete(TaskEntity taskEntity);

    public List<TaskEntity> list();
}
