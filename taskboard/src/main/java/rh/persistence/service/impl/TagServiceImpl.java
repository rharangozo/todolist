package rh.persistence.service.impl;

import rh.persistence.service.TagService;
import rh.persistence.dao.TagDAO;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rh.domain.Tag;
import rh.domain.Task;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDAO tagDAO;

    @Override
    public void loadTagsFor(List<Task> tasks) {

        if(tasks == null || tasks.isEmpty()) {
            return;
        }

        tasks.forEach(task -> {
            task.setTags(tagDAO.listTagsFor(task));
        });
    }

    @Override
    public void removeTagsOf(int id) {
        tagDAO.removeTagsWhereTaskIdIs(id);
    }

    @Override
    public void removeTagsOf(Task task) {
        removeTagsOf(task.getId());
    }

    @Override
    public void persist(Set<Tag> tags, Task task) {
        if(tags == null || tags.isEmpty()) {
            return;
        }
        persist(tags, task.getId());
    }

    @Override
    public void persist(Set<Tag> tags, int taskId) {
        if(tags == null || tags.isEmpty()) {
            return;
        }
        tags.forEach(tag -> tagDAO.save(tag, taskId));
    }
}
