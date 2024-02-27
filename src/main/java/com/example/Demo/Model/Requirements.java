package com.example.Demo.Model;

import com.example.Demo.Enum.EnumClass;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "requirements")
public class Requirements {

    @Id
    private String id;
    private EnumClass.Need need;
    private String description;
    private String priority;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Requirements() {
        super();
    }

    public Requirements(EnumClass.Need need, String priority) {
        super();
        this.need = need;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Requirement [need=" + need + ", priority=" + priority + "]";
    }

    public EnumClass.Need getNeed() {
        return need;
    }

    public void setNeed(EnumClass.Need need) {
        this.need = need;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
