package com.raf.learning.controller;

import com.raf.learning.model.CreateDirectoryRequest;
import com.raf.learning.service.LocalDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/directories")
public class DirectoryController {
    private final LocalDirectoryService directoryService;

    @Autowired
    public DirectoryController(LocalDirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    /**
     * Creates a directory based on the input provided in the request.
     * The directory structure will follow: "C:\Projects\<subject>\<year>\<testType>"
     *
     * @param request The request object containing subject, year, and testType.
     *                - subject: The subject name or identifier (String).
     *                - year: The year for which the directory is being created (String, e.g., "2024").
     *                - testType: The type of test (String, e.g., "Midterm" or "Final").
     * @return A ResponseEntity containing the path of the created directory or an error message.
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createDirectory(@RequestBody CreateDirectoryRequest request) {

        try {
            // Build platform-agnostic path using Paths.get
            Path baseDirectoryPath = Paths.get(
                    "/srv/git",
                    request.getSubject().replace("/", "_"), // Sanitize forward slashes
                    request.getYear().replace("/", "_"),    // Replace invalid chars
                    request.getTestType().replace("/", "_")
            ).toAbsolutePath();

            // Add the hardcoded subdirectory to the base directory
            Path fullDirectoryPath = baseDirectoryPath.resolve("Studentska_resenja");

            directoryService.createDirectory(fullDirectoryPath.toString());

            // Return structured JSON response
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("basePath", baseDirectoryPath.toString());
            response.put("fullPath", fullDirectoryPath.toString());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "failure");
            errorResponse.put("error", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
