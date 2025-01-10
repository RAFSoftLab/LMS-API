package com.raf.learning.repository;

import com.raf.learning.model.TestGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface TestGroupRepository extends ListCrudRepository<TestGroup, Long> {
    @Query("SELECT g FROM TestGroup g WHERE g.testTypeId IN " +
            "(SELECT t.id FROM TestType t WHERE t.subjectId = " +
            "(SELECT s.id FROM Subject s WHERE s.shortName = :subject) " +
            "AND t.schoolYear = :year AND t.name = :type)")
    List<TestGroup> findBySubjectAndYearAndType(String subject, String year, String type);
}