package rh.persistence;

import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rh.Configuration;
import rh.categories.IntegrationTests;
import rh.domain.TaskEntity;

/**
 * Integration test to verify the integrity of Task DAO implementations
 *
 * @author Roland_Harangozo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configuration.class)
@Category(IntegrationTests.class)
public class TaskEntityDAOTest {

    private TaskEntity taskEntityMock = new TaskEntity();

    @Autowired
    private TaskEntityDAO taskEntityDAO;

    @Before
    public void initialize() {

        taskEntityMock.setDescription("Mock Task Entity");
        taskEntityMock.setId(1);
        taskEntityMock.setUserId("mock-user");
    }

    @Test
    public void saveAndCheckByListing() {

        List<TaskEntity> listWithoutNewTask = taskEntityDAO.list(taskEntityMock.getUserId());
        taskEntityDAO.save(taskEntityMock);
        List<TaskEntity> listWithNewTask = taskEntityDAO.list(taskEntityMock.getUserId());

        listWithNewTask.removeAll(listWithoutNewTask);
        TaskEntity retreivedTask = listWithNewTask.get(0);

        //Override the retrieved task ID field with the expected as it is irrelevant in this case
        retreivedTask.setId(taskEntityMock.getId());

        assertEquals(taskEntityMock, retreivedTask);
    }
}
