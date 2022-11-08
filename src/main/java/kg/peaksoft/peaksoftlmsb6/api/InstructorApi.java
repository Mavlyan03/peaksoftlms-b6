package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructor")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Instructor API", description = "ADMIN instructor api endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class InstructorApi {
    private final InstructorService instructorService;

    @PostMapping()
    @Operation(summary = "Save instructor",
            description = "To save instructor by request")
    public InstructorResponse createInstructor(@RequestBody @Valid InstructorRequest request) {
        return instructorService.createInstructor(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates instructor",
            description = "Admin update instructor by id")
    public InstructorResponse updateInstructor(@PathVariable Long id,
                                               @RequestBody @Valid InstructorRequest request) {
        return instructorService.updateInstructor(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes instructor ",
            description = "Admin delete the instructor by id")
    public SimpleResponse deleteInstructor(@PathVariable Long id) {
        return instructorService.deleteInstructorById(id);
    }

    @GetMapping
    @Operation(summary = "Gets all instructors",
            description = "Admin get all instructors")
    public List<InstructorResponse> getAllInstructors() {
        return instructorService.getAllInstructors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get instructor by id",
            description = "Get instructor by id for admin")
    public InstructorResponse getInstructorById(@PathVariable Long id) {
        return instructorService.getById(id);
    }

}
