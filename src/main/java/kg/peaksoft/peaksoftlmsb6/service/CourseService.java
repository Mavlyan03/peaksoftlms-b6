package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course(request);
        courseRepository.save(course);
        return courseRepository.getCourse(course.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
        for (Instructor instructor : course.getInstructors()) {
            instructor.getCourses().remove(course);
        }
        for (Group group : course.getGroup()) {
            if(group != null) {
                group.getCourses().remove(course);
            }
        }
        courseRepository.delete(course);
        return new SimpleResponse("Курс удалён");
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
        return courseRepository.getCourse(course.getId());
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
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
                () -> new NotFoundException("Курс не найден"));
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
                () -> new NotFoundException("Курс не найден"));
        List<Instructor> instructors = course.getInstructors();
        List<AssignInstructorResponse> assignResponse = new ArrayList<>();
        for (Instructor instructor : instructors) {
            assignResponse.add(courseRepository.getInstructorByCourseId(instructor.getId()));
        }
        return assignResponse;
    }

    public SimpleResponse assignInstructorToCourse(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Инструктор не найден"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
        instructor.addCourse(course);
        course.addInstructor(instructor);
        courseRepository.save(course);
        return new SimpleResponse("Инструктор назначен на курс");
    }

    public SimpleResponse unassigned(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException("Инструктор не найден"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
        for (Instructor instructor1 : course.getInstructors()) {
            instructor1.getCourses().remove(course);
        }
        for (Course course1 : instructor.getCourses()) {
            course1.getInstructors().remove(instructor);
        }
        courseRepository.save(course);
        instructorRepository.save(instructor);
        return new SimpleResponse("Инструктор удален с курса");
    }

    public Deque<CourseResponse> getAllCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new NotFoundException("Пользователь с почтой не найден"));
        Deque<CourseResponse> courseResponses = new ArrayDeque<>();
        switch (user1.getRole().getAuthority()) {
            case "ADMIN":
                return courseRepository.getAllCourses();
            case "STUDENT":
                Student student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                        () -> new NotFoundException("Студент с почтой не найден"));
                for (Course course : student.getGroup().getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
            case "INSTRUCTOR":
                Instructor instructor = instructorRepository.findByUserId(user1.getId())
                        .orElseThrow(() -> new NotFoundException("Инструктор не найден"));
                for (Course course : instructor.getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
        }
        return courseResponses;
    }

    public SimpleResponse assignGroupToCourse(AssignGroupRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new NotFoundException("Группа не найдена"));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException("Курс не найден"));
        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        return new SimpleResponse("Группа назначена на курс");
    }

    public SimpleResponse deleteGroupFromCourse(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Группа не найдена"));
        for(Course course : group.getCourses()) {
            course.getGroup().remove(group);
        }
        group.setCourses(null);
        groupRepository.save(group);
        return new SimpleResponse("Группа удалена с курса");
    }
}