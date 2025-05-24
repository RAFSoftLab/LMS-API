package com.raf.learning.controller;

import com.google.gson.Gson;
import com.raf.learning.dto.EventBatchDto;
import com.raf.learning.dto.StudentEventDto;
import com.raf.learning.model.ResponseMessage;
import com.raf.learning.model.StudentEvent;
import com.raf.learning.model.StudentSession;
import com.raf.learning.model.StudentStruggle;
import com.raf.learning.repository.StudentEventRepository;
import com.raf.learning.repository.StudentSessionRepository;
import com.raf.learning.repository.StudentStruggleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tracking")
public class StudentTrackingController {

    private final StudentEventRepository studentEventRepository;
    private final StudentSessionRepository studentSessionRepository;
    private final StudentStruggleRepository studentStruggleRepository;
    private final Gson gson;

    public StudentTrackingController(StudentEventRepository studentEventRepository,
                                     StudentSessionRepository studentSessionRepository,
                                     StudentStruggleRepository studentStruggleRepository) {
        this.studentEventRepository = studentEventRepository;
        this.studentSessionRepository = studentSessionRepository;
        this.studentStruggleRepository = studentStruggleRepository;
        this.gson = new Gson();
    }

    // Batch event ingestion endpoint
    @PostMapping("/events/batch")
    public ResponseEntity<ResponseMessage> ingestEventBatch(@RequestBody EventBatchDto eventBatch) {
        try {
            List<StudentEvent> events = eventBatch.getEvents().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());

            studentEventRepository.saveAll(events);

            // Update session statistics
            updateSessionStatistics(events);

            String message = String.format("Successfully ingested %d events", events.size());
            return ResponseEntity.ok(new ResponseMessage(message));

        } catch (Exception e) {
            String errorMessage = "Failed to ingest event batch: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage(errorMessage));
        }
    }

    // Get events for a specific student
    @GetMapping("/events/student/{studentId}")
    public ResponseEntity<ResponseMessage> getStudentEvents(@PathVariable String studentId) {
        try {
            List<StudentEvent> events = studentEventRepository.findByStudentIdOrderByTimestampAsc(studentId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(events)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve events: " + e.getMessage()));
        }
    }

    // Get events for a specific session
    @GetMapping("/events/session/{sessionId}")
    public ResponseEntity<ResponseMessage> getSessionEvents(@PathVariable String sessionId) {
        try {
            List<StudentEvent> events = studentEventRepository.findBySessionIdOrderByTimestampAsc(sessionId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(events)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve session events: " + e.getMessage()));
        }
    }

    // Get session summary
    @GetMapping("/sessions/student/{studentId}")
    public ResponseEntity<ResponseMessage> getStudentSessions(@PathVariable String studentId) {
        try {
            List<StudentSession> sessions = studentSessionRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(sessions)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve sessions: " + e.getMessage()));
        }
    }

    // Get struggles for a student
    @GetMapping("/struggles/student/{studentId}")
    public ResponseEntity<ResponseMessage> getStudentStruggles(@PathVariable String studentId) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findByStudentIdOrderByStartTimeDesc(studentId);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(struggles)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve struggles: " + e.getMessage()));
        }
    }

    // Analytics endpoint - high severity struggles
    @GetMapping("/analytics/struggles/high-severity")
    public ResponseEntity<ResponseMessage> getHighSeverityStruggles(@RequestParam(defaultValue = "7") Integer minSeverity) {
        try {
            List<StudentStruggle> struggles = studentStruggleRepository.findHighSeverityStruggles(minSeverity);
            return ResponseEntity.ok(new ResponseMessage(gson.toJson(struggles)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage("Failed to retrieve high severity struggles: " + e.getMessage()));
        }
    }

    // Helper methods
    private StudentEvent convertToEntity(StudentEventDto dto) {
        StudentEvent event = new StudentEvent();
        event.setStudentId(dto.getStudentId());
        event.setSessionId(dto.getSessionId());
        event.setEventType(dto.getEventType());
        event.setTimestamp(dto.getTimestamp());
        event.setEventData(gson.toJson(dto.getEventData()));
        event.setTaskId(dto.getTaskId());
        return event;
    }

    private void updateSessionStatistics(List<StudentEvent> events) {
        // Group events by session and update statistics
        events.stream()
                .collect(Collectors.groupingBy(StudentEvent::getSessionId))
                .forEach((sessionId, sessionEvents) -> {
                    StudentSession session = studentSessionRepository.findBySessionId(sessionId)
                            .orElse(new StudentSession(sessionId,
                                    sessionEvents.get(0).getStudentId(),
                                    sessionEvents.get(0).getTaskId(),
                                    LocalDateTime.now()));

                    // Update counters
                    session.setTotalEvents(session.getTotalEvents() + sessionEvents.size());

                    long compilationEvents = sessionEvents.stream()
                            .filter(e -> e.getEventType().contains("COMPILATION"))
                            .count();
                    session.setCompilationAttempts(session.getCompilationAttempts() + (int) compilationEvents);

                    long codeChangeEvents = sessionEvents.stream()
                            .filter(e -> e.getEventType().equals("CODE_CHANGE"))
                            .count();
                    session.setCodeChanges(session.getCodeChanges() + (int) codeChangeEvents);

                    studentSessionRepository.save(session);
                });
    }
}
