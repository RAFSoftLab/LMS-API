package com.raf.learning.dto;

public class StudentInfoDto {
    private String firstName;
    private String lastName;
    private Integer indexNumber;
    private String startYear;
    private String studyProgramShort; // RN, RI - iz indeksa
//    private List<Test> assignedTests;

    public StudentInfoDto(String firstName,
                          String lastName,
                          Integer indexNumber,
                          String startYear,
                          String studyProgramShort
//                          List<Test> assignedTests
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
        this.startYear = startYear;
        this.studyProgramShort = studyProgramShort;
//        this.assignedTests = assignedTests;
    }
}
