package rh.persistence.dao;

import java.util.List;
import rh.domain.Task;

public interface TaskDAO {

    public Task get(int id);

    public Task getHead(String userId);
    
    public void update(Task taskEntity);

    public void delete(int id);

    public List<Task> list(String userId);

    public int save(Task taskEntity);

    public Task next(Task task);
}
