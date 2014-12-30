package rh.persistence.service.impl;

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
            throw new NoFreeOrderException();
        }
        return median;
    }
    
}
