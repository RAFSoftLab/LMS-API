package com.raf.learning.model;

import jakarta.persistence.ManyToOne;

//@Entity
public class Assignment {

    @ManyToOne
    private TestPeriod testPeriod; // termin u kom se radi zadatak 9-12 na primer

    private String groupLabel; // grupa1, grupa2 i sl

    private String gitRepoPath; // treba u nekom conf fajlu (application properties mozda) da se zapise root git folder a ovde relativna putanja

    @ManyToOne
    private Test test;

    private int maxPoints;

    public TestPeriod getTestPeriod() {
        return testPeriod;
    }

    public void setTestPeriod(TestPeriod testPeriod) {
        this.testPeriod = testPeriod;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
    }

    public String getGitRepoPath() {
        return gitRepoPath;
    }

    public void setGitRepoPath(String gitRepoPath) {
        this.gitRepoPath = gitRepoPath;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
