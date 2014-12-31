package rh.persistence.service;

import java.util.Iterator;

public interface FreeOrderLookup {

    final static Integer HEAD = 0;
    final static Integer TAIL = Integer.MAX_VALUE;
    
    Integer freeOrderBetween(Integer from, Integer to);

    public Iterator<Integer> orderReDistribution(Integer from, Integer to, int size);
}
