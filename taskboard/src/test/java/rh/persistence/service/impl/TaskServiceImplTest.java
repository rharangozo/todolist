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

    @Test(expected = RuntimeException.class)
    public void checkOrderUniqueConstraint() {
        int id = -1;
        try {
            id = ts.save(taskMock);
            assertNotEquals("Id -1 is invalid!", id, -1);

            //Attempt to save a task with a used order value
            ts.save(taskMock);
        } finally {
            ts.deleteTaskBy(id);
        }
    }

    @Test
    public void checkDefectWithDragAndDrop() {
        int id1 = -1;
        int id2 = -1;
        try {
            //1. start app - list testuser's task
            //2. insert a new task and drag&drop between the two tasks        
            Task newTask1 = new Task();
            newTask1.setDescription("newTask1");
            newTask1.setUserId("testuser");
            id1 = ts.save(newTask1);
            
            newTask1 = ts.getTaskBy(id1);
            ts.insertAfter(newTask1, ts.getTaskBy(0));

            //3. complete the new task by clicking on the pipe
            newTask1.setComplete(true);
            ts.update(newTask1);

            //4. create a new task again and move to between them again - error 500!
            Task newTask2 = new Task();
            newTask2.setDescription("newTask2");
            newTask2.setUserId("testuser");
            
            id2 = ts.save(newTask2);
            
            newTask2 = ts.getTaskBy(id2);
            ts.insertAfter(newTask2, ts.getTaskBy(0));
        } finally {
            if (id1 != -1) {
                ts.deleteTaskBy(id1);
            }
            if (id2 != -1) {
                ts.deleteTaskBy(id2);
            }
        }
    }
}
