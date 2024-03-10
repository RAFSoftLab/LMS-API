package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;

@Entity
public class StudentSubmission {
    @Id
    private String id;

    @ManyToOne
    private StudentInfo student;

    @ManyToOne
    private Assignment assignment;

    private String forkPath; // mora da se napravi pre clone-a, moze i profesor

    private boolean cloned;

    private Timestamp taskClonedTime;

    private boolean taskSubmitted;
    private Timestamp taskSubmittedTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public StudentInfo getStudent() {
        return student;
    }

    public void setStudent(StudentInfo student) {
        this.student = student;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public String getForkPath() {
        return forkPath;
    }

    public void setForkPath(String forkPath) {
        this.forkPath = forkPath;
    }

    public boolean isCloned() {
        return cloned;
    }

    public void setCloned(boolean cloned) {
        this.cloned = cloned;
    }

    public Timestamp getTaskClonedTime() {
        return taskClonedTime;
    }

    public void setTaskClonedTime(Timestamp taskClonedTime) {
        this.taskClonedTime = taskClonedTime;
    }

    public boolean isTaskSubmitted() {
        return taskSubmitted;
    }

    public void setTaskSubmitted(boolean taskSubmitted) {
        this.taskSubmitted = taskSubmitted;
    }

    public Timestamp getTaskSubmittedTime() {
        return taskSubmittedTime;
    }

    public void setTaskSubmittedTime(Timestamp taskSubmittedTime) {
        this.taskSubmittedTime = taskSubmittedTime;
    }
}
