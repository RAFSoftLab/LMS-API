package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.Subject;
import com.raf.learning.repository.SubjectsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectsController {
    private final SubjectsRepository subjectsRepository;

    public SubjectsController(SubjectsRepository subjectsRepository) {
        this.subjectsRepository = subjectsRepository;
    }

    @GetMapping
    public List<Subject> getSubjects() {
        return subjectsRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getSubject(@PathVariable Long id) {
        var result = subjectsRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Subject with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var subject = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(subject)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createSubject(@RequestBody Subject newSubject) {
        var subject = subjectsRepository.save(newSubject);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(subject)
        ));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteSubject(@PathVariable Long id){
        if (subjectsRepository.findById(id).isEmpty()){
            var message = String.format("Subject with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        subjectsRepository.deleteById(id);
        var message = String.format("Subject with id: %s has been successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
}
