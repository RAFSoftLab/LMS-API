package com.raf.learning.controller;

import com.raf.learning.model.ExamInfo;
import com.raf.learning.model.Student;
import com.raf.learning.model.TaskSubmissionInfo;
import com.raf.learning.repository.StudentRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
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
    public String getStudent(@PathVariable String id){
        return repository.findById(id).isPresent()
                ? repository.findById(id).get().toString()
                : String.format("Student with id: %s doesn't exist in the db", id);
    }

    @PostMapping
    public String createStudent(@RequestBody Student newStudent) {
//        if(newStudent.getId() == null) {
//            return "Provided input doesn't contain id, please add id to your request json";
//        }
        newStudent.setId(
                newStudent.getStudyProgram()
                        +newStudent.getMajor()
                        +newStudent.getIndexNumber()
                        +newStudent.getStartYear());
        return repository.save(newStudent).toString();
    }

    @PostMapping("/{id}/task_cloned")
    public String taskIsCloned(@PathVariable String id, @RequestBody ExamInfo examInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            return String.format("Student with id: %s doesn't exist in the db", id);
        }
        var dbStudent = result.get();
        dbStudent.setTaskCloned(true);
        dbStudent.setTaskClonedTime(LocalDateTime.now(ZoneId.of("CET")));
        dbStudent.setTaskGroup(examInfo.getTaskGroup());
        dbStudent.setClassroom(examInfo.getClassroom());

        return repository.save(dbStudent).toString();
    }

    @PostMapping("/{id}/task_submitted")
    public String taskIsSubmitted(@PathVariable String id, @RequestBody TaskSubmissionInfo taskSubmissionInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            return String.format("Student with id: %s doesn't exist in the db", id);
        }
        var dbStudent = result.get();
        dbStudent.setTaskSubmitted(true);
        dbStudent.setTaskSubmittedTime(LocalDateTime.now(ZoneId.of("CET")));
        dbStudent.setForkName(taskSubmissionInfo.getForkName());

        return repository.save(dbStudent).toString();
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable String id){
      if (repository.findById(id).isEmpty()){
          return String.format("Student with id: %s doesn't exist in the db", id);
      }
      repository.deleteById(id);
      return String.format("Student with id: %s has been successfully deleted", id);
    }
}
