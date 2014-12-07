package rh.persistence;

import java.util.List;
import rh.domain.TaskEntity;

public interface TaskEntityDAO {

    public TaskEntity get(int id);

    public void update(TaskEntity taskEntity);

    public void delete(int id);

    public List<TaskEntity> list(String userId);

    public int save(TaskEntity taskEntity);
}
