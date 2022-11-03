package kg.peaksoft.peaksoftlmsb6.service;

import com.poiji.bind.Poiji;
import com.poiji.option.PoijiOptions;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.poiji.MyFormatting;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public StudentResponse createStudent(StudentRequest studentRequest) {
        studentRequest.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group not found with id=%s", studentRequest.getGroupId())));
        Student student = new Student(studentRequest);
        group.addStudents(student);
        student.setGroup(group);
        studentRepository.save(student);
        return studentRepository.getStudent(student.getId());
    }

    public StudentResponse update(Long id, StudentRequest studentRequest) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Student with id =%s not found", studentRequest.getId())));
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group not found with id=%s", studentRequest.getGroupId())));
        student.setGroup(group);
        group.addStudents(student);
        studentRepository.update(
                student.getId(),
                studentRequest.getFirstName(),
                studentRequest.getLastName(),
                studentRequest.getStudyFormat(),
                studentRequest.getPhoneNumber());
        student.getUser().setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id =%s not found", student.getUser().getId())));
        user.setEmail(studentRequest.getEmail());
        user.setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        studentRepository.save(student);
        return studentRepository.getStudent(student.getId());
    }

    public SimpleResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Not found"));
        studentRepository.delete(student);
        return new SimpleResponse(String.format("student with id = %s deleted",id));
    }

    public List<StudentResponse> getAllStudent() {
        return studentRepository.getAllStudents();
    }

    PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
            .withFormatting(new MyFormatting())
            .build();
    List<Student> students = Poiji.fromExcel(new File("students.xls"), Student.class, options);
    Student student = students.get(0);

}