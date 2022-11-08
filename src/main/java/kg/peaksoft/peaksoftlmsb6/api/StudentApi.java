package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name =  "Student API", description = "ADMIN student api endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentApi {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Save student",
            description = "To save a new student by admin")
    public StudentResponse createStudent(@RequestBody StudentRequest studentRequest){
        return studentService.createStudent(studentRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student",
            description = "Admin update student by id")
    public StudentResponse updateStudent(@PathVariable Long id,
                                         @RequestBody StudentRequest studentRequest){
        return studentService.update(id, studentRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student",
            description = "Admin delete student by id")
    public SimpleResponse deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }


    @GetMapping("/filter")
    @Operation(summary = "Get all students",
              description = "Get all students with filter by study format")
    public List<StudentResponse> getStudentByStudyFormat(@RequestParam StudyFormat studyFormat){
        return studentService.getAllStudent(studyFormat);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by id",
            description = "Get student by id for admin")
    public StudentResponse getStudentById(@PathVariable Long id) {
        return studentService.getById(id);
    }

}
