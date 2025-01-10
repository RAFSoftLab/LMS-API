package com.raf.learning.repository;

import com.raf.learning.model.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubjectsRepository extends ListCrudRepository<Subject, Long> {

    @Query("SELECT s.shortName FROM Subject s")
    List<String> findAllShortNames();

    Optional<Subject> findByShortName(String shortName);
}
