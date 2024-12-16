package com.raf.learning.dto;

public class StudentDto {
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studyProgram;

    public StudentDto(String firstName,
                      String lastName,
                      Integer indexNumber,
                      String startYear,
                      String studyProgram) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studyProgram = studyProgram;
    }

}
