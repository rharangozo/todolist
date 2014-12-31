package rh.persistence.service;

import rh.domain.Task;

public interface OrderDistManager {

    Integer getOrderForHead(String userId);
    
    Integer getOrderAfter(Task task);
    
    void normalization(String userId);
}
