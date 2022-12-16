package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.EnableTestResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TestInnerPageResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TestResponse;
import kg.peaksoft.peaksoftlmsb6.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Test API", description = "INSTRUCTOR api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class TestApi {

    private final TestService testService;

    @PostMapping
    @Operation(summary = "Save test",
            description = "To save a new test by request")
    public TestResponse createTest(@RequestBody TestRequest request) {
        return testService.createTest(request);
    }


    @PutMapping("/{id}")
    @Operation(summary = "Update test",
            description = "Instructor update test by id")
    public TestInnerPageResponse updateTest(@PathVariable Long id, @RequestBody TestRequest testRequest) {
        return testService.updateTest(id, testRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete test",
            description = "Instructor delete test by id")
    public SimpleResponse deleteTest(@PathVariable Long id) {
        return testService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get test by id",
            description = "Get test by id")
//    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public TestInnerPageResponse getById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    @PutMapping("/enable/{id}")
    @Operation(summary = "Test is enable",
            description = "Admin switch enable or disable status of test")
    public SimpleResponse isEnable(@PathVariable Long id) {
        return testService.isEnable(id);
    }
}
