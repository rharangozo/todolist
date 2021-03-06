package rh.persistence.service;

import java.util.List;
import java.util.Set;
import rh.domain.Tag;
import rh.domain.Task;

public interface TagService {

    /**
     * Loads the tags to the items in the list!
     * @param tasks
     */
    void loadTagsFor(List<Task> tasks);
    
    void removeTagsOf(Task task);
    
    void removeTagsOf(int id);
    
    void persist(Set<Tag> tags, Task task);
    
    void persist(Set<Tag> tags, int taskId);

    public Set<Tag> getTagsOf(Task task);
}
