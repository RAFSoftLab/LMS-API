package com.raf.learning.repository;

import com.raf.learning.model.TestType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTypeRepository extends ListCrudRepository<TestType, Long> {
    @Query("SELECT DISTINCT t.schoolYear FROM TestType t WHERE t.subjectId = " +
            "(SELECT s.id FROM Subject s WHERE s.shortName = :subject)")
    List<String> findSchoolYearsBySubjectShortName(String subject);

    @Query("SELECT DISTINCT t.name FROM TestType t WHERE t.subjectId = " +
            "(SELECT s.id FROM Subject s WHERE s.shortName = :subject) " +
            "AND t.schoolYear = :year")
    List<String> findTypesBySubjectAndYear(String subject, String year);
}

