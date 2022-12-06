package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.VideoRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.VideoResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Video;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class VideoServiceTest {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void saveVideo() {
        VideoRequest request = new VideoRequest();
        request.setLink("blog.link");
        request.setVideoName("video link");
        request.setDescription("video about junit");
        request.setLessonId(7L);

        VideoResponse video = videoService.saveVideo(request);

        assertNotNull(video);
        assertEquals(video.getLink(), request.getLink());
        assertEquals(video.getVideoName(), request.getVideoName());
        assertEquals(video.getDescription(), request.getDescription());
    }

    @Test
    void updateVideo() {
        VideoRequest request = new VideoRequest();
        request.setLink("link.com");
        request.setVideoName("link");
        request.setDescription("video about test");

        Video video = videoRepository.findById(1L).orElseThrow(
                () -> new NotFoundException("Video not found"));
        VideoResponse response = videoService.updateVideo(video.getId(), request);

        assertNotNull(response);
        assertEquals(response.getLink(), request.getLink());
        assertEquals(response.getVideoName(), request.getVideoName());
        assertEquals(response.getDescription(), request.getDescription());
    }

    @Test
    void deleteById() {
        SimpleResponse simpleResponse = videoService.deleteById(1L);

        assertNotNull(simpleResponse);
        assertThatThrownBy(() -> videoService.getById(1L)).isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Видео не найдена");
    }

    @Test
    void getById() {
        VideoResponse response = videoService.getById(1L);

        assertNotNull(response);
        assertEquals(response.getId(), 1L);
    }
}