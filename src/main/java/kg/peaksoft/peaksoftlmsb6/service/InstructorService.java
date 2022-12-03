package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.InstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Instructor;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InstructorService {

    private final InstructorRepository instructorRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public InstructorResponse createInstructor(InstructorRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Instructor already exists");
            throw new BadRequestException("Инструктор уже существует");
        }
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        Instructor instructor = new Instructor(request);
        instructorRepository.save(instructor);
        log.info("New instructor successfully saved!");
        return instructorRepository.getInstructor(instructor.getId());
    }

    public InstructorResponse updateInstructor(Long id, UpdateInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Instructor with id {} not found", id);
                    throw new NotFoundException("Инструктор не найден");
                });
        int index = request.getFullName().lastIndexOf(' ');
        String firstName = request.getFullName().substring(0, index);
        String lastName = request.getFullName().substring(index + 1);
        instructorRepository.update(instructor.getId(),
                firstName,
                lastName,
                request.getSpecialization(),
                request.getPhoneNumber());
        User user = userRepository.findById(instructor.getUser().getId())
                .orElseThrow(() -> {
                    log.error("User with id {} not found", instructor.getUser().getId());
                    throw new NotFoundException("Пользователь не найден");
                });
        user.setEmail(request.getEmail());
        user.setRole(Role.INSTRUCTOR);
        instructor.setUser(user);
        instructorRepository.save(instructor);
        log.info("Update instructor with id {} was successfully", id);
        return instructorRepository.getInstructor(instructor.getId());
    }

    public SimpleResponse deleteInstructorById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Instructor with id {} not found", id);
                    throw new NotFoundException("Инструктор не найден");
                });
        instructorRepository.delete(instructor);
        log.info("Delete instructor by id {} was successfully", id);
        return new SimpleResponse("Инструктор удалён");
    }

    public List<InstructorResponse> getAllInstructors() {
        log.info("Get all instructor was successfully");
        return instructorRepository.getAllInstructors();
    }

    public InstructorResponse getById(Long id) {
        Instructor instructor = instructorRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Instructor with id {} not found", id);
                    throw new NotFoundException("Инструктор не найден");
                });
        log.info("Get instructor by id {} was successfully", id);
        return instructorRepository.getInstructor(instructor.getId());
    }
}