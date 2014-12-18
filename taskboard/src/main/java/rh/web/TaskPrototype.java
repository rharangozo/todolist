package rh.web;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import rh.domain.ITask;
import rh.domain.Tag;

TODO: finish this class and complete task.jsp - consider to refactor the Task and ITask class and interface

@Service
@Scope(WebApplicationContext.SCOPE_APPLICATION)
public class TaskPrototype implements ITask{

    public static final int ID_PLACEHOLDER = -1;
    private final Set<Tag> tagsPrototype;
    
    {
        Tag tagPrototype = new Tag();
        tagPrototype.setTagName("${tagName}");
        Set<Tag> tags = new HashSet<>();
        tags.add(tagPrototype);
        tagsPrototype = Collections.unmodifiableSet(tags);
    }
    
    @Override
    public String getDescription() {
        return "${description}";
    }

    @Override
    public int getId() {
        return TaskPrototype.ID_PLACEHOLDER;
    }

    @Override
    public Set<Tag> getTags() {
        return tagsPrototype;
    }

    @Override
    public String getUserId() {
        return "${userId}";
    }
}
