package rh.persistence.service.impl;

import rh.persistence.service.TagService;
import rh.persistence.dao.TagDAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
