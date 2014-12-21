package rh.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Objects;

public class Tag {

    private int Id;
    
    private String tagName;
    
    @JsonCreator
    public static Tag getInstance(String name) {
        Tag tag = new Tag();
        tag.setTagName(name);
        return tag;
    }
    
    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.Id;
        hash = 67 * hash + Objects.hashCode(this.tagName);
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
        final Tag other = (Tag) obj;
        if (this.Id != other.Id) {
            return false;
        }
        if (!Objects.equals(this.tagName, other.tagName)) {
            return false;
        }
        return true;
    }

    
}
