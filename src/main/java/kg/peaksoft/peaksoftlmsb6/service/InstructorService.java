package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public SimpleResponse addInstructor(InstructorRequest request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Instructor instructor = new Instructor(request);
        instructorRepository.save(instructor);
        return new SimpleResponse("instructor saved");
    }

    public SimpleResponse updateInstructor(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(String.format("Instructor with id =%s not found",id)));
        method(request, instructor);
        User user = userRepository.findById(instructor.getUser().getId())
                        .orElseThrow(()-> new NotFoundException(String.format("User with id =%s not found",instructor.getUser().getId())));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.INSTRUCTOR);
        userRepository.save(user);
        instructor.setUser(user);
        instructorRepository.save(instructor);
        return new SimpleResponse("Instructor updated");
    }

    public static void method(InstructorRequest request, Instructor instructor) {
        instructor.setFirstName(request.getFirstName());
        instructor.setLastName(request.getLastName());
        instructor.setPhoneNumber(request.getPhoneNumber());
        instructor.setSpecialization(request.getSpecialization());

    }

    public SimpleResponse deleteInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                        .orElseThrow(()-> new NotFoundException(String.format("Instructor with id =%s not found",id)));
        instructorRepository.delete(instructor);
        return new SimpleResponse("Instructor deleted");
    }

    public List<InstructorResponse> getAllInstructors() {
         return instructorRepository.getAllInstructors();
    }

}
