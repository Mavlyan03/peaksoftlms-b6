package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.PresentationRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.PresentationResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Presentation;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.PresentationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PresentationServiceTest {

    @Autowired
    private PresentationService presentationService;

    @Autowired
    private PresentationRepository presentationRepository;

    @Test
    void createPresentation() {
        PresentationRequest request = new PresentationRequest();
        request.setPresentationName("interview");
        request.setPresentationLink("inter.com");
        request.setDescription("presentation about interview");
        request.setLessonId(7L);

        PresentationResponse presentation = presentationService.createPresentation(request);

        assertNotNull(presentation);
        assertEquals(presentation.getPresentationName(), request.getPresentationName());
        assertEquals(presentation.getPresentationLink(), request.getPresentationLink());
        assertEquals(presentation.getDescription(), request.getDescription());
    }

    @Test
    void updatePresentation() {
        PresentationRequest request = new PresentationRequest();
        request.setPresentationName("interview");
        request.setPresentationLink("inter.com");
        request.setDescription("presentation about interview");
        request.setLessonId(6L);

        Presentation presentation = presentationRepository.findById(3L).orElseThrow(
                () -> new NotFoundException("Presentation not found"));
        PresentationResponse response = presentationService.updatePresentation(presentation.getId(), request);

        assertNotNull(response);
        assertEquals(response.getPresentationName(), request.getPresentationName());
        assertEquals(response.getPresentationLink(), request.getPresentationLink());
        assertEquals(response.getDescription(), request.getDescription());
    }

    @Test
    void deleteById() {
        PresentationRequest request = new PresentationRequest();
        request.setPresentationName("interview");
        request.setPresentationLink("inter.com");
        request.setDescription("presentation about interview");
        request.setLessonId(7L);

        PresentationResponse presentation = presentationService.createPresentation(request);

        presentationRepository.deleteById(presentation.getId());
        assertThatThrownBy(() -> presentationService.getById(presentation.getId())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getById() {
        PresentationResponse response = presentationService.getById(3L);

        assertNotNull(response);
        assertEquals(response.getId(), 3L);
    }
}