package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.api.StudentApi;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Group;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.apache.poi.ss.usermodel.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
        return new SimpleResponse(String.format("student with id = %s deleted", id));
    }


    public List<StudentResponse> getAllStudent(StudyFormat studyFormat) {
        if (studyFormat.equals(StudyFormat.ALL)) {
            return studentRepository.getAllStudents();
        } else {
            return studentRepository.findStudentByStudyFormat(studyFormat);
        }
    }

//    public List<StudentResponse> importExcel(MultipartFile file, Long id) throws IOException {
//        List<Student> students = new ArrayList<>();
//        Workbook workbook = WorkbookFactory.create(file.getInputStream(), "123456");
//        for (Sheet sheet : workbook) {
//            System.out.print(sheet.getSheetName());
//        }
//        Sheet sheet = workbook.getSheetAt(0);
//        DataFormatter dataFormatter = new DataFormatter();
//        for (Row row : sheet) {
//            User user = new User();
//            user.setEmail(row.getCell(6).getStringCellValue());
//            for (Cell cell : row) {
//                String cellValue = dataFormatter.formatCellValue(cell);
//                System.out.print(cellValue);
//            }
//        }
//        workbook.close();
//        for (Student student : students) {
//            Group groupEntity = groupRepository.findById(id).orElseThrow(
//                    () -> new NotFoundException(String.format("Group with id =%s not found",id)));
//            student.setGroup(groupEntity);
//            studentRepository.save(student);
//        }
//        List<StudentResponse> studentResponses = new ArrayList<>();
//        for (Student student: studentRepository.findAll()) {
//            studentResponses.add(studentRepository.getStudent(student.getId()));
//        }
//        return studentResponses;
//    }

    public List<StudentResponse> importExcel(MultipartFile file, Long id) throws IOException {
        String path = "C:\\Users\\user\\IdeaProjects\\peaksoftlms-b6\\students.xlsx";
        List<Student> students = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(String.valueOf(file));
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        System.out.println("Hello world!");
        for (Sheet sheet : workbook) {
            System.out.print(sheet.getSheetName());
        }
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : sheet) {
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                System.out.print(cellValue);
                Student student = new Student();
                if(row.getCell(0)==null){
                    throw new BadRequestException("FirstName in line index "+cell+" is empty!");
                }else {
                    student.setFirstName(row.getCell(0).getStringCellValue());
                }

                if(row.getCell(1)==null){
                    throw new BadRequestException("LastName in line index "+cell+" is empty!");
                }else {
                    student.setLastName(row.getCell(1).getStringCellValue());
                }

                if(row.getCell(2)==null){
                    throw new BadRequestException("Phone number in line index "+cell+" is empty!");
                }else {
                    student.setPhoneNumber(String.valueOf((long) row.getCell(2).getNumericCellValue()));
                }

                if(row.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)==null){
                    throw new BadRequestException("Study format in line index "+cell+" is empty!");
                }else {
                    student.setStudyFormat(StudyFormat.valueOf(row.getCell(3).getStringCellValue()));
                }
                User user = new User();

                if(row.getCell(4,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)==null){
                    throw new BadRequestException("Email in line index "+cell+" is empty!");
                }else {
                    user.setEmail(row.getCell(4).getStringCellValue());
                }

                if(row.getCell(5)==null){
                    throw new BadRequestException("Role in line index "+cell+" is empty!");
                }else {
                    user.setRole(Role.valueOf(row.getCell(5).getStringCellValue()));
                }

                if(row.getCell(6)==null){
                    throw new BadRequestException("Password in line index "+cell+" is empty!");
                }else {
                    user.setPassword(passwordEncoder.encode(String.valueOf(row.getCell(6).getStringCellValue())));
                }

                student.setUser(user);
                students.add(student);
            }
        }

        for (Student student : students) {
            Group groupEntity = groupRepository.findById(id).orElseThrow(
                    () -> new NotFoundException(String.format("Group with id =%s not found",id)));
            student.setGroup(groupEntity);
            studentRepository.save(student);
        }

        List<StudentResponse> studentResponses = new ArrayList<>();
        for (Student student : studentRepository.findAll()) {
            studentResponses.add(studentRepository.getStudent(student.getId()));
        }
        return studentResponses;
    }


}
//        workbook.close();
//        inputStream.close();
//        List<StudentResponse> studentResponses = new ArrayList<>();
//        for (Student student : studentRepository.findAll()) {
//            studentResponses.add(studentRepository.getStudent(student.getId()));
//        }
//        return studentResponses;



    //        PoijiOptions options = PoijiOptions.PoijiOptionsBuilder.settings()
//                .password("123456789")
//                .build();
//        List<Student> students = Poiji.fromExcel(new File("students.xlsx"), Student.class, options);
//        students.size();
//        Student student = students.get(0);