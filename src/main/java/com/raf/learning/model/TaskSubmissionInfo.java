package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TaskSubmissionInfo {

    @Id
    private Long id;

    private String forkName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getForkName() {
        return forkName;
    }

    public void setForkName(String forkName) {
        this.forkName = forkName;
    }
}
