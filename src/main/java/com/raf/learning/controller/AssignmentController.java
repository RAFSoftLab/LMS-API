package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.Assignment;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.repository.AssignmentsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
public class AssignmentController {
    private final AssignmentsRepository assignmentsRepository;

    public AssignmentController(AssignmentsRepository assignmentsRepository) {
        this.assignmentsRepository = assignmentsRepository;
    }

    @GetMapping
    public List<Assignment> getAssignment() {
        return assignmentsRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getAssignment(@PathVariable Long id) {
        var result = assignmentsRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Assignment with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var test = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(test)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createAssignment(@RequestBody Assignment newAssignment) {
        var assignment = assignmentsRepository.save(newAssignment);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(assignment)
        ));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteAssignment(@PathVariable Long id){
        if (assignmentsRepository.findById(id).isEmpty()){
            var message = String.format("Assignment with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        assignmentsRepository.deleteById(id);
        var message = String.format("Assignment with id: %s has been successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
}
