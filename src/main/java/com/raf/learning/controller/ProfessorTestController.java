package com.raf.learning.controller;

import com.raf.learning.model.TestGroup;
import com.raf.learning.repository.SubjectsRepository;
import com.raf.learning.repository.TestGroupRepository;
import com.raf.learning.repository.TestTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professor/tests")
public class ProfessorTestController {

    private final SubjectsRepository subjectsRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestGroupRepository testGroupRepository;

    public ProfessorTestController(SubjectsRepository subjectsRepository, TestTypeRepository testTypeRepository, TestGroupRepository testGroupRepository) {
        this.subjectsRepository = subjectsRepository;
        this.testTypeRepository = testTypeRepository;
        this.testGroupRepository = testGroupRepository;
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<String>> getAllSubjects() {
        List<String> subjects = subjectsRepository.findAllShortNames();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/subjects/{subject}/years")
    public ResponseEntity<List<String>> getYearsForSubject(@PathVariable String subject) {
        List<String> years = testTypeRepository.findSchoolYearsBySubjectShortName(subject);
        return ResponseEntity.ok(years);
    }

    @GetMapping("/subjects/{subject}/years/{year}/types")
    public ResponseEntity<List<String>> getTestTypes(
            @PathVariable String subject,
            @PathVariable String year) {
        List<String> types = testTypeRepository.findTypesBySubjectAndYear(subject, year);
        return ResponseEntity.ok(types);
    }

    @GetMapping("/subjects/{subject}/years/{year}/types/{type}/groups")
    public ResponseEntity<List<TestGroup>> getGroups(
            @PathVariable String subject,
            @PathVariable String year,
            @PathVariable String type) {
        List<TestGroup> groups = testGroupRepository.findBySubjectAndYearAndType(subject, year, type);
        return ResponseEntity.ok(groups);
    }
}
