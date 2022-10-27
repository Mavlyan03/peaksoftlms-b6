package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name =  "Student CRUD", description = "ADMIN student api endpoints")
@PreAuthorize("hasAuthority('ADMIN')")
public class StudentApi {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Save students",
            description = "Admin save student be request")
    public StudentResponse createStudent(@RequestBody StudentRequest studentRequest){
        return studentService.createStudent(studentRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updates student",
            description = "Admin update student by id")
    public StudentResponse updateStudent(@PathVariable Long id,
                                         @RequestBody StudentRequest studentRequest){
        return studentService.update(id, studentRequest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes student",
            description = "Admin delete student by id")
    public SimpleResponse deleteStudent(@PathVariable Long id){
        return studentService.deleteStudent(id);
    }

    @GetMapping
    @Operation(summary = "Get all students",
            description = "Admin get all students")
    public List<StudentResponse> getAllStudents(){
        return studentService.getAllStudent();
    }
}
