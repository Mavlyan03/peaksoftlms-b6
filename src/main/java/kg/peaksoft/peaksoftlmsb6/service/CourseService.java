package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course(request);
        courseRepository.save(course);
        return courseRepository.getCourse(course.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        for (Instructor instructor : course.getInstructors()) {
            instructor.getCourses().remove(course);
        }
        for (Group group : course.getGroup()) {
            group.getCourses().remove(course);
        }
        courseRepository.delete(course);
        return new SimpleResponse("Course deleted");
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        return courseRepository.getCourse(course.getId());
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        courseRepository.update(
                course.getId(),
                request.getCourseName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
        return new CourseResponse(
                course.getId(),
                request.getCourseName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
    }

    public List<StudentResponse> getAllStudentsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Course not found"));
        List<StudentResponse> assignStudent = new ArrayList<>();
        for (Group group : course.getGroup()) {
            for (Student student : group.getStudents()) {
                assignStudent.add(courseRepository.getStudentByCourseId(student.getId()));
            }
        }
        return assignStudent;
    }

    public List<AssignInstructorResponse> getAllInstructorsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("course with id %s not found",id)));
        List<Instructor> instructors = course.getInstructors();
        List<AssignInstructorResponse> assignResponse = new ArrayList<>();
        for (Instructor instructor : instructors) {
            assignResponse.add(courseRepository.getInstructorByCourseId(instructor.getId()));
        }
        return assignResponse;
    }

    public SimpleResponse assignInstructorToCourse(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Instructor not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));
        instructor.addCourse(course);
        course.addInstructor(instructor);
        courseRepository.save(course);
        return new SimpleResponse("Instructor assigned to course");
    }

    public SimpleResponse unassigned(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Instructor not found"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Course not found"));
        for (Instructor instructor1 : course.getInstructors()) {
            instructor1.getCourses().remove(course);
        }
        for (Course course1 : instructor.getCourses()) {
            course1.getInstructors().remove(instructor);
        }
        courseRepository.save(course);
        instructorRepository.save(instructor);
        return new SimpleResponse("Instructor unassigned from course");
    }

    public List<CourseResponse> getAllCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        List<CourseResponse> courseResponses = new ArrayList<>();
        switch (user1.getRole().getAuthority()) {
            case "ADMIN":
                List<Course> courses = courseRepository.findAll();
                for (Course course : courses) {
                    courseResponses.add(courseRepository.getCourse(course.getId()));
                }
                break;
            case "STUDENT":
                Student student = studentRepository.findByEmail(user1.getEmail()).orElseThrow(
                        () -> new NotFoundException("Student not found"));
                for (Course course : student.getGroup().getCourses()) {
                    courseResponses.add(courseRepository.getCourse(course.getId()));
                }
                break;
            case "INSTRUCTOR":
                Instructor instructor = instructorRepository.findByUserId(user1.getId())
                        .orElseThrow(() -> new NotFoundException("Instructor not found"));
                for (Course course : instructor.getCourses()) {
                    courseResponses.add(courseRepository.getCourse(course.getId()));
                }
                break;
        }
        return courseResponses;
    }

}