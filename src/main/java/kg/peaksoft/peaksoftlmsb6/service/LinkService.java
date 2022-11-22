package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LinkRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Link;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LinkService {

    private final LinkRepository linkRepository;

    private final LessonRepository lessonRepository;

    public LinkResponse createLink(LinkRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", request.getLessonId());
                    throw new NotFoundException("Урок не найден");
                });
        Link link = null;
        if(lesson.getLink() == null) {
            link = new Link(request);
            lesson.setLink(link);
            link.setLesson(lesson);
            linkRepository.save(link);
        } else {
            log.error("Lesson already have a link");
            throw new BadRequestException("У урока уже есть ссылка");
        }
        log.info("New lesson successfully saved!");
        return linkRepository.getLink(link.getId());
    }

    public LinkResponse updateLink(Long id, LinkRequest request) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Link with id {} not found", id);
                    throw new NotFoundException("Ссылка не найдена");
                });
        linkRepository.update(
                link.getId(),
                request.getLinkText(),
                request.getLink());
        linkRepository.save(link);
        log.info("Update link with id {} was successfully", id);
        return new LinkResponse(
                link.getId(),
                request.getLinkText(),
                request.getLink());
    }

    public SimpleResponse deleteById(Long id) {
        if (!linkRepository.existsById(id)) {
            log.error("Link with id {} not found", id);
            throw new NotFoundException("Ссылка не найдена");
        }
        linkRepository.deleteLinkById(id);
        log.info("Delete link by id {} was successfully", id);
        return new SimpleResponse("Ссылка удалена");
    }

    public LinkResponse getLinkById(Long id) {
        Link link = linkRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Link with id {} not found", id);
                    throw new NotFoundException("Ссылка не найдена");
                });
        log.info("Get link by id {} was successfully", id);
        return linkRepository.getLink(link.getId());
    }
}
