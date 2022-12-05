package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.DeleteInstructorFromCourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.GroupRepository;
import kg.peaksoft.peaksoftlmsb6.repository.InstructorRepository;
import kg.peaksoft.peaksoftlmsb6.repository.UserRepository;
import kg.peaksoft.peaksoftlmsb6.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Deque;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class CourseServiceTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CourseService courseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
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
        SimpleResponse simpleResponse = courseService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> courseService.getById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Курс не найден");
    }

    @Test
    void getById() {
        CourseResponse course = courseService.getById(1L);

        assertNotNull(course);
        assertEquals(course.getId(), 1L);
    }

    void getAllCourses() {

    }

    @Test
    void getAllStudentsFromCourse() {
        List<StudentResponse> students = courseService.getAllStudentsFromCourse(1L);

        assertEquals(5, students.size());
    }

    @Test
    void testGetAllInstructorsFromCourse() {
        List<AssignInstructorResponse> instructors = courseService.getAllInstructorsFromCourse(1L);

        assertEquals(1, instructors.size());
    }

    @Test
    void testAssignInstructorToCourse() {
        AssignInstructorRequest request = new AssignInstructorRequest();
        request.setInstructorId(List.of(2L));
        request.setCourseId(2L);

        SimpleResponse result = courseService.assignInstructorToCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getInstructors().contains(instructorRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Instructor not found")))).isTrue();
    }

    @Test
    void testAssignGroupToCourse() {
        AssignGroupRequest request = new AssignGroupRequest();
        request.setGroupId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.assignGroupToCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getGroup().contains(groupRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Group not found")))).isTrue();

    }

    @Test
    void testUnassigned() {
        DeleteInstructorFromCourseRequest request = new DeleteInstructorFromCourseRequest();
        request.setInstructorId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.unassigned(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getInstructors().contains(instructorRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Instructor not found")))).isFalse();
    }

    @Test
    void testDeleteGroupFromCourse() {
        AssignGroupRequest request = new AssignGroupRequest();
        request.setGroupId(2L);
        request.setCourseId(2L);

        SimpleResponse result = courseService.deleteGroupFromCourse(request);

        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));

        assertNotNull(result);
        assertThat(course.getGroup().contains(groupRepository.findById(2L)
                .orElseThrow(() -> new NotFoundException("Group not found")))).isFalse();
    }

    @Test
    void testGetAllCourses() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@gmail.com");
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        Deque<CourseResponse> courses = courseService.getAllCourses(authentication);

        assertNotNull(courses);
        assertEquals(2, courses.size());
    }

}