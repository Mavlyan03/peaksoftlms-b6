package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LinkRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Link;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LinkService {

    private final LinkRepository linkRepository;
    private final LessonRepository lessonRepository;

    public LinkResponse createLink(LinkRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Урок не найден",request.getLessonId())));
        Link link = new Link(request);
        lesson.setLink(link);
        link.setLesson(lesson);
        linkRepository.save(link);
        return linkRepository.getLink(link.getId());
    }

    public LinkResponse updateLink(Long id, LinkRequest request) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Ссылка не найдена",id)));
        linkRepository.update(
                link.getId(),
                request.getLinkText(),
                request.getLink());
        linkRepository.save(link);
        return new LinkResponse(
                link.getId(),
                request.getLinkText(),
                request.getLink());
    }

    public SimpleResponse deleteById(Long id) {
        if(!linkRepository.existsById(id)) {
            throw new NotFoundException(String.format("Ссылка не найдена",id));
        }
        linkRepository.deleteLinkById(id);
        return new SimpleResponse("Ссылка удалена");
    }

    public LinkResponse getLinkById(Long id) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Ссылка не найдена",id)));
        return linkRepository.getLink(link.getId());
    }
}
