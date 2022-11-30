package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.AssignGroupRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.AssignInstructorRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.request.DeleteInstructorFromCourseRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.AssignInstructorResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.CourseResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;

    private final InstructorRepository instructorRepository;

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final GroupRepository groupRepository;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course(request);
        courseRepository.save(course);
        log.info("New course successfully saved!");
        return courseRepository.getCourse(course.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        for (Instructor instructor : course.getInstructors()) {
            instructor.getCourses().remove(course);
        }
        for (Group group : course.getGroup()) {
            if (group != null) {
                group.getCourses().remove(course);
            }
        }
        courseRepository.delete(course);
        log.info("Delete course by id {} was successfully", id);
        return new SimpleResponse("Курс удалён");
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        log.info("Get course by id {} was successfully", id);
        return courseRepository.getCourse(course.getId());
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        courseRepository.update(
                course.getId(),
                request.getCourseName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
        log.info("Update course with id {} was successfully", id);
        return new CourseResponse(
                course.getId(),
                request.getCourseName(),
                request.getDescription(),
                request.getDateOfStart(),
                request.getImage());
    }

    public List<StudentResponse> getAllStudentsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        List<StudentResponse> assignStudent = new ArrayList<>();
        for (Group group : course.getGroup()) {
            for (Student student : group.getStudents()) {
                assignStudent.add(courseRepository.getStudentByCourseId(student.getId()));
            }
        }
        log.info("Get all students from course by id {} was successfully", id);
        return assignStudent;
    }

    public List<AssignInstructorResponse> getAllInstructorsFromCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        List<Instructor> instructors = course.getInstructors();
        List<AssignInstructorResponse> assignResponse = new ArrayList<>();
        for (Instructor instructor : instructors) {
            assignResponse.add(courseRepository.getInstructorByCourseId(instructor.getId()));
        }
        log.info("Get all instructor from course by id {} was successfully", id);
        return assignResponse;
    }

    public SimpleResponse assignInstructorToCourse(AssignInstructorRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", request.getCourseId());
                    throw new NotFoundException("Курс не найден");
                });
        for (Long id : request.getInstructorId()) {
            Instructor instructor = instructorRepository.findById(id)
                    .orElseThrow(
                            () -> {
                                log.error("Instructor with id {} not found", request.getInstructorId());
                                throw new NotFoundException("Инструктор не найден");
                            });
            if (course.getInstructors().contains(instructor)) {
                log.error("Instructor has already assigned the course");
                throw new BadRequestException("Инструктор уже назначен на курс");
            } else {
                instructor.addCourse(course);
                course.addInstructor(instructor);
                courseRepository.save(course);
            }
            log.info("Assign instructor {} to course was successfully", instructor.getUser().getEmail());
        }
        return new SimpleResponse("Инструктор назначен на курс");
    }

    public SimpleResponse unassigned(DeleteInstructorFromCourseRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", request.getCourseId());
                    throw new NotFoundException("Курс не найден");
                });
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> {
                    log.error("Instructor with id {} not found", request.getInstructorId());
                    throw new NotFoundException("Инструктор не найден");
                });
        if (course.getInstructors().contains(instructor)) {
            for (Course course1 : instructor.getCourses()) {
                course1.getInstructors().remove(instructor);
            }
            instructor.getCourses().remove(course);
        }
        courseRepository.save(course);

        log.info("Unassigned instructor from course was successfully");
        return new SimpleResponse("Инструктор удален с курса");
    }

    public Deque<CourseResponse> getAllCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> {
                    log.error("User with email {} not found", user.getEmail());
                    throw new NotFoundException("Пользователь с почтой не найден");
                });
        Deque<CourseResponse> courseResponses = new ArrayDeque<>();
        switch (user1.getRole().getAuthority()) {
            case "ADMIN":
                return courseRepository.getAllCourses();
            case "STUDENT":
                Student student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                        () -> {
                            log.error("Student with email {} not found", user1.getEmail());
                            throw new NotFoundException("Студент с почтой не найден");
                        });
                for (Course course : student.getGroup().getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
            case "INSTRUCTOR":
                Instructor instructor = instructorRepository.findByUserId(user.getId()).orElseThrow(
                        () -> {
                            log.error("Instructor with id {} not found", user1.getId());
                            throw new NotFoundException("Инструктор не найден");
                        });
                for (Course course : instructor.getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
        }
        log.info("Get all courses was successfully");
        return courseResponses;
    }

    public SimpleResponse assignGroupToCourse(AssignGroupRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", request.getCourseId());
                    throw new NotFoundException("Курс не найден");
                });
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", request.getGroupId());
                    throw new NotFoundException("Группа не найдена");
                });
        if (course.getGroup().contains(group)) {
            throw new BadRequestException("Группа уже назначена на курс");
        }
        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        log.info("Assign group to course was successfully");
        return new SimpleResponse("Группа назначена на курс");
    }

    public SimpleResponse deleteGroupFromCourse(AssignGroupRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", request.getCourseId());
                    throw new NotFoundException("Курс не найден");
                });
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(
                () -> {
                    log.error("Group with id {} not found", request.getGroupId());
                    throw new NotFoundException("Группа не найдена");
                });
        if (course.getGroup().contains(group)) {
            for (Course course1 : group.getCourses()) {
                course1.getGroup().remove(group);
            }
            group.getCourses().remove(course);
            groupRepository.save(group);
            log.info("Delete group from course by id {} was successfully", request.getCourseId());
        }
        return new SimpleResponse("Группа удалена с курса");
    }
}