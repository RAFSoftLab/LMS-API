package com.raf.learning.dto;

import java.sql.Timestamp;

public class StudentDto {
    private String id;
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studiesGroup;
    private String taskGroup;
    private boolean taskCloned;
    private Timestamp taskClonedTime;
    private boolean taskSubmitted;
    private Timestamp taskSubmittedTime;
    private String studyProgram;
    private String classroom;

    public StudentDto(String id,
                      String firstName,
                      String lastName,
                      Integer indexNumber,
                      String startYear,
                      String studiesGroup,
                      String taskGroup,
                      boolean taskCloned,
                      Timestamp taskClonedTime,
                      boolean taskSubmitted,
                      Timestamp taskSubmittedTime,
                      String studyProgram,
                      String classroom) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studiesGroup = studiesGroup;
        this.taskGroup = taskGroup;
        this.taskCloned = taskCloned;
        this.taskClonedTime = taskClonedTime;
        this.taskSubmitted = taskSubmitted;
        this.taskSubmittedTime = taskSubmittedTime;
        this.studyProgram = studyProgram;
        this.classroom = classroom;
    }

}
