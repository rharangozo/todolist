package rh.persistence;

import rh.persistence.dao.TaskDAO;
import java.util.List;
import javax.annotation.Resource;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rh.Configuration;
import rh.categories.IntegrationTests;
import rh.domain.Task;

/**
 * Integration test to verify the integrity of Task DAO implementations
 *
 * @author Roland_Harangozo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Configuration.class)
@Category(IntegrationTests.class)
public class TaskDAOTest {

    private final Task taskEntityMock = new Task();
    private final Task taskEntityMock2 = new Task();
    
    @Resource(name = "taskDAO")
    private TaskDAO taskEntityDAO;

    @Before
    public void initialize() {

        taskEntityMock.setDescription("Mock Task Entity");
        taskEntityMock.setId(111);
        taskEntityMock.setOrder(333);
        taskEntityMock.setUserId("mock-user");
        
        taskEntityMock.setDescription("Mock Task Entity - Second");
        taskEntityMock.setId(112);
        taskEntityMock.setOrder(334);
        taskEntityMock.setUserId("mock-user");

    }

    @Test
    public void saveAndCheckByListing() {

        List<Task> listWithoutNewTask = taskEntityDAO.list(taskEntityMock.getUserId());
        int id = taskEntityDAO.save(taskEntityMock);
        List<Task> listWithNewTask = taskEntityDAO.list(taskEntityMock.getUserId());

        listWithNewTask.removeAll(listWithoutNewTask);
        Task retreivedTask = listWithNewTask.get(0);

        //Override the retrieved task ID field with the expected as it is irrelevant in this case
        retreivedTask.setId(taskEntityMock.getId());

        assertEquals("The retrieved object does not idetical with the saved one",
                taskEntityMock, retreivedTask);
        
        taskEntityDAO.delete(id);
        
        List<Task> listAfterDelete = taskEntityDAO.list(taskEntityMock.getUserId());
        listAfterDelete.removeAll(listWithoutNewTask);
        
        assertTrue("Probably the removal does not work", listAfterDelete.isEmpty());
    }
    
    @Test(expected = RuntimeException.class)
    public void checkOrderUniqueConstraint() {
        int id = -1;
        try{
            id = taskEntityDAO.save(taskEntityMock);
            assertNotEquals("Id -1 is invalid!", id, -1);
            
            //Attempt to save a task with a used order value
            taskEntityDAO.save(taskEntityMock);
        }finally{
            taskEntityDAO.delete(id);
        }
    }

    @Test(expected = RuntimeException.class)
    public void checkOrderUniqueConstraintOnUpdate() {
        int id = -1;
        int id2 = -1;
        try{
            id = taskEntityDAO.save(taskEntityMock);
            assertNotEquals("Id -1 is invalid!", id, -1);
            
            id2 = taskEntityDAO.save(taskEntityMock2);
            assertNotEquals("Id -1 is invalid!", id2, -1);

            //Attempt to update a task with a used order value
            taskEntityMock.setOrder(taskEntityMock2.getOrder());
            taskEntityDAO.update(taskEntityMock);
        }finally{
            taskEntityDAO.delete(id);
            taskEntityDAO.delete(id2);
        }
    }
    
    @Test
    public void checkPersistingTasksWithSameOrder() {
        int id = -1;
        int id2 = -1;
        try {
            id = taskEntityDAO.save(taskEntityMock);
            assertNotEquals("Id -1 is invalid!", id, -1);
            
            taskEntityMock.setUserId("NEWUSER");
            

            id2 = taskEntityDAO.save(taskEntityMock);
            assertNotEquals("Id -1 is invalid!", id2, -1);
            
            assertNotEquals(id, id2);
        }finally{
            taskEntityDAO.delete(id);
            taskEntityDAO.delete(id2);
        }
    }
}
