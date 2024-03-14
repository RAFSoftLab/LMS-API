package com.raf.learning.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Subject {

    @Id
    private Long id;

    private String fullName; // Objektno orijentisano programiranje

    private String shortName; // OOP

    // TODO dodati ESPB, nastavnike, asistente, studente, ali vezano za skolsku godinu...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
