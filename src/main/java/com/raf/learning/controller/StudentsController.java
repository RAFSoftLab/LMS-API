package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.Application;
import com.raf.learning.authorisation.TokenManager;
import com.raf.learning.dto.StudentInfoDto;
import com.raf.learning.helpers.CSVHelper;
import com.raf.learning.model.*;
import com.raf.learning.repository.StudentSubmissionsRepository;
import com.raf.learning.repository.StudentsInfoRepository;
import com.raf.learning.repository.TaskSubmissionsInfoRepository;
import com.raf.learning.service.CSVService;
import com.raf.learning.service.GitRepositoriesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private final StudentsInfoRepository studentsInfoRepository;
    private final CSVService fileService;
    private final TokenManager tokenManager;
    private final GitRepositoriesService gitRepositoriesService;
    private final TaskSubmissionsInfoRepository taskSubmissionsInfoRepository;
    private final StudentSubmissionsRepository studentSubmissionsRepository;

    public StudentsController(StudentsInfoRepository repository,
                              CSVService fileService,
                              TokenManager tokenManager,
                              GitRepositoriesService gitRepositoriesService,
                              TaskSubmissionsInfoRepository taskSubmissionsInfoRepository,
                              StudentSubmissionsRepository studentSubmissionsRepository) {
        this.studentsInfoRepository = repository;
        this.fileService = fileService;
        this.tokenManager = tokenManager;
        this.gitRepositoriesService = gitRepositoriesService;
        this.taskSubmissionsInfoRepository = taskSubmissionsInfoRepository;
        this.studentSubmissionsRepository = studentSubmissionsRepository;
    }

    @GetMapping
    public List<StudentInfo> getStudents(){
        return studentsInfoRepository.findAll();
    }

    @PostMapping("/{id}/authorize")
    public ResponseEntity<ResponseMessage> authorizeStudent(@PathVariable String id) {
        var result = studentsInfoRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var student = result.get();
        if (student.getToken() != null) {
            var message = String.format("Student with id: %s is already authorized," +
                    " please use previously provided token", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var token = tokenManager.generateToken(id);
        student.setToken(token.getValue());

        var taskSubmissionInfo = new TaskSubmissionInfo();
        var forkName = student.getId() + UUID.randomUUID().toString();
        taskSubmissionInfo.setId(id);
        taskSubmissionInfo.setForkName(forkName);
        taskSubmissionsInfoRepository.save(taskSubmissionInfo);

        studentsInfoRepository.save(student);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(token)
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getStudent(@PathVariable String id){
        var result = studentsInfoRepository.findById(id);

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
    public ResponseEntity<ResponseMessage> createStudent(@RequestBody StudentInfo newStudent) {
        log.info("Create Student method entered");
        newStudent.setId(newStudent.getStudyProgramShort()
                        +newStudent.getIndexNumber()
                        +newStudent.getStartYear());
        log.info("Student id is: " + newStudent.getId());
        var student = studentsInfoRepository.save(newStudent);
        log.info("Student is saved in db");
        log.info("Returning response");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
               new Gson().toJson(student)
        ));
    }

    @GetMapping("/{id}/repository/{token}/exam/{exam}")
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

    @GetMapping("/{id}/repository/{token}/fork")
    public ResponseEntity<ResponseMessage> getFork(@PathVariable String id,
                                                     @PathVariable String token) {
        var tokenIsValid = tokenManager.verifyToken(id, token);
        if (!tokenIsValid) {
            var message = String.format("Invalid token: %s", token);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var result = taskSubmissionsInfoRepository.findById(id);
        if (result.isEmpty()) {
            var message = String.format("Fork for student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(result.get().getForkName())
        ));
    }

    @PostMapping("/{id}/task_cloned")
    public ResponseEntity<ResponseMessage> taskIsCloned(@PathVariable String id, @RequestBody ExamInfo examInfo) {
        var result = studentsInfoRepository.findById(id);
        if (result.isEmpty()){
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var dbStudent = result.get();
        var studentSubmission = new StudentSubmission();
        studentSubmission.setId(dbStudent.getId());
        studentSubmission.setCloned(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        studentSubmission.setTaskClonedTime(Timestamp.valueOf(ldt));

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(studentSubmission)
        ));
    }

    @PostMapping("/{id}/task_submitted")
    public ResponseEntity<ResponseMessage> taskIsSubmitted(
            @PathVariable String id,
            @RequestBody TaskSubmissionInfo taskSubmissionInfo) {
        var result = studentsInfoRepository.findById(id);
        if (result.isEmpty()){
            var message = String.format("Student with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var dbStudent = result.get();
        var studentSubmissionResult = studentSubmissionsRepository.findById(dbStudent.getId());
        if (studentSubmissionResult.isEmpty()){
            var message = String.format("Student with id: %s hasn't cloned the test, and can't submit it", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        var studentSubmission = studentSubmissionResult.get();
        studentSubmission.setTaskSubmitted(true);
        var ldt = LocalDateTime.now(ZoneId.of("CET"));
        studentSubmission.setTaskSubmittedTime(Timestamp.valueOf(ldt));
        studentSubmissionsRepository.save(studentSubmission);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(studentSubmission)
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteStudent(@PathVariable String id){
      if (studentsInfoRepository.findById(id).isEmpty()){
          var message = String.format("Student with id: %s doesn't exist in the db", id);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
      }
      studentsInfoRepository.deleteById(id);
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

    public StudentInfoDto mapStudentToDto(StudentInfo student) {
        return new StudentInfoDto(
                student.getFirstName(),
                student.getLastName(),
                student.getIndexNumber(),
                student.getStartYear(),
                student.getStudyProgramShort()
//                student.getAssignedTests()
                );
    }
}
