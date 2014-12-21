package rh.web;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import rh.domain.Tag;

//TODO: write unit test to verify that it has the same getters than the class Task
@Component("taskPrototype")
public class TaskPrototype implements ServletContextAware {

    private Set<Tag> tagsPrototype;
    
    @PostConstruct
    public void init() {
        Tag tagPrototype = new Tag();
        tagPrototype.setTagName("${tagName}");
        Set<Tag> tags = new HashSet<>();
        tags.add(tagPrototype);
        tagsPrototype = Collections.unmodifiableSet(tags);
    }
    
    @Override
    public void setServletContext(ServletContext sc) {
        sc.setAttribute("taskPrototype", this);
    }
    
    public String getDescription() {
        return "${description}";
    }

    
    public String getId() {
        return "${id}";
    }

    public Set<Tag> getTags() {
        return tagsPrototype;
    }

    public String getUserId() {
        return "${userId}";
    }
}
