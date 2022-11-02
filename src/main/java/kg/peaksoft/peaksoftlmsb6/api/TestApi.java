package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.TestRequest;
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

//    @PostMapping
//    @Operation(summary = "Save test",
//            description = "To save a new test by request")
//    public TestResponse save(@RequestBody TestRequest request) {
//        return testService.save(request);
//    }

//    @GetMapping("/{id}")
//    @Operation(summary = "Get test by id",
//            description = "Get test by id")
//    public TestInnerPageResponse getById(@PathVariable Long id) {
//        return testService.getTestById(id);
//    }

}
