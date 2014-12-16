package rh.persistence;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rh.domain.TaskEntity;

@Repository
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDAO tagDAO;

    @Override
    public void loadTagsFor(List<TaskEntity> tasks) {

        if(tasks == null || tasks.isEmpty()) {
            return;
        }

        tasks.forEach(task -> {
            task.setTags(tagDAO.listTagsFor(task));
        });
    }
}
