package rh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TaskEntity {

    private int id;
    private String description;
    private String userId;

    
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
}
