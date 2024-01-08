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

    @GetMapping("/{id}")
    public String getStudent(@PathVariable Integer id){
        return repository.findById(id).isPresent()
                ? repository.findById(id).get().toString()
                : String.format("Student with id: %s doesn't exist in the db", id);
    }

    @PostMapping
    public String createStudent(@RequestBody Student newStudent) {
        if(newStudent.getId() == null) {
            return "Provided input doesn't contain id, please add id to your request json";
        }
        return repository.save(newStudent).toString();
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Integer id){
      if (repository.findById(id).isEmpty()){
          return String.format("Student with id: %s doesn't exist in the db", id);
      }
      repository.deleteById(id);
      return String.format("Student with id: %s has been successfully deleted", id);
    }
}
