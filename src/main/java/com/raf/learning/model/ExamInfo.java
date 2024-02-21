package com.raf.learning.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ExamInfo {

    @Id
    private Long id;

    private String taskGroup;
    private String classroom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}
