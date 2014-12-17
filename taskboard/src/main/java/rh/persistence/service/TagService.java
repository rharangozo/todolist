package rh.persistence.service;

import java.util.List;
import rh.domain.Task;

public interface TagService {

    /**
     * Loads the tags to the items in the list!
     * @param tasks
     */
    void loadTagsFor(List<Task> tasks);
}
