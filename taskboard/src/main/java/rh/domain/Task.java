package rh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.Set;

public class Task {

    private int id;
    private String description;
    private String userId;
    private Set<Tag> tags;
    private Integer order;
    private boolean complete;

    @JsonIgnore //Not expose the user id on the rest API as it is present in the URL
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //TODO 2: It should be a list ordered by names
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.userId, other.userId)) {
            return false;
        }
        if (!Objects.equals(this.tags, other.tags)) {
            return false;
        }
        if (!Objects.equals(this.order, other.order)) {
            return false;
        }
        if (this.complete != other.complete) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", description=" + description + 
                ", userId=" + userId + ", tags=" + tags + ", order=" + order + 
                ", complete=" + complete + '}';
    }    
    
    public static enum Special {

        NULL;

        private final Task nullTaskObj;

        {
            Task task = new Task();

            task.setDescription("");
            task.setId(-1);
            task.setOrder(Integer.MAX_VALUE);
            task.setTags(null);
            task.setUserId("");
            task.setComplete(false);

            nullTaskObj = task;
        }

        public Task getInstance() {
            return nullTaskObj;
        }
    }
}
