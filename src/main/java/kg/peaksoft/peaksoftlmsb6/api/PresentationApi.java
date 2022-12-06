package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.PresentationRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.PresentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/presentation")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Presentation API", description = "Presentation api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class PresentationApi {

    private final PresentationService presentationService;

    @PostMapping
    @Operation(summary = "Save presentation",
            description = "To save a new presentation by Instructor")
    public PresentationResponse createPresentation(@RequestBody PresentationRequest request) {
        return presentationService.savePresentation(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update presentation",
            description = "Instructor update presentation by id")
    public PresentationResponse updatePresentation(@PathVariable("id") Long id, @RequestBody PresentationRequest request) {
        return presentationService.updatePresentation(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete presentation",
            description = "Instructor delete presentation by id")
    public SimpleResponse deletePresentation(@PathVariable("id") Long id) {
        return presentationService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get presentation by id",
            description = "Instructor get presentation by id")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public PresentationResponse getPresentationById(@PathVariable("id") Long id) {
        return presentationService.getById(id);
    }
}
