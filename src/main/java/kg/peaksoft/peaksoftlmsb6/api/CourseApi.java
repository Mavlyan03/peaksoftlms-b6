package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Deque;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Course API", description = "ADMIN course layout api endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class CourseApi {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Save course",
            description = "Admin save new course")
    public CourseResponse createCourse(@RequestBody CourseRequest request) {
        return courseService.createCourse(request);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update course",
            description = "Admin update course by id")
    public CourseResponse updateCourse(@PathVariable("id") Long id, @RequestBody CourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete course",
            description = "Admin delete course by id")
    public SimpleResponse deleteCourse(@PathVariable("id") Long id) {
        return courseService.deleteById(id);
    }

    @PostMapping("/assign")
    @Operation(summary = "Assign",
            description = "Admin assign instructors to course by their id")
    public SimpleResponse assign(@RequestBody AssignInstructorRequest request) {
        return courseService.assignInstructorToCourse(request);
    }

    @PostMapping("/unassigned")
    @Operation(summary = "Unassigned course",
            description = "Admin unassigned instructor from course by their id")
    public SimpleResponse unassigned(@RequestBody AssignInstructorRequest request) {
        return courseService.unassigned(request);
    }

    @GetMapping("/instructors/{id}")
    @Operation(summary = "Get all instructor from course",
            description = "Admin get all instructors from course by course id")
    public List<AssignInstructorResponse> getAllInstructorsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllInstructorsFromCourse(id);
    }

    @GetMapping("/students/{id}")
    @Operation(summary = "Get all students from course",
            description = "Admin get all students from course by course id")
    public List<StudentResponse> getAllStudentsFromCourse(@PathVariable("id") Long id) {
        return courseService.getAllStudentsFromCourse(id);
    }

    @GetMapping
    @Operation(summary = "Get all courses",
            description = "Get all courses for admin, instructor, student")
    @PreAuthorize("hasAnyAuthority('ADMIN','INSTRUCTOR','STUDENT')")
    public Deque<CourseResponse> getAllCourses(Authentication authentication) {
        return courseService.getAllCourses(authentication);
    }

    @PostMapping("/assign/group")
    @Operation(summary = "Assign group to course",
            description = "Instructor assign group to course by their id")
    public SimpleResponse assignGroupToCourse(@RequestBody AssignGroupRequest request) {
        return courseService.assignGroupToCourse(request);
    }

    @DeleteMapping("/group/{id}")
    @Operation(summary = "Delete group from course",
            description = "Instructor delete group from course by id")
    public SimpleResponse deleteGroupFromCourse(@PathVariable Long id) {
        return courseService.deleteGroupFromCourse(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get course by id",
            description = "Get course by id for admin")
    public CourseResponse getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }
}