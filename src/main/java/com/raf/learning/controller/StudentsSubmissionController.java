package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.StudentSubmission;
import com.raf.learning.repository.StudentsSubmissionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studentsSubmission")
public class StudentsSubmissionController {
    private final StudentsSubmissionRepository studentsSubmissionRepository;

    public StudentsSubmissionController(StudentsSubmissionRepository studentsSubmissionRepository) {
        this.studentsSubmissionRepository = studentsSubmissionRepository;
    }

    @GetMapping
    public List<StudentSubmission> getStudentsSubmission() {
        return studentsSubmissionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getStudentSubmission(@PathVariable String id) {
        var result = studentsSubmissionRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Student submission with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var test = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(test)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createStudentSubmission(@RequestBody StudentSubmission newStudentSubmission){
        var studentSubmission = studentsSubmissionRepository.save(newStudentSubmission);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(studentSubmission)
        ));
    }
}
