package rh.persistence.service;

import java.util.List;
import rh.domain.Task;

public interface TaskService {

    public Task getTaskBy(int id);

    public void deleteTaskBy(int id);

    public int save(Task taskEntity);

    public void update(Task taskEntity);

    public List<Task> listTasksWithTagsFor(String user);
    
    public int saveWithTags(Task task);
    
    public void deepUpdate(Task task);

    public void moveTop(Task task);

    public void insertAfter(Task task, Task after);
}
