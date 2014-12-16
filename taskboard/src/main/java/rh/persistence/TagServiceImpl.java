package rh.persistence;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rh.domain.TaskEntity;

@Repository
public class TagServiceImpl implements TagService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void loadTagsFor(List<TaskEntity> tasks) {

        if(tasks == null || tasks.isEmpty()) {
            return;
        }

        tasks.stream()
                .mapToInt(i -> i.getId())
                .TODO(continue here);

        throw new UnsupportedOperationException("Not supported yet.");
    }

}
