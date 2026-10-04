package offer_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import offer_tracker.dto.GenerateQuestionsRequest;
import offer_tracker.dto.InterviewQuestionResponse;
import offer_tracker.service.InterviewQuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/questions")
@RequiredArgsConstructor
@Tag(name = "Interview Questions", description = "Generate interview questions from a job description with AI")
public class InterviewQuestionController {

    private final InterviewQuestionService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Generate interview questions",
            description = "Sends the job description to an LLM and saves the generated questions. "
                    + "Existing questions of this application are replaced.")
    public List<InterviewQuestionResponse> generate(@PathVariable Long applicationId,
                                                    @Valid @RequestBody GenerateQuestionsRequest request) {
        return service.generate(applicationId, request);
    }

    @GetMapping
    @Operation(summary = "List saved interview questions of an application")
    public List<InterviewQuestionResponse> list(@PathVariable Long applicationId) {
        return service.list(applicationId);
    }
}