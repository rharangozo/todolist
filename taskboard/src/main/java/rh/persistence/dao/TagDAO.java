package rh.persistence.dao;

import java.util.Set;
import rh.domain.Task;
import rh.domain.Tag;
        
public interface TagDAO {

    Set<Tag> listTagsFor(Task taskEntity);
    
    void removeTagsWhereTaskIdIs(int taskId);
}
