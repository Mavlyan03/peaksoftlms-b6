package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.PassTestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.ResultResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResultResponse;
import kg.peaksoft.peaksoftlmsb6.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/results")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Results API", description = "INSTRUCTOR results api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class ResultApi {

    private final ResultService resultService;

//    @PostMapping
//    @Operation(summary = "Pass test",
//            description = "Students pass test")
//    @PreAuthorize("hasAnyAuthority('STUDENT')")
//    public SimpleResponse saveResult(Authentication authentication, @RequestBody PassTestRequest passTestRequest) {
//        return resultService.passTest(passTestRequest, authentication);
//    }

    @PostMapping
    @Operation(summary = "Pass test",
            description = "Students pass test")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public StudentResultResponse saveResult(Authentication authentication,
                                            @RequestBody PassTestRequest passTestRequest) {
        return resultService.passTest(passTestRequest, authentication);
    }

    @GetMapping("/lesson/{id}")
    @Operation(summary = "Get all results",
            description = "Instructor get all results by test id")
    public List<ResultResponse> getAllResults(@PathVariable Long id) {
        return resultService.getAllResults(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get result by id",
            description = "Instructor get result by id")
    public ResultResponse getResultById(@PathVariable Long id) {
        return resultService.getById(id);
    }

}
