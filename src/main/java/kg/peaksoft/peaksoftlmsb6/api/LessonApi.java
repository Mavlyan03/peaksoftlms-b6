package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.LessonRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LessonResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/lesson")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Lesson CRUD", description = "Instructor my course inner page endpoints")
@PreAuthorize("hasAuthority('INSTRUCTOR')")
public class LessonApi {

    private final LessonService lessonService;

    @PostMapping
    @Operation(summary = "Save a new lesson to course",
            description = "Instructor saves new lesson")
    public SimpleResponse addLesson(@RequestBody LessonRequest request){
        return lessonService.createLesson(request);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update lesson",
            description = "Instructor updates lesson by id")
    public LessonResponse updateLesson(@PathVariable Long id,
                                       @RequestBody @Valid LessonRequest request){
        return lessonService.updateLesson(id, request);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "delete lesson",
            description = "Instructor deletes lesson by id")
    public SimpleResponse deleteLesson(@PathVariable Long id){
        return lessonService.deleteLesson(id);
    }

    @GetMapping("/course/{id}")
    @Operation(summary = "Get all lessons",
            description = "Instructor gets all lessons")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public List<LessonResponse> getAllLessons(@PathVariable Long id){
        return lessonService.getAllLessonsByCourseId(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get lesson by id",
            description = "Get lesson by id for instructor")
    @PreAuthorize("hasAnyAuthority('INSTRUCTOR','STUDENT')")
    public LessonResponse getLessonById(@PathVariable Long id) {
        return lessonService.getById(id);
    }
}
