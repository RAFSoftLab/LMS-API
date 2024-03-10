package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class StudentInfo {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studyProgramShort; // RN, RI - iz indeksa
//    @ManyToMany
    @OneToMany
    private List<Test> assignedTests;

    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getStudyProgramShort() {
        return studyProgramShort;
    }

    public void setStudyProgramShort(String studyProgramShort) {
        this.studyProgramShort = studyProgramShort;
    }

//    public List<Test> getAssignedTests() {
//        return assignedTests;
//    }
//
//    public void setAssignedTests(List<Test> assignedTests) {
//        this.assignedTests = assignedTests;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
