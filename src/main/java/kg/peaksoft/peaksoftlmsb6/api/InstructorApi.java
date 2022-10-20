package kg.peaksoft.peaksoftlmsb6.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/instructor")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Instructor CRUD", description = "ADMIN can do a CRUD operations")
@PreAuthorize("hasAuthority('ADMIN')")
public class InstructorApi {

    private final InstructorService instructorService;


    @PostMapping()
    @Operation(description = "ADMIN add new instructor with fields first name," +
            "last name, phone number, specialization and email")
    public SimpleResponse addInstructor(@RequestBody InstructorRequest request){
        return instructorService.addInstructor(request);
    }

    @PutMapping("/{id}")
    @Operation(description = "ADMIN update instructor with fields first name," +
            "last name, phone number, specialization and email")
    public SimpleResponse updateInstructor(@PathVariable Long id,
                                               @RequestBody InstructorRequest request){
       return instructorService.updateInstructor(id,request);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "ADMIN delete instructor by id with fields first name," +
            "last name, phone number, specialization and email")
    public SimpleResponse deleteInstructor(@PathVariable Long id){
        return instructorService.deleteInstructorById(id);
    }

    @GetMapping
    @Operation(description = "ADMIN get instructors with fields first name," +
            "last name, phone number, specialization and email")
    public List<InstructorResponse> getAllInstructors(){
        return instructorService.getAllInstructors();
    }

}
