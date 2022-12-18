package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LessonRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LessonResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Course;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.CourseRepository;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class LessonServiceTest {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void createLesson() {
        LessonRequest request = new LessonRequest();
        request.setLessonName("Mockito");
        request.setCourseId(1L);

        SimpleResponse lesson = lessonService.createLesson(request);

        assertNotNull(lesson);
        assertEquals(request.getLessonName(), "Mockito");
        assertEquals(request.getCourseId(), 1L);
    }

    @Test
    void updateLesson() {
        LessonRequest request = new LessonRequest();
        request.setLessonName("Selenium");
        request.setCourseId(2L);

        Lesson lesson = lessonRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Lesson not found"));
        LessonResponse response = lessonService.updateLesson(lesson.getId(), request);

        assertNotNull(response);
        assertEquals(response.getLessonName(), request.getLessonName());
        assertEquals(response.getLinkId(), lesson.getLink().getId());
        assertEquals(response.getVideoId(), lesson.getVideo().getId());
        assertEquals(response.getTaskId(), lesson.getTask().getId());
        assertEquals(response.getTestId(), lesson.getTest().getId());
        assertEquals(response.getPresentationId(), lesson.getPresentation().getId());
    }

    @Test
    void deleteLesson() {
        SimpleResponse simpleResponse = lessonService.deleteLesson(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> lessonService.getById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Урок не найден");
    }

    @Test
    void getAllLessonsByCourseId() {
        Course course = courseRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Course not found"));

        List<LessonResponse> lessons = lessonService.getAllLessonsByCourseId(course.getId());

        assertEquals(3, lessons.size());
    }

    @Test
    void getById() {
        LessonResponse lesson = lessonService.getById(1L);

        assertNotNull(lesson);
        assertEquals(lesson.getLessonId(), 1L);
    }
}