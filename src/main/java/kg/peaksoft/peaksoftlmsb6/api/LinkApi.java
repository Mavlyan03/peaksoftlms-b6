package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.LinkRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/link")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Link API", description = "Instructor api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class LinkApi {

    private final LinkService linkService;

    @PostMapping
    @Operation(summary = "Save link",
            description = "To save a new link by Instructor")
    public LinkResponse createLink(@RequestBody LinkRequest linkRequest){
        return linkService.createLink(linkRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update link",
            description = "Instructor update link by id")
    public LinkResponse updateLink(@PathVariable Long id,
                                         @RequestBody LinkRequest linkRequest){
        return linkService.updateLink(id, linkRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete link",
            description = "Instructor delete link by id")
    public SimpleResponse deleteLink(@PathVariable Long id){
        return linkService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get link by id",
            description = "Instructor get link by id")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public LinkResponse getLinkById(@PathVariable Long id) {
        return linkService.getLinkById(id);
    }
}
