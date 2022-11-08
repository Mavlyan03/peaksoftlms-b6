package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;


    public InstructorResponse createInstructor(InstructorRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Instructor instructor = new Instructor(request);
        instructorRepository.save(instructor);
        return instructorRepository.getInstructor(instructor.getId());
    }

    public InstructorResponse updateInstructor(Long id, InstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Instructor with id =%s not found", id)));
        instructorRepository.update(instructor.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getSpecialization(),
                request.getPhoneNumber());
        User user = userRepository.findById(instructor.getUser().getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id =%s not found", instructor.getUser().getId())));
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.INSTRUCTOR);
        instructor.setUser(user);
        instructorRepository.save(instructor);
        return instructorRepository.getInstructor(instructor.getId());
    }

    public SimpleResponse deleteInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Instructor with id =%s not found", id)));
        instructorRepository.delete(instructor);
        return new SimpleResponse("Instructor deleted");
    }

    public List<InstructorResponse> getAllInstructors() {
        return instructorRepository.getAllInstructors();
    }


    public InstructorResponse getById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Инструктор с id =%s не найден", id)));
        return instructorRepository.getInstructor(instructor.getId());
    }
}