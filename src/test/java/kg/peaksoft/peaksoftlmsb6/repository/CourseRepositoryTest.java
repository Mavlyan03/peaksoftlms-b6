package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CourseRepositoryTest {

    private CourseRepository courseRepository;

    @Test
    void update() {
    }

    @Test
    void getInstructorByCourseId() {
    }

    @Test
    void getCourse() {
    }

    @Test
    void getStudentByCourseId() {
    }

    @Test
    void getAllCourses() {
        ArrayDeque<CourseResponse> courses = courseRepository.getAllCourses();

    }
}