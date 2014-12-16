package rh.persistence;

import java.util.List;
import rh.domain.TaskEntity;

public interface TagService {

    /**
     * Loads the tags to the items in the list!
     * @param tasks
     */
    void loadTagsFor(List<TaskEntity> tasks);
}
