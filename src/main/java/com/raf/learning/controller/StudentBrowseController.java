package com.raf.learning.controller;

import com.raf.learning.model.Subject;
import com.raf.learning.model.TestGroupInfo;
import com.raf.learning.service.StudentBrowseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/browse")
public class StudentBrowseController {

    @Autowired
    private StudentBrowseService browseService;

    /**
     * Retrieves all subjects that have available test repositories for students.
     * This endpoint returns only subjects that contain at least one test group,
     * ensuring students only see subjects with actual tests available.
     *
     * @return ResponseEntity containing a list of Subject entities with available tests.
     *         Each Subject includes:
     *         - id: The unique identifier of the subject
     *         - shortName: The abbreviated name (e.g., "OOP", "Math")
     *         - fullName: The complete subject name (e.g., "Object-Oriented Programming")
     *         - schoolYear: The academic year for this subject instance
     *
     * @apiNote This is the first step in the hierarchical browsing flow.
     *          Students select a subject before proceeding to years.
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getSubjects() {
        return ResponseEntity.ok(browseService.getAvailableSubjects());
    }

    /**
     * Retrieves all academic years that have test repositories for a specific subject.
     * Returns years in which the specified subject has test groups available.
     *
     * @param subjectShortName The short name identifier of the subject (e.g., "OOP", "Math").
     *                        Must match exactly with the shortName field in the Subject entity.
     * @return ResponseEntity containing a list of academic year strings.
     *         Years are returned in string format (e.g., "2024_25", "2023_24").
     *
     * @throws EntityNotFoundException if the specified subject does not exist
     *                                or has no available test repositories.
     *
     * @apiNote This is the second step in the browsing flow. Students select a year
     *          after choosing their subject.
     */
    @GetMapping("/subjects/{subjectShortName}/years")
    public ResponseEntity<List<String>> getYears(@PathVariable String subjectShortName) {
        return ResponseEntity.ok(browseService.getYearsForSubject(subjectShortName));
    }

    /**
     * Retrieves all test types available for a specific subject and academic year combination.
     * Test types represent different examination periods or assessment categories.
     *
     * @param subjectShortName The short name identifier of the subject (e.g., "OOP").
     * @param year The academic year in string format (e.g., "2024_25").
     * @return ResponseEntity containing a list of test type names.
     *         Common test types include: "Prvi_ispit", "Drugi_ispit", "Kolokvijum_1", etc.
     *
     * @throws EntityNotFoundException if the subject/year combination does not exist
     *                                or has no available test types.
     *
     * @apiNote This is the third step in the browsing flow. Students select a test type
     *          after choosing their subject and year.
     */
    @GetMapping("/subjects/{subjectShortName}/years/{year}/test-types")
    public ResponseEntity<List<String>> getTestTypes(
            @PathVariable String subjectShortName,
            @PathVariable String year) {
        return ResponseEntity.ok(browseService.getTestTypesForSubjectAndYear(subjectShortName, year));
    }

    /**
     * Retrieves all test groups available for a specific subject, year, and test type combination.
     * This is the final level in the browsing hierarchy and returns the actual test repositories
     * that students can clone and work with.
     *
     * @param subjectShortName The short name identifier of the subject (e.g., "OOP").
     * @param year The academic year in string format (e.g., "2024_25").
     * @param testType The test type name (e.g., "Prvi_ispit").
     * @return ResponseEntity containing a list of TestGroupInfo objects.
     *         Each TestGroupInfo includes:
     *         - testGroupId: Unique identifier for database operations
     *         - groupNumber: Display number for the group (e.g., "15")
     *         - gitPath: Full filesystem path to the Git repository
     *
     * @throws EntityNotFoundException if the subject/year/testType combination does not exist
     *                                or has no available test groups.
     *
     * @apiNote This is the final step in the browsing flow. Students select a group
     *          to clone the test repository and begin working.
     *
     * @see TestGroupInfo for the structure of returned group information
     */
    @GetMapping("/subjects/{subjectShortName}/years/{year}/test-types/{testType}/groups")
    public ResponseEntity<List<TestGroupInfo>> getGroups(
            @PathVariable String subjectShortName,
            @PathVariable String year,
            @PathVariable String testType) {
        return ResponseEntity.ok(browseService.getGroupsForTest(subjectShortName, year, testType));
    }
}
