package rh.domain;

import java.util.Set;

public interface ITask {

    String getDescription();

    int getId();

    Set<Tag> getTags();
    
    String getUserId();
}
