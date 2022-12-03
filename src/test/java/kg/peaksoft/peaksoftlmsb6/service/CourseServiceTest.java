package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseService courseService;

    @Test
    void createCourse() {
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

    @Test
    void updateCourse() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Python");
        request.setDescription("light language");
        request.setDateOfStart(LocalDate.of(2007, 3, 22));
        request.setImage("habr.com");

        Course course = courseRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Course not found"));
        CourseResponse response = courseService.updateCourse(course.getId(), request);

        assertNotNull(response);
        assertEquals(response.getCourseName(), request.getCourseName());
        assertEquals(response.getDescription(), request.getDescription());
        assertEquals(response.getDateOfStart(),request.getDateOfStart());
        assertEquals(response.getImage(),  request.getImage());
    }

    @Test
    void deleteCourseById() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Java");
        request.setDescription("Java course");
        request.setDateOfStart(LocalDate.now());
        request.setImage("image");

        CourseResponse course = courseService.createCourse(request);

        courseRepository.deleteById(course.getId());
        assertThatThrownBy(() -> courseService.getById(course.getId())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById() {
        CourseResponse course = courseService.getById(1L);

        assertNotNull(course);
        assertEquals(course.getId(), 1L);
    }

    void getAllCourses() {
        CourseRequest request = new CourseRequest();
        request.setCourseName("Java");
        request.setDescription("Java course");
        request.setDateOfStart(LocalDate.now());
        request.setImage("image");

        courseService.createCourse(request);

        CourseRequest request1 = new CourseRequest();
        request1.setCourseName("Java");
        request1.setDescription("Java course");
        request1.setDateOfStart(LocalDate.now());
        request1.setImage("image");

        courseService.createCourse(request);
    }
}