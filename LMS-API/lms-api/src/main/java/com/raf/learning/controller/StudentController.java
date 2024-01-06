package com.raf.learning.controller;

import com.raf.learning.model.Student;
import com.raf.learning.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Student> getStudents(){
        return repository.findAll();
    }

    @PostMapping
    public Student createStudent(@RequestBody Student newStudent) {
        return repository.save(newStudent);
    }

}
