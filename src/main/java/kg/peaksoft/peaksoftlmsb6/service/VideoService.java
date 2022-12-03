package kg.peaksoft.peaksoftlmsb6.service;

import kg.peaksoft.peaksoftlmsb6.dto.request.VideoRequest;
import kg.peaksoft.peaksoftlmsb6.dto.response.SimpleResponse;
import kg.peaksoft.peaksoftlmsb6.dto.response.VideoResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import kg.peaksoft.peaksoftlmsb6.entity.Video;
import kg.peaksoft.peaksoftlmsb6.exception.BadRequestException;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import kg.peaksoft.peaksoftlmsb6.repository.LessonRepository;
import kg.peaksoft.peaksoftlmsb6.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final VideoRepository videoRepository;

    private final LessonRepository lessonRepository;

    public VideoResponse createVideo(VideoRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> {
                    log.error("Lesson with id {} not found", request.getLessonId());
                    throw new NotFoundException("Урок не найден");
                });
        Video video = null;
        if(lesson.getVideo() == null) {
            video = new Video(request);
            lesson.setVideo(video);
            video.setLesson(lesson);
            videoRepository.save(video);
        } else {
            log.error("Lesson already have a video");
            throw new BadRequestException("У урока уже есть видео");
        }
        log.info("New video successfully saved!");
        return videoRepository.getVideo(video.getId());
    }

    public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Video with id {} not found", id);
                    throw new NotFoundException("Видео не найдена");
                });
        videoRepository.update(id,
                request.getVideoName(),
                request.getDescription(),
                request.getLink());
        log.info("Update video with id {} was successfully", id);
        return new VideoResponse(
                video.getId(),
                request.getVideoName(),
                request.getDescription(),
                request.getLink());
    }

    public SimpleResponse deleteById(Long id) {
        if (!videoRepository.existsById(id)) {
            log.error("Video with id {} not found", id);
            throw new NotFoundException("Видео не найдена");
        }
        videoRepository.deleteVideoById(id);
        log.info("Delete video by id {} was successfully", id);
        return new SimpleResponse("Видео удалено");
    }

    public VideoResponse getById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Video with id {} not found", id);
                    throw new NotFoundException("Видео не найдена");
                });
        log.info("Get video by id {} was successfully", id);
        return videoRepository.getVideo(video.getId());
    }
}
