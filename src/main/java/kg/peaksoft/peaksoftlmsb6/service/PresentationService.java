package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PresentationRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Presentation;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PresentationService {

    private final PresentationRepository presentationRepository;

    private final LessonRepository lessonRepository;

    public PresentationResponse savePresentation(PresentationRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", request.getLessonId());
                    throw new NotFoundException("Урок не найден");
                });
        Presentation presentation = new Presentation(request);
        lesson.setPresentation(presentation);
        presentation.setLesson(lesson);
        presentationRepository.save(presentation);
        log.info("New presentation successfully saved!");
        return presentationRepository.getPresentation(presentation.getId());
    }

    public PresentationResponse updatePresentation(Long id, PresentationRequest request) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Presentation with id {} not found", id);
                    throw new NotFoundException("Презентация не найдена");
                });
        presentationRepository.update(id,
                request.getPresentationName(),
                request.getDescription(),
                request.getPresentationLink());
        log.info("Update presentation with id {} was successfully", id);
        return new PresentationResponse(
                presentation.getId(),
                request.getPresentationName(),
                request.getDescription(),
                request.getPresentationLink());
    }

    public SimpleResponse deleteById(Long id) {
        if (!presentationRepository.existsById(id)) {
            log.error("Presentation with id {} not found", id);
            throw new NotFoundException("Презентация не найдена");
        }
        presentationRepository.deletePresentationById(id);
        log.info("Delete presentation by id {} was successfully", id);
        return new SimpleResponse("Презентация удалена");
    }

    public PresentationResponse getById(Long id) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Presentation with id {} not found", id);
                    throw new NotFoundException("Презентация не найдена");
                });
        log.info("Get presentation by id {} was successfully", id);
        return presentationRepository.getPresentation(presentation.getId());
    }
}
