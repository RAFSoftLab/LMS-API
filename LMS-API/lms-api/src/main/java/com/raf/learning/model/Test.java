package com.raf.learning.model;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;

//@Entity
public class Test {

    private LocalDate testDate;

    @OneToMany
    private List<Assignment> assignments;

    @ManyToOne
    private Subject subject;

    private String testType; // kolokvijum, ispit, domaci,...

    private Boolean active;

    // dodati entitet profesor

    @ManyToMany
    private List<StudentInfo> studentsAssigned;

    public LocalDate getTestDate() {
        return testDate;
    }

    public void setTestDate(LocalDate testDate) {
        this.testDate = testDate;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTestType() {
        return testType;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
