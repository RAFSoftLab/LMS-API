package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class StudentSubmission {

    @Id
    private Long id;

    @ManyToOne
    private StudentInfo student;

    @ManyToOne
    private Assignment assignment;

    private String forkPath; // mora da se napravi pre clone-a, moze i profesor

    private boolean cloned;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
