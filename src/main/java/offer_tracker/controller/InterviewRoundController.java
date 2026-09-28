package offer_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import offer_tracker.dto.InterviewRoundRequest;
import offer_tracker.dto.InterviewRoundResponse;
import offer_tracker.service.InterviewRoundService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/rounds")
@RequiredArgsConstructor
@Tag(name = "Interview Rounds", description = "Manage interview rounds of a job application")
public class InterviewRoundController {

    private final InterviewRoundService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add an interview round",
            description = "If the application is APPLIED, its status is moved to INTERVIEWING automatically.")
    public InterviewRoundResponse create(@PathVariable Long applicationId,
                                         @Valid @RequestBody InterviewRoundRequest request) {
        return service.create(applicationId, request);
    }

    @GetMapping
    @Operation(summary = "List interview rounds of an application")
    public List<InterviewRoundResponse> list(@PathVariable Long applicationId) {
        return service.list(applicationId);
    }

    @PutMapping("/{roundId}")
    @Operation(summary = "Update an interview round")
    public InterviewRoundResponse update(@PathVariable Long applicationId,
                                         @PathVariable Long roundId,
                                         @Valid @RequestBody InterviewRoundRequest request) {
        return service.update(applicationId, roundId, request);
    }

    @DeleteMapping("/{roundId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete an interview round")
    public void delete(@PathVariable Long applicationId, @PathVariable Long roundId) {
        service.delete(applicationId, roundId);
    }
}