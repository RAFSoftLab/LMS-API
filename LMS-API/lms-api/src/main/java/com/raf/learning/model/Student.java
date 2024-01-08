package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Student {
    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String startYear;
    private String studyProgram;
    private Integer indexNumber;

    public Student() {
    }

    public Student(Integer id,
                   String firstName,
                   String lastName,
                   String startYear,
                   String studyProgram,
                   Integer indexNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.startYear = startYear;
        this.studyProgram = studyProgram;
        this.indexNumber = indexNumber;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStartYear() {
        return startYear;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public Integer getIndexNumber() {
        return indexNumber;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public void setStudyProgram(String studyProgram) {
        this.studyProgram = studyProgram;
    }

    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"id\" :");
        builder.append(id);
        builder.append(", \"firstName\" :");
        builder.append("\"").append(firstName).append("\"");
        builder.append(", \"lastName\" :");
        builder.append("\"").append(lastName).append("\"");
        builder.append(", \"startYear\" :");
        builder.append("\"").append(startYear).append("\"");
        builder.append(", \"studyProgram\" :");
        builder.append("\"").append(studyProgram).append("\"");
        builder.append(", \"indexNumber\" :");
        builder.append("\"").append(indexNumber).append("\"");
        builder.append("}");
        return builder.toString();
    }

}


