package rh.persistence;

import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rh.Configuration;
import rh.categories.IntegrationTests;
import rh.domain.Tag;
import rh.domain.TaskEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configuration.class)
@Category(IntegrationTests.class)
public class TagDAOTest {

    private final TaskEntity taskEntity = new TaskEntity();
    
    @Autowired
    private TagDAO tagDAO;
    
    @Before
    public void initialize() {
        taskEntity.setId(1);
    }
    
    @Test
    public void testSimpleTagLoad() {
        
        Set<Tag> tags = tagDAO.listTagsFor(taskEntity);
        
        Assert.assertEquals(1, tags.size());
        Assert.assertEquals("testtag", tags.iterator().next().getTagName());
    }
}
