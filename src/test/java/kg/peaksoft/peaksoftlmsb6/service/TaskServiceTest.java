package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.ContentRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.TaskRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.TaskResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Task;
import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void createTask() {
        TaskRequest request = new TaskRequest();
        request.setTaskName("MVC");
        request.setLessonId(7L);
        List<ContentRequest> contents = new ArrayList<>();
        ContentRequest contentRequest = new ContentRequest();
        contentRequest.setContentName("link");
        contentRequest.setContentFormat(ContentFormat.LINK);
        contentRequest.setContentValue("link about interview");
        contents.add(contentRequest);
        request.setContentRequests(contents);

        TaskResponse task = taskService.createTask(request);

        assertNotNull(task);
        assertEquals(task.getTaskName(), request.getTaskName());
        assertEquals(task.getContentResponses().listIterator().next().getContentName(),
                request.getContentRequests().listIterator().next().getContentName());
        assertEquals(task.getContentResponses().listIterator().next().getContentFormat(),
                request.getContentRequests().listIterator().next().getContentFormat());
        assertEquals(task.getContentResponses().listIterator().next().getContentValue(),
                request.getContentRequests().listIterator().next().getContentValue());
    }

    @Test
    void updateTask() {
        TaskRequest request = new TaskRequest();
        request.setTaskName("Migration");
        request.setLessonId(7L);
        List<ContentRequest> contents = new ArrayList<>();
        ContentRequest contentRequest = new ContentRequest();
        contentRequest.setContentName("video");
        contentRequest.setContentFormat(ContentFormat.LINK);
        contentRequest.setContentValue("video about interview");
        contents.add(contentRequest);
        request.setContentRequests(contents);

        Task task = taskRepository.findById(2L).orElseThrow(() -> new NotFoundException("Task not found"));
        TaskResponse response = taskService.updateTask(task.getId(), request);

        assertNotNull(response);
        assertEquals(task.getTaskName(), request.getTaskName());
    }

    @Test
    void deleteById() {
        SimpleResponse simpleResponse = taskService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> taskService.getTaskById(1L)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void getTaskById() {
        TaskResponse task = taskService.getTaskById(2L);

        assertNotNull(task);
        assertEquals(task.getId(), 2L);
    }

//    package kg.peaksoft.peaksoftlmsb6.service;
//
//import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
//import kg.peaksoft.peaksoftlmsb6.dto.request.UpdateStudentRequest;
//import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
//import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
//import kg.peaksoft.peaksoftlmsb6.entity.Student;
//import kg.peaksoft.peaksoftlmsb6.entity.User;
//import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
//import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
//import kg.peaksoft.peaksoftlmsb6.repository.StudentRepository;
//import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.mail.MessagingException;
//import javax.transaction.Transactional;
//import java.util.List;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//class StudentServiceTest {
//
//    @Autowired
//    private StudentService studentService;
//
//    @Autowired
//    private GroupService groupService;
//
//    @Autowired
//    private StudentRepository studentRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void createStudent() throws MessagingException {
//        StudentRequest request = new StudentRequest();
//        request.setFirstName("Mavlyan");
//        request.setLastName("Sadirov");
//        request.setPhoneNumber("0220390009");
//        request.setEmail("mavlyansadirov@gmail.com");
//        request.setStudyFormat(StudyFormat.OFFLINE);
//        request.setGroupId(2L);
//        request.setPassword("Mavlyan07");
//
//        StudentResponse student = studentService.createStudent(request);
//
//        assertNotNull(student);
//        assertEquals(student.getFullName(), request.getFirstName() + " " + request.getLastName());
//        assertEquals(student.getPhoneNumber(), request.getPhoneNumber());
//        assertEquals(student.getStudyFormat(), request.getStudyFormat());
//        assertEquals(student.getEmail(), request.getEmail());
//        assertEquals(student.getGroupName(), groupService.getById(request.getGroupId()).getGroupName());
//    }
//
//    @Test
//    void update() {
//        UpdateStudentRequest request = new UpdateStudentRequest();
//        request.setFullName("Maksat Kairullaev");
//        request.setPhoneNumber("44050304");
//        request.setEmail("max@gmail.com");
//        request.setStudyFormat(StudyFormat.ONLINE);
//        request.setGroupId(2L);
//
//        Student student = studentRepository.findById(2L).orElseThrow(
//                () -> new NotFoundException("Student not found"));
//        StudentResponse response = studentService.update(student.getId(), request);
//
//        assertNotNull(response);
//        assertEquals(response.getFullName(), request.getFullName());
//        assertEquals(response.getPhoneNumber(), request.getPhoneNumber());
//        assertEquals(response.getStudyFormat(), request.getStudyFormat());
//        assertEquals(response.getEmail(), request.getEmail());
//        assertEquals(response.getGroupName(), groupService.getById(request.getGroupId()).getGroupName());
//    }
//
//    @Test
//    void deleteStudent() {
//        SimpleResponse simpleResponse = studentService.deleteStudent(1L);
//
//        assertNotNull(simpleResponse);
//        assertThatThrownBy(() -> studentService.getById(1L)).isInstanceOf(NotFoundException.class)
//                .hasMessageContaining("Студент не найден");
//    }
//
//    @Test
//    void getAllStudent() {
//        List<StudentResponse> students = studentService.getAllStudent(StudyFormat.ALL);
//        List<StudentResponse> allStudent = studentService.getAllStudent(StudyFormat.OFFLINE);
//        List<StudentResponse> student = studentService.getAllStudent(StudyFormat.ONLINE);
//
//        assertEquals(5, students.size());
//        assertEquals(4, allStudent.size());
//        assertEquals(1, student.size());
//    }
//
//    @Test
//    void getById() {
//        StudentResponse student = studentService.getById(1L);
//
//        assertNotNull(student);
//        assertEquals(student.getId(), 1L);
//    }
//
//    @Test
//    void importExcel() {
//    }
//}

}