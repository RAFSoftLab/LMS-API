package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.TestPeriod;
import com.raf.learning.repository.TestsPeriodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/testsPeriods")
public class TestsPeriodController {
    private final TestsPeriodRepository testsPeriodRepository;

    public TestsPeriodController(TestsPeriodRepository testsPeriodRepository) {
        this.testsPeriodRepository = testsPeriodRepository;
    }

    @GetMapping
    public List<TestPeriod> getTests() {
        return testsPeriodRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getTest(@PathVariable Long id) {
        var result = testsPeriodRepository.findById(id);

        if (result.isEmpty()) {
            var message = String.format("Test period with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }

        var testPeriod = result.get();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(testPeriod)
        ));
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createTest(@RequestBody TestPeriod newTestPeriod) {
        var test = testsPeriodRepository.save(newTestPeriod);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
                new Gson().toJson(test)
        ));
    }

    @DeleteMapping
    public ResponseEntity<ResponseMessage> deleteTest(@PathVariable Long id){
        if (testsPeriodRepository.findById(id).isEmpty()){
            var message = String.format("Test with id: %s doesn't exist in the db", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
        testsPeriodRepository.deleteById(id);
        var message = String.format("Test with id: %s has been successfully deleted", id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
}
