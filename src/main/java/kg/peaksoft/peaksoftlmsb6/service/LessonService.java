package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LessonRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LessonResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LessonService {

    private final LessonRepository lessonRepository;

    private final CourseRepository courseRepository;

    private final TestRepository testRepository;

    private final TaskRepository taskRepository;

    private final ContentRepository contentRepository;

    private final ResultsRepository resultsRepository;


    public SimpleResponse createLesson(LessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", request.getCourseId());
                    throw new NotFoundException("Курс не найден");
                });
        Lesson lesson = new Lesson(request);
        lesson.setCourse(course);
        lessonRepository.save(lesson);
        log.info("Save a new lesson by request was successfully");
        return new SimpleResponse("Урок сохранён");
    }

    public LessonResponse updateLesson(Long id, LessonRequest request) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", id);
                    throw new NotFoundException("Урок не найден");
                });
        lesson.setLessonName(request.getLessonName());
        log.info("Update lesson with id {} was successfully", id);
        return new LessonResponse(lesson);
    }

    public SimpleResponse deleteLesson(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Урок не найден"));
        if (lesson.getTest() != null) {
            Test test = testRepository.findById(lesson.getTest().getId())
                    .orElseThrow(() -> new NotFoundException("Тест не найден"));
            Results results = resultsRepository.findById(lesson.getTest().getId())
                    .orElseThrow(() -> new NotFoundException("Результат не найден"));
            resultsRepository.delete(results);
            testRepository.delete(test);
        }

        if (lesson.getTask() != null) {
            Task task = taskRepository.findById(lesson.getTask().getId())
                    .orElseThrow(() -> new NotFoundException("Задача не найдена"));
            Content content = contentRepository.findById(lesson.getTask().getId())
                    .orElseThrow(() -> new NotFoundException("Контент не найден"));
            contentRepository.delete(content);
            taskRepository.delete(task);
        }

        lessonRepository.delete(lesson);
        log.info("Delete lesson by id {} was successfully", id);
        return new SimpleResponse("Урок удалён");
    }

    public List<LessonResponse> getAllLessonsByCourseId(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Course with id {} not found", id);
                    throw new NotFoundException("Курс не найден");
                });
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            lessonResponses.add(new LessonResponse(lesson));
        }
        log.info("Get all lessons by course id {} was successfully", id);
        return lessonResponses;
    }

    public LessonResponse getById(Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", id);
                    throw new NotFoundException("Урок не найден");
                });
        log.info("Get lesson by id {} was successfully", id);
        return new LessonResponse(lesson);
    }
}