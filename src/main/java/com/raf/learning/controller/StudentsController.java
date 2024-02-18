package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.authorisation.TokenManager;
import com.raf.learning.dto.StudentDto;
import com.raf.learning.helpers.CSVHelper;
import com.raf.learning.model.ExamInfo;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.Student;
import com.raf.learning.model.TaskSubmissionInfo;
import com.raf.learning.repository.StudentRepository;
import com.raf.learning.service.CSVService;
import com.raf.learning.service.GitRepositoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
public class StudentsController {
    private final StudentRepository repository;
    private final CSVService fileService;
    private final TokenManager tokenManager;
    private final GitRepositoriesService gitRepositoriesService;

    public StudentsController(StudentRepository repository,
                              CSVService fileService,
                              TokenManager tokenManager,
                              GitRepositoriesService gitRepositoriesService) {
        this.repository = repository;
        this.fileService = fileService;
        this.tokenManager = tokenManager;
        this.gitRepositoriesService = gitRepositoriesService;
    }

    @GetMapping
    public List<Student> getStudents(){
        return repository.findAll();
    }

    @PostMapping("/{id}/authorize")
    public ResponseEntity<ResponseMessage> authorizeStudent(@PathVariable String id) {
        var result = repository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var student = result.get();
        if (!student.getToken().isEmpty()) {
            var message = String.format("Student with id: %s is already authorized," +
                    " please use previously provided token", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var token = tokenManager.generateToken(id);
        student.setToken(token.getValue());

        var forkName = student.getId() + UUID.randomUUID().toString();
        student.setForkName(forkName);

        repository.save(student);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(mapStudentToDto(student))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getStudent(@PathVariable String id){
        var result = repository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var student = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(mapStudentToDto(student))
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createStudent(@RequestBody Student newStudent) {
        newStudent.setId(
                newStudent.getStudyProgram()
                        +newStudent.getIndexNumber()
                        +newStudent.getStartYear());
        var student = repository.save(newStudent);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(mapStudentToDto(student))
        ));
    }

    @GetMapping("/{id}/repository/{token}/{exam}")
    public ResponseEntity<ResponseMessage> getRepository(@PathVariable String id,
                                                         @PathVariable String token,
                                                         @PathVariable String exam) {
        var tokenIsValid = tokenManager.verifyToken(id, token);
        if (!tokenIsValid) {
            var message = String.format("Invalid token: %s", token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(gitRepositoriesService.getRepository(exam))
        ));
    }

    @GetMapping("/{id}/repository/{token}")
    public ResponseEntity<ResponseMessage> getFork(@PathVariable String id,
                                                     @PathVariable String token) {
        var tokenIsValid = tokenManager.verifyToken(id, token);
        if (!tokenIsValid) {
            var message = String.format("Invalid token: %s", token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var result = repository.findById(id);
        if (result.isEmpty()) {
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(result.get().getForkName())
        ));
    }


    @PostMapping("/{id}/task_cloned")
    public ResponseEntity<ResponseMessage> taskIsCloned(@PathVariable String id, @RequestBody ExamInfo examInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var dbStudent = result.get();
        dbStudent.setTaskCloned(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        dbStudent.setTaskClonedTime(Timestamp.valueOf(ldt));
        dbStudent.setTaskGroup(examInfo.getTaskGroup());
        dbStudent.setClassroom(examInfo.getClassroom());
        var student = repository.save(dbStudent);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(mapStudentToDto(student))
        ));
    }

    @PostMapping("/{id}/task_submitted")
    public ResponseEntity<ResponseMessage> taskIsSubmitted(@PathVariable String id, @RequestBody TaskSubmissionInfo taskSubmissionInfo) {
        var result = repository.findById(id);
        if (result.isEmpty()){
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var dbStudent = result.get();
        dbStudent.setTaskSubmitted(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        dbStudent.setTaskSubmittedTime(Timestamp.valueOf(ldt));
        dbStudent.setForkName(taskSubmissionInfo.getForkName());

        var student = repository.save(dbStudent);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(mapStudentToDto(student))
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteStudent(@PathVariable String id){
      if (repository.findById(id).isEmpty()){
          var message = String.format("Student with id: %s doesn't exist in the db", id);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
      }
      repository.deleteById(id);
        var message = String.format("Student with id: %s has been successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
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

    public StudentDto mapStudentToDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getIndexNumber(),
                student.getStartYear(),
                student.getStudiesGroup(),
                student.getTaskGroup(),
                student.isTaskCloned(),
                student.getTaskClonedTime(),
                student.isTaskSubmitted(),
                student.getTaskSubmittedTime(),
                student.getStudyProgram(),
                student.getClassroom());
    }
}
