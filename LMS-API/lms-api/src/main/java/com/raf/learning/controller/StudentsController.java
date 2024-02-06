package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.ExamInfo;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.Student;
import com.raf.learning.model.TaskSubmissionInfo;
import com.raf.learning.repository.StudentRepository;
import com.raf.learning.helpers.CSVHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.raf.learning.service.CSVService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentsController {
    private final StudentRepository repository;
    private final CSVService fileService;

    public StudentsController(StudentRepository repository,
                              CSVService fileService) {
        this.repository = repository;
        this.fileService = fileService;
    }

    @GetMapping
    public List<Student> getStudents(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public String getStudent(@PathVariable String id){
        return repository.findById(id).isPresent()
                ? new Gson().toJson(repository.findById(id).get())
                : String.format("Student with id: %s doesn't exist in the db", id);
    }

    @PostMapping
    public String createStudent(@RequestBody Student newStudent) {
        newStudent.setId(
                newStudent.getStudyProgram()
                        +newStudent.getIndexNumber()
                        +newStudent.getStartYear());
        return new Gson().toJson(repository.save(newStudent));
    }

    @PostMapping("/{id}/task_cloned")
    public String taskIsCloned(@PathVariable String id, @RequestBody ExamInfo examInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            return String.format("Student with id: %s doesn't exist in the db", id);
        }
        var dbStudent = result.get();
        dbStudent.setTaskCloned(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        dbStudent.setTaskClonedTime(Timestamp.valueOf(ldt));
        dbStudent.setTaskGroup(examInfo.getTaskGroup());
        dbStudent.setClassroom(examInfo.getClassroom());

        return new Gson().toJson(repository.save(dbStudent));
    }

    @PostMapping("/{id}/task_submitted")
    public String taskIsSubmitted(@PathVariable String id, @RequestBody TaskSubmissionInfo taskSubmissionInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            return String.format("Student with id: %s doesn't exist in the db", id);
        }
        var dbStudent = result.get();
        dbStudent.setTaskSubmitted(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        dbStudent.setTaskSubmittedTime(Timestamp.valueOf(ldt));
        dbStudent.setForkName(taskSubmissionInfo.getForkName());

        return new Gson().toJson(repository.save(dbStudent));
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable String id){
      if (repository.findById(id).isEmpty()){
          return String.format("Student with id: %s doesn't exist in the db", id);
      }
      repository.deleteById(id);
      return String.format("Student with id: %s has been successfully deleted", id);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadStudents(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.saveStudents(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }
}
