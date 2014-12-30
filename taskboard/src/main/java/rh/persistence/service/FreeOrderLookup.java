package rh.persistence.service;

public interface FreeOrderLookup {

    final static Integer HEAD = 0;
    final static Integer TAIL = Integer.MAX_VALUE;
    
    Integer freeOrderBetween(Integer from, Integer to);
}
