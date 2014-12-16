package rh.persistence;

import java.util.Set;
import rh.domain.TaskEntity;
import rh.domain.Tag;
        
public interface TagDAO {

    Set<Tag> listTagsFor(TaskEntity taskEntity);
}
