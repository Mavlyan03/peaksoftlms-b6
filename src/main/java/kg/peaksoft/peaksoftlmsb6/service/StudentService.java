package kg.peaksoft.peaksoftlmsb6.service;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import com.poiji.option.PoijiOptions;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentExcelRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateStudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    private final PasswordEncoder passwordEncoder;

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    public StudentResponse createStudent(StudentRequest studentRequest) throws MessagingException {
        if (userRepository.existsByEmail(studentRequest.getEmail())) {
            throw new BadRequestException("Student already exists");
        }
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", studentRequest.getGroupId());
                    throw new NotFoundException("Группа не найден");
                });
        Student student = new Student(studentRequest);
        group.addStudents(student);
        student.setGroup(group);
        String password = studentRequest.getPassword();
        student.getUser().setPassword(passwordEncoder.encode(studentRequest.getPassword()));
        studentRepository.save(student);
        log.info("Save a new student by request was successfully");
//        sendEmail(student.getUser().getEmail(), password);
        return studentRepository.getStudent(student.getId());
    }

//    private void sendEmail(String email, String password) throws MessagingException {
//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new NotFoundException(String.format("Пользователь с email =%s не найден", email)));
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
//        messageHelper.setSubject("[peaksoftlms-b6] send password and username");
//        messageHelper.setFrom("peaksoftlms-b6@gmail.com");
//        messageHelper.setTo(user.getEmail());
//        messageHelper.setText("WELCOME TO PEAKSOFT SCHOOL! " +
//                " Username: " + user.getUsername() + "  Password: " + password, true);
//        javaMailSender.send(mimeMessage);
//    }

    public StudentResponse update(Long id, UpdateStudentRequest studentRequest) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Student with id {} not found", id);
                    throw new NotFoundException("Студент не найден");
                });
        Group group = groupRepository.findById(studentRequest.getGroupId()).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", studentRequest.getGroupId());
                    throw new NotFoundException("Группа не найден");
                });
        student.setGroup(group);
        group.addStudents(student);
        int index = studentRequest.getFullName().lastIndexOf(' ');
        String firstName = studentRequest.getFullName().substring(0,index);
        String lastName = studentRequest.getFullName().substring(index + 1);
        studentRepository.update(
                student.getId(),
                firstName,
                lastName,
                studentRequest.getStudyFormat(),
                studentRequest.getPhoneNumber());
        User user = userRepository.findById(student.getUser().getId())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        user.setEmail(studentRequest.getEmail());
        studentRepository.save(student);
        log.info("Update student with id {} was successfully", id);
        return studentRepository.getStudent(student.getId());
    }

    public SimpleResponse deleteStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Student with id {} not found", id);
                    throw new NotFoundException("Студент не найден");
                });
        studentRepository.delete(student);
        log.info("Delete student by id {} was successfully", id);
        return new SimpleResponse("Студент удалён");
    }


    public List<StudentResponse> getAllStudent(StudyFormat studyFormat) {
        if (studyFormat.equals(StudyFormat.ALL)) {
            log.info("Get all students by study format {} was successfully", studyFormat);
            return studentRepository.getAllStudents();
        } else {
            log.info("Get all students by study format {} was successfully", studyFormat);
            return studentRepository.findStudentByStudyFormat(studyFormat);
        }
    }

    public StudentResponse getById(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Student with id {} not found", id);
                    throw new NotFoundException("Студент не найден");
                });
        log.info("Get student by id {} was successfully", id);
        return studentRepository.getStudent(student.getId());
    }

    public SimpleResponse importExcel(Long groupId, MultipartFile multipartFile) throws IOException, MessagingException {
        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", groupId);
                    throw new NotFoundException("Группа с " + groupId + " id не найдено !!!");
                });
        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings().build();
        InputStream inputStream = multipartFile.getInputStream();

        if (multipartFile.isEmpty()) {
            throw new BadRequestException("Файл пуст");
        }

        List<StudentExcelRequest> excelResponseList = Poiji.fromExcel(inputStream, PoijiExcelType.XLSX, StudentExcelRequest.class, options);

        List<Student> students = new ArrayList<>();

        Random random = new Random();

        for (StudentExcelRequest studentExcelRequest : excelResponseList) {

            boolean exists = studentRepository.existsByUserEmail(studentExcelRequest.getEmail());

            if (!exists) {

                int randomNumber = random.nextInt(1000);

                int email = studentExcelRequest.getEmail().lastIndexOf('@');
                String password = studentExcelRequest.getEmail().substring(0, email);
                String generatedPassword = password + randomNumber;

                Student student = new Student(studentExcelRequest, passwordEncoder.encode(generatedPassword));
                student.setGroup(group);
                group.addStudents(student);

                students.add(student);
//                sendEmail(student.getUser().getEmail(), generatedPassword);
            }
        }
        studentRepository.saveAll(students);
        log.info("Import students from excel was successfully");
        return new SimpleResponse("Студенты из файла Excel успешно добавлены");
    }
}