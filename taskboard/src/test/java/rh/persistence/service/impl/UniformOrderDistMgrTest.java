package rh.persistence.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import rh.domain.Task;
import rh.persistence.dao.TaskDAO;
import rh.persistence.service.NoFreeOrderException;

public class UniformOrderDistMgrTest {

    private UniformOrderDistMgr distMgr;

    @Before
    public void init() {
        distMgr = new UniformOrderDistMgr();
        distMgr.setLowLimit(2);
        distMgr.setHighLimit(6);
    }

    @Test
    public void simleTestGetOrderForHead() {
        List<Integer[]> params = Arrays.asList(
                //0 - head's order
                //1 - expected new head's order
                new Integer[]{3, 2},
                new Integer[]{2, 3},
                new Integer[]{6, 4});

        params.forEach(param -> {
            Task head = mock(Task.class);
            when(head.getOrder()).thenReturn(param[0]);

            TaskDAO taskDAO = mock(TaskDAO.class);
            when(taskDAO.getHead(anyString())).thenReturn(head);
            when(taskDAO.list(anyString())).thenReturn(Arrays.asList(head));

            distMgr.setTaskDAO(taskDAO);

            Integer ret = distMgr.getOrderForHead("somestring");
            assertEquals("Wrong order value for head - Params : " + Arrays.toString(param),
                    param[1], ret);
        });
    }
    
    @Test(expected = NoFreeOrderException.class)
    public void testWhenFull() {
        Task head = mock(Task.class);
        head.setOrder(2);
        
        List<Task> fullList = mock(List.class);
        when(fullList.size()).thenReturn(5);
        
        TaskDAO taskDAO = mock(TaskDAO.class);
        when(taskDAO.getHead(anyString())).thenReturn(head);
        when(taskDAO.list(anyString())).thenReturn(fullList);

        distMgr.setTaskDAO(taskDAO);
        
        distMgr.getOrderForHead("somestring");
        fail("Exception was not threw however no free order capacity");
    }

    @Test
    public void simleTestgetOrderAfter() {

        List<Task> tasks = new ArrayList();
        
        tasks.add(newTask(1, 2)); 
        Task after = newTask(2, 3);
        tasks.add(after); 
        Task next = newTask(3, 7);
        tasks.add(next);
        tasks.add(newTask(4, 20));
        
        
        TaskDAO taskDAO = mock(TaskDAO.class);
        when(taskDAO.list(anyString())).thenReturn(tasks);
        when(taskDAO.next(anyObject())).thenReturn(next);

        distMgr.setTaskDAO(taskDAO);
        
        Integer ret = distMgr.getOrderAfter(after);
        assertEquals(Integer.valueOf(5), ret);
    }
    
    @Test
    public void getOrderAfterWhenFull() {

        List<Task> tasks = new ArrayList();
        
        tasks.add(newTask(1, 2)); 
        Task after = newTask(2, 3);
        tasks.add(after); 
        Task next = newTask(3, 4);
        tasks.add(next);
        tasks.add(newTask(4, 6));
        
        
        TaskDAO taskDAO = mock(TaskDAO.class);
        when(taskDAO.list(anyString())).thenReturn(tasks);
        when(taskDAO.next(anyObject())).thenReturn(next);

        distMgr.setTaskDAO(taskDAO);
        
        Integer ret = distMgr.getOrderAfter(after);
        assertEquals(Integer.valueOf(4), ret);
    }

    @Test
    public void testNormalization() {
        List<Task> tasks = new ArrayList();
        
        tasks.add(newTask(1, 2));
        tasks.add(newTask(2, 6));
        
        TaskDAO taskDAO = mock(TaskDAO.class);
        when(taskDAO.list(anyString())).thenReturn(tasks);
        
        distMgr.setTaskDAO(taskDAO);
        
        distMgr.normalization("anystring");
        
        assertEquals(Integer.valueOf(3), tasks.get(0).getOrder());
        assertEquals(Integer.valueOf(5), tasks.get(1).getOrder());
    }
    
    @Test
    public void lookUpFreeOrderForFirstTask() {
        List<Task> tasks = new ArrayList();
        
        TaskDAO taskDAO = mock(TaskDAO.class);
        when(taskDAO.list(anyString())).thenReturn(tasks);
        when(taskDAO.getHead(anyString())).thenReturn(Task.Special.NULL.getInstance());
        
        distMgr.setTaskDAO(taskDAO);
        
        distMgr.normalization("anystring");
        assertEquals(Integer.valueOf(4), distMgr.getOrderForHead("anystring"));
    }
    
    private Task newTask(int id, int order) {
        Task task = new Task();
        task.setId(id);
        task.setOrder(order);
        return task;
    }

}
