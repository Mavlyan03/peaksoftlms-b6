package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LessonRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LessonResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.*;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final TestRepository  testRepository;
    private final TaskRepository taskRepository;
    private final ContentRepository contentRepository;
    private final ResultsRepository resultsRepository;


    public SimpleResponse createLesson(LessonRequest request){
            Course course = courseRepository.findById(request.getCourseId()).orElseThrow(()-> new NotFoundException("Course not found"));
            Lesson lesson = new Lesson(request);
            lesson.setCourse(course);
            lessonRepository.save(lesson);
            return new SimpleResponse("Lesson saved");
    }

    public LessonResponse updateLesson(Long id, LessonRequest request){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Lesson with id =%s not found", id)));
        lessonRepository.update(lesson.getId(),
                request.getLessonName());
        lessonRepository.save(lesson);
        return convertToResponse(lesson);
    }

    public SimpleResponse deleteLesson(Long id){

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Lesson with id =%s not found", id)));
        if (lesson.getTest() != null) {
            Test test = testRepository.findById(lesson.getTest().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Test with id = %s not found", lesson.getTest().getId())));
            testRepository.delete(test);
        }

        if (lesson.getTask() != null) {
            Task task = taskRepository.findById(lesson.getTask().getId())
                .orElseThrow(() -> new NotFoundException(String.format("Test with id = %s not found", lesson.getTask().getId())));
            taskRepository.delete(task);
        }

        if (lesson.getTask() != null) {
            Content content = contentRepository.findById(lesson.getTask().getId())
                .orElseThrow(() -> new NotFoundException("Content with id = $s not found"));
            contentRepository.delete(content);
        }

        if (lesson.getTest() != null) {
            Results results = resultsRepository.findById(lesson.getTest().getId())
                .orElseThrow(() -> new NotFoundException("Results with id = %s not found"));
            resultsRepository.delete(results);
        }
        lessonRepository.delete(lesson);
        return new SimpleResponse("lesson deleted");
    }

    public List<LessonResponse> getAllLessonsByCourseId(Long id){
        Course course = courseRepository.findById(id).orElseThrow(()-> new NotFoundException("Course not found"));
        List<LessonResponse> lessonResponses = new ArrayList<>();
        for (Lesson lesson : course.getLessons()) {
            lessonResponses.add(convertToResponse(lesson));
        }
        return lessonResponses;
    }

    public LessonResponse convertToResponse(Lesson lesson){
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setLessonId(lesson.getId());
        lessonResponse.setLessonName(lesson.getLessonName());


        if (lesson.getVideo() != null) {
            lessonResponse.setVideoId(lesson.getVideo().getId());
        }else lessonResponse.setVideoId(null);


        if (lesson.getPresentation() != null) {
            lessonResponse.setPresentationId(lesson.getPresentation().getId());
        } else lessonResponse.setPresentationId(null);


        if (lesson.getLink() != null) {
            lessonResponse.setLinkId(lesson.getLink().getId());
        }else lessonResponse.setLinkId(null);


        if (lesson.getTest() != null) {
            lessonResponse.setTestId(lesson.getTest().getId());
        } else lessonResponse.setTestId(null);


        if (lesson.getTask() != null) {
            lessonResponse.setTaskId(lesson.getTask().getId());
        } else lessonResponse.setTaskId(null);
        return lessonResponse;
    }
}
