package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.Test;
import com.raf.learning.repository.TestsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tests")
public class TestsController {
    private final TestsRepository testsRepository;

    public TestsController(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    @GetMapping
    public List<Test> getTests() {
        return testsRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getTest(@PathVariable Long id) {
        var result = testsRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Test with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var test = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(test)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createTest(@RequestBody Test newTest) {
        var test = testsRepository.save(newTest);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(test)
        ));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteTest(@PathVariable Long id){
        if (testsRepository.findById(id).isEmpty()){
            var message = String.format("Test with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        testsRepository.deleteById(id);
        var message = String.format("Test with id: %s has been successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
}
