package com.raf.learning.service;

import com.raf.learning.model.Subject;
import com.raf.learning.model.TestType;
import com.raf.learning.model.TestGroup;
import com.raf.learning.repository.SubjectsRepository;
import com.raf.learning.repository.TestTypeRepository;
import com.raf.learning.repository.TestGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TestDataSeeder {
    private static final Logger log = LoggerFactory.getLogger(TestDataSeeder.class);

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TestTypeRepository testTypeRepository;

    @Autowired
    private TestGroupRepository testGroupRepository;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedTestData();
    }

    private void seedTestData() {
        // Only seed if no subjects exist
        if (subjectsRepository.count() > 0) {
            log.info("Test data already exists, skipping seeding");
            return;
        }

        log.info("Seeding test data for student browsing...");

        // Create subjects
        Subject oop = new Subject();
        oop.setId(1L);
        oop.setFullName("Object-Oriented Programming");
        oop.setShortName("OOP");
        oop.setSchoolYear("2024/2025");
        subjectsRepository.save(oop);

        Subject math = new Subject();
        math.setId(2L);
        math.setFullName("Mathematics");
        math.setShortName("Math");
        math.setSchoolYear("2024/2025");
        subjectsRepository.save(math);

        Subject si = new Subject();
        si.setId(3L);
        si.setFullName("Software Engineering");
        si.setShortName("SI");
        si.setSchoolYear("2024/2025");
        subjectsRepository.save(si);

        // Create test types for OOP
        TestType oopFirstExam = new TestType();
        oopFirstExam.setName("Prvi_ispit");
        oopFirstExam.setSchoolYear("2024_25");
        oopFirstExam.setSubjectId(1L);
        oopFirstExam = testTypeRepository.save(oopFirstExam);

        TestType oopSecondExam = new TestType();
        oopSecondExam.setName("Drugi_ispit");
        oopSecondExam.setSchoolYear("2024_25");
        oopSecondExam.setSubjectId(1L);
        oopSecondExam = testTypeRepository.save(oopSecondExam);

        TestType oopKolokvijum = new TestType();
        oopKolokvijum.setName("Kolokvijum_1");
        oopKolokvijum.setSchoolYear("2024_25");
        oopKolokvijum.setSubjectId(1L);
        oopKolokvijum = testTypeRepository.save(oopKolokvijum);

        // Create test types for Math
        TestType mathFirstExam = new TestType();
        mathFirstExam.setName("Prvi_ispit");
        mathFirstExam.setSchoolYear("2024_25");
        mathFirstExam.setSubjectId(2L);
        mathFirstExam = testTypeRepository.save(mathFirstExam);

        // Add some previous year data for OOP
        TestType oopPreviousYear = new TestType();
        oopPreviousYear.setName("Prvi_ispit");
        oopPreviousYear.setSchoolYear("2023_24");
        oopPreviousYear.setSubjectId(1L);
        oopPreviousYear = testTypeRepository.save(oopPreviousYear);

        // Create test groups for OOP Prvi_ispit 2024_25
        createTestGroup("15", "/srv/git/OOP/2024_25/Prvi_ispit/15", oopFirstExam.getId());
        createTestGroup("16", "/srv/git/OOP/2024_25/Prvi_ispit/16", oopFirstExam.getId());
        createTestGroup("17", "/srv/git/OOP/2024_25/Prvi_ispit/17", oopFirstExam.getId());

        // Create test groups for OOP Drugi_ispit 2024_25
        createTestGroup("15", "/srv/git/OOP/2024_25/Drugi_ispit/15", oopSecondExam.getId());
        createTestGroup("16", "/srv/git/OOP/2024_25/Drugi_ispit/16", oopSecondExam.getId());

        // Create test groups for OOP Kolokvijum_1 2024_25
        createTestGroup("1", "/srv/git/OOP/2024_25/Kolokvijum_1/1", oopKolokvijum.getId());
        createTestGroup("2", "/srv/git/OOP/2024_25/Kolokvijum_1/2", oopKolokvijum.getId());

        // Create test groups for Math
        createTestGroup("1", "/srv/git/Math/2024_25/Prvi_ispit/1", mathFirstExam.getId());
        createTestGroup("2", "/srv/git/Math/2024_25/Prvi_ispit/2", mathFirstExam.getId());

        // Create test groups for previous year
        createTestGroup("10", "/srv/git/OOP/2023_24/Prvi_ispit/10", oopPreviousYear.getId());
        createTestGroup("11", "/srv/git/OOP/2023_24/Prvi_ispit/11", oopPreviousYear.getId());

        log.info("Test data seeding completed successfully!");
    }

    private void createTestGroup(String groupNumber, String gitPath, Long testTypeId) {
        TestGroup group = new TestGroup();
        group.setGroupNumber(groupNumber);
        group.setGitPath(gitPath);
        group.setTestTypeId(testTypeId);
        testGroupRepository.save(group);
    }
}
