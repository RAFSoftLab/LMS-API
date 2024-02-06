package com.raf.learning.service;

import com.raf.learning.model.Student;
import com.raf.learning.repository.StudentRepository;
import com.raf.learning.helpers.CSVHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {
    private final StudentRepository repository;

    public CSVService(StudentRepository repository) {
        this.repository = repository;
    }

    public void saveStudents(MultipartFile file) {
        try {
            List<Student> tutorials = CSVHelper.csvToTutorials(file.getInputStream());
            repository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Student> getAllStudents() {
        return repository.findAll();
    }
}
