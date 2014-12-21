package rh.persistence.dao.impl;

import rh.persistence.dao.TagDAO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import rh.domain.Tag;
import rh.domain.Task;

@Repository
public class TagDAOImpl implements TagDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public Set<Tag> listTagsFor(Task taskEntity) {
        if(taskEntity == null) {
            return Collections.emptySet();
        }
        
        //TODO: not so good that it needs to be converted
        List<Tag> tags = jdbcTemplate.query("SELECT * FROM TAG WHERE FK_TASK_ID = ?",
                new Object[]{taskEntity.getId()}, 
                new TagMapper());
        
        return new HashSet<>(tags);
    }

    @Override
    public void removeTagsWhereTaskIdIs(int taskId) {
        jdbcTemplate.update("DELETE FROM TAG WHERE FK_TASK_ID = ?", taskId);
    }

    @Override
    public void save(Tag tag, int taskId) {
        jdbcTemplate.update("INSERT INTO TAG(TAG_NAME, FK_TASK_ID) VALUES (?, ?)",
                tag.getTagName(), taskId);
    }

    private static class TagMapper implements RowMapper<Tag>{

        @Override
        public Tag mapRow(ResultSet rs, int i) throws SQLException {
            Tag tag = new Tag();
            tag.setTagName(rs.getString("TAG_NAME"));
            tag.setId(rs.getInt("ID"));
            return tag;
        }
    }
    
}
