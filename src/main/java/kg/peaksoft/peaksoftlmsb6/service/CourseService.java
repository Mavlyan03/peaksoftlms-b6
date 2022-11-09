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
    private final LessonRepository lessonRepository;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final LinkRepository linkRepository;
    private final PresentationRepository presentationRepository;
    private final VideoRepository videoRepository;
    private final ContentRepository contentRepository;
    private final ResultRepository resultRepository;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course(request);
        courseRepository.save(course);
        return courseRepository.getCourse(course.getId());
    }

    public SimpleResponse deleteById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Course not found",id)));
        for (Instructor instructor : course.getInstructors()) {
            instructor.getCourses().remove(course);
        }
        for (Group group : course.getGroup()) {
            if(group != null) {
                group.getCourses().remove(course);
            }
        }
        for(Lesson lesson : course.getLessons()) {
            linkRepository.deleteById(lesson.getLink().getId());
            videoRepository.deleteById(lesson.getVideo().getId());
            presentationRepository.deleteById(lesson.getPresentation().getId());
            Test test = lesson.getTest();
            test.setLesson(null);
            lesson.setCourse(null);
            testRepository.deleteById(test.getId());
            Task task = lesson.getTask();
            task.setLesson(null);
            lesson.setTask(null);
            for(Content content : task.getContents()) {
                contentRepository.deleteById(content.getId());
            }
            Results results = resultRepository.findResultByTestId(test.getId());
            results.setStudent(null);
            results.setTest(null);
            resultRepository.deleteById(results.getId());
            taskRepository.deleteById(task.getId());
            lessonRepository.deleteLessonById(lesson.getId());
        }
        courseRepository.delete(course);
        return new SimpleResponse("Course deleted");
    }

    public CourseResponse getById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Курс с id = %s не найден",id)));
        return courseRepository.getCourse(course.getId());
    }

    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Course not found",id)));
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
                () -> new NotFoundException(String.format("Course not found",id)));
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
                () -> new NotFoundException(String.format("Instructor not found",request.getInstructorId())));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException(String.format("Course not found",request.getCourseId())));
        instructor.addCourse(course);
        course.addInstructor(instructor);
        courseRepository.save(course);
        return new SimpleResponse("Instructor assigned to course");
    }

    public SimpleResponse unassigned(AssignInstructorRequest request) {
        Instructor instructor = instructorRepository.findById(request.getInstructorId()).orElseThrow(
                () -> new NotFoundException(String.format("Instructor not found",request.getInstructorId())));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException(String.format("Course not found",request.getCourseId())));
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

    public Deque<CourseResponse> getAllCourses(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        User user1 = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new NotFoundException("User with email %s not found"));
        Deque<CourseResponse> courseResponses = new ArrayDeque<>();
        switch (user1.getRole().getAuthority()) {
            case "ADMIN":
                return courseRepository.getAllCourses();
            case "STUDENT":
                Student student = studentRepository.findByUserId(user1.getId()).orElseThrow(
                        () -> new NotFoundException(String.format("Student with email =%s not found",user1.getId())));
                for (Course course : student.getGroup().getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
            case "INSTRUCTOR":
                Instructor instructor = instructorRepository.findByUserId(user1.getId())
                        .orElseThrow(() -> new NotFoundException(String.format("Instructor with id =%s not found", user1.getId())));
                for (Course course : instructor.getCourses()) {
                    courseResponses.addFirst(courseRepository.getCourse(course.getId()));
                }
                break;
        }
        return courseResponses;
    }

    public SimpleResponse assignGroupToCourse(AssignGroupRequest request) {
        Group group = groupRepository.findById(request.getGroupId()).orElseThrow(
                () -> new NotFoundException(String.format("Group with id =%s not found",request.getGroupId())));
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new NotFoundException(String.format("Course with id =%s not found",request.getCourseId())));
        group.addCourse(course);
        course.addGroup(group);
        courseRepository.save(course);
        return new SimpleResponse("Group assigned to course was successfully");
    }

    public SimpleResponse deleteGroupFromCourse(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Group with id =%s not found",id)));
        for(Course course : group.getCourses()) {
            course.getGroup().remove(group);
        }
        group.setCourses(null);
        groupRepository.save(group);
        return new SimpleResponse("Group deleted from course successfully");
    }
}