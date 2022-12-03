package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.VideoRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.VideoResponse;
import kg.peaksoft.peaksoftlmsb6.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Video API", description = "Video api endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class VideoApi {

    private final VideoService videoService;

    @PostMapping
    @Operation(summary = "Save video",
            description = "To save a new video by Instructor")
    public VideoResponse saveVideo(@RequestBody VideoRequest request) {
        return videoService.createVideo(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update video",
            description = "Instructor update video by id")
    public VideoResponse updateVideo(@PathVariable("id") Long id, @RequestBody VideoRequest request) {
        return videoService.updateVideo(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete video",
            description = "Instructor delete video by id")
    public SimpleResponse deleteVideo(@PathVariable("id") Long id) {
        return videoService.deleteById(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get video by id",
            description = "Instructor get video by id")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public VideoResponse getVideoById(@PathVariable("id") Long id) {
        return videoService.getById(id);
    }
}