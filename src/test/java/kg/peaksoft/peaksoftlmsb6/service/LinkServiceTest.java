package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.LinkRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.LinkResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Link;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LinkServiceTest {

    @Autowired
    private LinkService linkService;

    @Autowired
    private LinkRepository linkRepository;


    @Test
    void createLink() {
        LinkRequest request = new LinkRequest();
        request.setLink("linkedin");
        request.setLinkText("hello world");
        request.setLessonId(7L);

        LinkResponse link = linkService.createLink(request);

        assertNotNull(link);
        assertEquals(link.getLink(), request.getLink());
        assertEquals(link.getLinkText(), request.getLinkText());
    }

    @Test
    void updateLink() {
        LinkRequest request = new LinkRequest();
        request.setLink("washington post");
        request.setLinkText("link about a news");

        Link link = linkRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Link not found"));
        LinkResponse response = linkService.updateLink(link.getId(), request);

        assertNotNull(response);
        assertEquals(response.getLink(), request.getLink());
        assertEquals(response.getLinkText(), request.getLinkText());
    }

    @Test
    void deleteById() {
        SimpleResponse simpleResponse = linkService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> linkService.getLinkById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Ссылка не найдена");
    }

    @Test
    void getLinkById() {
        LinkResponse response = linkService.getLinkById(1L);

        assertNotNull(response);
        assertEquals(response.getId(), 1L);
    }
}