package com.raf.learning.controller;

import com.raf.learning.Config;
import com.raf.learning.model.*;
import com.raf.learning.repository.SubjectsRepository;
import com.raf.learning.repository.TestGroupRepository;
import com.raf.learning.repository.TestTypeRepository;
import com.raf.learning.service.LocalDirectoryService;
import jakarta.persistence.EntityNotFoundException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/directories")
public class DirectoryController {
    private final LocalDirectoryService directoryService;
    private final SubjectsRepository subjectsRepository;
    private final TestTypeRepository testTypeRepository;
    private final TestGroupRepository testGroupRepository;

    @Autowired
    public DirectoryController(LocalDirectoryService directoryService, SubjectsRepository subjectsRepository, TestTypeRepository testTypeRepository, TestGroupRepository testGroupRepository) {
        this.directoryService = directoryService;
        this.subjectsRepository = subjectsRepository;
        this.testTypeRepository = testTypeRepository;
        this.testGroupRepository = testGroupRepository;
    }

    /**
     * Creates a directory based on the input provided in the request and initializes it as a Git repository.
     * The directory structure will follow: "/srv/git/<subject>/<year>/<testType>/<group>/Studentska_resenja".
     * The created directory will be set up as a Git repository, allowing files to be pushed to it remotely.
     *
     * @param request The request object containing the following fields:
     *                - subject: The subject name or identifier (String, e.g., "Math").
     *                - year: The year for which the directory is being created (String, e.g., "2024").
     *                - testType: The type of test (String, e.g., "Midterm" or "Final").
     *                - group: The group name or identifier (String, e.g., "Group_A").
     * @return A ResponseEntity containing:
     *         - "status": The status of the operation ("success" or "failure").
     *         - "basePath": The absolute path of the created base directory.
     *         - "fullPath": The absolute path of the created directory, including the "Studentska_resenja" subdirectory.
     *         - "gitInitialized": A boolean value ("true") indicating whether the Git repository was successfully initialized.
     *         - In case of failure, an "error" field containing the error message.
     *
     * @throws IOException If an error occurs during directory creation, Git initialization, or permission setup.
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createDirectory(@RequestBody CreateDirectoryRequest request) {

        try {
            // Build platform-agnostic path using Paths.get
            Path baseDirectoryPath = Paths.get(
                    "/srv/git",
                    request.getSubject().replace("/", "_"), // Sanitize forward slashes
                    request.getYear().replace("/", "_"),    // Replace invalid chars
                    request.getTestType().replace("/", "_"),
                    request.getGroup().replace("/", "_")
            ).toAbsolutePath();

            // Add the hardcoded subdirectory to the base directory
            Path fullDirectoryPath = baseDirectoryPath.resolve("Studentska_resenja");

            directoryService.createDirectory(fullDirectoryPath.toString());

            System.out.println("Attempting to Initialize Git repository at: " + baseDirectoryPath);
            // Initialize Git repository
            try {
                // Initialize as bare repository
                Git.init()
                        .setBare(true)
                        .setDirectory(baseDirectoryPath.toFile())
                        .call();

                // Create Studentska_resenja after git init
                directoryService.createDirectory(fullDirectoryPath.toString());

                // Verify directory exists before running commands
                if (!baseDirectoryPath.toFile().exists()) {
                    throw new IOException("Base directory was not created properly at: " + baseDirectoryPath);
                }

                System.out.println("Successfully initialized Git repository at: " + baseDirectoryPath);

            } catch (GitAPIException e) {
                throw new IOException("Failed to initialize Git repository: " + e.getMessage(), e);
            }
            SetupPermissionsRequest setupPermissionsRequest = new SetupPermissionsRequest();
            setupPermissionsRequest.setRepoPath(baseDirectoryPath.toString());
            setupPermissions(setupPermissionsRequest);

            // Save the information to database
            Subject subject = subjectsRepository.findByShortName(request.getSubject())
                    .orElseThrow(() -> new EntityNotFoundException("Subject not found"));

            TestType testType = new TestType();
            testType.setName(request.getTestType());
            testType.setSubjectId(subject.getId());  // Set subject ID instead of Subject object
            testType.setSchoolYear(request.getYear());
            testType = testTypeRepository.save(testType);

            TestGroup testGroup = new TestGroup();
            testGroup.setGroupNumber(request.getGroup());
            testGroup.setTestTypeId(testType.getId());  // Set test type ID instead of TestType object
            testGroup.setGitPath(baseDirectoryPath.toString());
            testGroupRepository.save(testGroup);


            // Return structured JSON response
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("basePath", baseDirectoryPath.toString());
            response.put("fullPath", fullDirectoryPath.toString());
            response.put("gitInitialized", "true");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "failure");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/setup-permissions")
    public ResponseEntity<Map<String, String>> setupPermissions(@RequestBody SetupPermissionsRequest request) {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "sudo",
                    Config.GIT_PERMISSIONS_SCRIPT_PATH,
                    request.getRepoPath());

            Process process = pb.start();

            // Capture error output for debugging
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder error = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                error.append(line).append("\n");
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return ResponseEntity.ok(Map.of("status", "success"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of(
                                "status", "failure",
                                "error", "Script failed with exit code: " + exitCode + "\nError: " + error.toString()
                        ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "failure", "error", e.getMessage()));
        }
    }
}
