package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

//    @Test
//    @DisplayName("Should throw an exception when the course is invalid")
//    void createCourseWhenCourseIsInvalidThenThrowException() {
//        CourseRequest request = new CourseRequest();
//        request.setCourseName("");
//        request.setDescription("");
//        request.setDateOfStart(LocalDate.now());
//        request.setImage("");
//
//        assertThrows(ConstraintViolationException.class, () -> courseService.createCourse(request));
//    }

    @Test
    @DisplayName("Should save the course when the course is valid")
    void createCourseWhenCourseIsValid() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Java");
        request.setDescription("Java course");
        request.setDateOfStart(LocalDate.now());
        request.setImage("image");

        CourseResponse response = courseService.createCourse(request);

        assertNotNull(response);
        assertEquals(request.getCourseName(), response.getCourseName());
        assertEquals(request.getDescription(), response.getDescription());
        assertEquals(request.getDateOfStart(), response.getDateOfStart());
        assertEquals(request.getImage(), response.getImage());
    }
}