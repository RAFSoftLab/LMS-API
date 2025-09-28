package com.raf.learning.service;

import com.raf.learning.model.Subject;
import com.raf.learning.model.TestGroup;
import com.raf.learning.model.TestGroupInfo;
import com.raf.learning.repository.SubjectsRepository;
import com.raf.learning.repository.TestGroupRepository;
import com.raf.learning.repository.TestTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentBrowseService {

    @Autowired
    private SubjectsRepository subjectsRepository;

    @Autowired
    private TestTypeRepository testTypeRepository;

    @Autowired
    private TestGroupRepository testGroupRepository;

    /**
     * Retrieves all subjects that have at least one available test group.
     * This method ensures that students only see subjects with actual test repositories
     * they can work with, filtering out subjects that may exist but have no current tests.
     *
     * @return List of Subject entities that contain available test repositories.
     *         Empty list if no subjects have available tests.
     *
     * @implNote Uses a join query to only return subjects that have associated test groups,
     *           ensuring efficient database access and accurate results.
     */
    public List<Subject> getAvailableSubjects() {
        return subjectsRepository.findSubjectsWithAvailableTests();
    }

    /**
     * Retrieves all academic years that have test repositories for the specified subject.
     * Academic years are returned in their database format (e.g., "2024_25").
     *
     * @param subjectShortName The short name of the subject to query (e.g., "OOP", "Math").
     *                        Must match exactly with existing subject short names.
     * @return List of academic year strings for the specified subject.
     *         Empty list if the subject exists but has no test repositories.
     *
     * @throws IllegalArgumentException if subjectShortName is null or empty.
     *
     * @implNote Years are returned in the order they appear in the database.
     *           Consider adding sorting if chronological order is required.
     */
    public List<String> getYearsForSubject(String subjectShortName) {
        if (subjectShortName == null || subjectShortName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject short name cannot be null or empty");
        }
        return testTypeRepository.findSchoolYearsBySubjectShortName(subjectShortName);
    }

    /**
     * Retrieves all test types available for the specified subject and academic year.
     * Test types represent different examination periods or assessment categories
     * within an academic year for a specific subject.
     *
     * @param subjectShortName The short name of the subject (e.g., "OOP").
     * @param year The academic year in string format (e.g., "2024_25").
     * @return List of test type names for the specified subject and year.
     *         Common examples: "Prvi_ispit", "Drugi_ispit", "Kolokvijum_1".
     *         Empty list if the combination exists but has no test types.
     *
     * @throws IllegalArgumentException if any parameter is null or empty.
     *
     * @implNote Test types are returned in database order. Consider adding
     *           custom sorting if a specific order is required (e.g., chronological).
     */
    public List<String> getTestTypesForSubjectAndYear(String subjectShortName, String year) {
        if (subjectShortName == null || subjectShortName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject short name cannot be null or empty");
        }
        if (year == null || year.trim().isEmpty()) {
            throw new IllegalArgumentException("Year cannot be null or empty");
        }
        return testTypeRepository.findTypesBySubjectAndYear(subjectShortName, year);
    }

    /**
     * Retrieves all test groups for the specified subject, year, and test type combination.
     * This method returns the final level of the browsing hierarchy - the actual test
     * repositories that students can clone and work with.
     *
     * @param subjectShortName The short name of the subject (e.g., "OOP").
     * @param year The academic year in string format (e.g., "2024_25").
     * @param testType The test type name (e.g., "Prvi_ispit").
     * @return List of TestGroupInfo objects containing all information needed
     *         for students to clone and work with test repositories.
     *         Empty list if the combination exists but has no test groups.
     *
     * @throws IllegalArgumentException if any parameter is null or empty.
     *
     * @see TestGroupInfo for the structure of returned group information.
     *
     * @implNote Each TestGroupInfo contains the Git path needed for repository operations
     *           and the test group ID needed for database tracking.
     */
    public List<TestGroupInfo> getGroupsForTest(String subjectShortName, String year, String testType) {
        if (subjectShortName == null || subjectShortName.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject short name cannot be null or empty");
        }
        if (year == null || year.trim().isEmpty()) {
            throw new IllegalArgumentException("Year cannot be null or empty");
        }
        if (testType == null || testType.trim().isEmpty()) {
            throw new IllegalArgumentException("Test type cannot be null or empty");
        }

        List<TestGroup> groups = testGroupRepository.findBySubjectAndYearAndType(subjectShortName, year, testType);

        return groups.stream()
                .map(g -> new TestGroupInfo(g.getId(), g.getGroupNumber(), g.getGitPath()))
                .collect(Collectors.toList());
    }
}
