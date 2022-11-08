package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PresentationRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Presentation;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.PresentationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PresentationService {

    private final PresentationRepository presentationRepository;

    private final LessonRepository lessonRepository;

    public PresentationResponse savePresentation(PresentationRequest request){
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id =%s not found",request.getLessonId())));
        Presentation presentation = null;
        if(lesson.getPresentation() == null) {
            presentation = new Presentation(request);
            lesson.setPresentation(presentation);
            presentation.setLesson(lesson);
        } else {
            throw new BadRequestException("У урока уже есть презентация");
        }
        presentationRepository.save(presentation);
        return presentationRepository.getPresentation(presentation.getId());
    }

    public PresentationResponse updatePresentation(Long id, PresentationRequest request) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Presentation with id =%s not found",id)));
        presentationRepository.update(id,
                request.getPresentationName(),
                request.getDescription(),
                request.getPresentationLink());
        return new PresentationResponse(
                presentation.getId(),
                request.getPresentationName(),
                request.getDescription(),
                request.getPresentationLink());
    }

    public SimpleResponse deleteById(Long id) {
        if (!presentationRepository.existsById(id)) {
            throw new NotFoundException(String.format("Presentation with id =%s not found",id));
        }
        presentationRepository.deletePresentationById(id);
        return new SimpleResponse("Presentation deleted");
    }

    public PresentationResponse getPresentationById(Long id) {
        Presentation presentation = presentationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Presentation with id =%s not found",id)));
        return presentationRepository.getPresentation(presentation.getId());
    }
}
