package com.raf.learning.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalDirectoryService {

    /**
     * Creates a directory at the given path.
     *
     * @param dirPath The path where the directory will be created.
     * @throws IOException If the directory creation fails.
     */
    public void createDirectory(String dirPath) throws IOException {
        Path path = Paths.get(dirPath).toAbsolutePath();
        System.out.println("Attempting to create directory at: " + path);
        if (Files.exists(path)) {
            System.out.println("Directory already exists: " + dirPath);
        } else {
            Files.createDirectories(path); // Ensures all parent directories are created if needed
            System.out.println("Directory created successfully: " + dirPath);
        }
    }

    public static void main(String[] args) {
        LocalDirectoryService service = new LocalDirectoryService();

        // Replace with your desired directory path
//        String directoryPath = "C:\\Projects\\TestDirectory";
        String directoryPath = "C:\\Projects\\OOP\\2024/25\\Prvi ispit";

        try {
            service.createDirectory(directoryPath);
        } catch (IOException e) {
            System.err.println("Failed to create directory: " + e.getMessage());
        }
    }
}
