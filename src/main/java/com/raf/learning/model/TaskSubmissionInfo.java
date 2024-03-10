package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TaskSubmissionInfo {

    @Id
    private String id;

    private String forkName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getForkName() {
        return forkName;
    }

    public void setForkName(String forkName) {
        this.forkName = forkName;
    }
}
