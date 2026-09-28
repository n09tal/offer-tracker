package offer_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import offer_tracker.dto.JobApplicationRequest;
import offer_tracker.dto.JobApplicationResponse;
import offer_tracker.service.JobApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import offer_tracker.dto.StatusUpdateRequest;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Job Applications", description = "Manage your job applications")
public class JobApplicationController {

    private final JobApplicationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a job application")
    public JobApplicationResponse create(@Valid @RequestBody JobApplicationRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "List my job applications")
    public List<JobApplicationResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a job application by id")
    public JobApplicationResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update company and position")
    public JobApplicationResponse update(@PathVariable Long id,
                                         @Valid @RequestBody JobApplicationRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change application status",
               description = "Allowed transitions: APPLIED -> INTERVIEWING or REJECTED; "
               + "INTERVIEWING -> OFFER or REJECTED. OFFER and REJECTED are final.")
    public JobApplicationResponse updateStatus(@PathVariable Long id, 
                                               @Valid @RequestBody StatusUpdateRequest request) {
        return service.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a job application")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}