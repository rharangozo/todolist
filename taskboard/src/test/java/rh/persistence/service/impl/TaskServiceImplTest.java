package rh.persistence.service.impl;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rh.Configuration;
import rh.categories.IntegrationTests;
import rh.domain.Task;
import rh.persistence.service.TaskService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configuration.class)
@Category(IntegrationTests.class)
public class TaskServiceImplTest {
    
    private final Task taskMock = new Task();
    
    @Autowired
    private TaskService ts;
    
    @Before
    public void initialize() {

        taskMock.setDescription("Mock Task Entity");
        taskMock.setId(211);
        taskMock.setOrder(433);
        taskMock.setUserId("TaskServiceImplIT");
    }
    
    @Test
    public void testSavingVeryFirstTask() {
        int id = -1;
        try {
            taskMock.setUserId("NONEXISTINGUSER");
            id = ts.save(taskMock);
            assertNotEquals("Id -1 is invalid!", id, -1);
        } finally {
            ts.deleteTaskBy(id);
        }
    }
    
}
