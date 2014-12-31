package rh.persistence.service.impl;

import java.util.Iterator;
import java.util.Objects;
import org.springframework.stereotype.Service;
import rh.persistence.service.FreeOrderLookup;
import rh.persistence.service.NoFreeOrderException;

@Service
public class MedianOrderLookupStrategy implements FreeOrderLookup {

    @Override
    public Integer freeOrderBetween(Integer from, Integer to) {
        Integer median = Math.abs(from - to) / 2;
        if(Objects.equals(median, from) || Objects.equals(median, to)) {
            throw new NoFreeOrderException(
                    "Median : " + median + 
                    ", From: " + from +
                    ", To : " + to);
        }
        return median;
    }

    @Override
    public Iterator<Integer> orderReDistribution(Integer from, Integer to, int size) {
        return new Iterator<Integer>(){

            private final Integer step = Math.abs(to-from) / (size + 1);
            private int index = 1;
            
            @Override
            public boolean hasNext() {
                return index <= size;
            }

            @Override
            public Integer next() {
                return index++ * step;
            }  
        };
    }
}
