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

    // Get subjects that actually have test groups (for student browsing)
    @Query("SELECT DISTINCT s FROM Subject s WHERE s.id IN " +
            "(SELECT DISTINCT t.subjectId FROM TestType t WHERE t.id IN " +
            "(SELECT DISTINCT g.testTypeId FROM TestGroup g))")
    List<Subject> findSubjectsWithAvailableTests();
}
