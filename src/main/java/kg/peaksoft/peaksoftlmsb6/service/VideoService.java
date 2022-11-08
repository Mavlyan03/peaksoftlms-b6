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
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final LessonRepository lessonRepository;

    public VideoResponse saveVideo(VideoRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new NotFoundException(String.format("Lesson with id =%s not found", request.getLessonId())));
        Video video = null;
        if(lesson.getVideo() == null) {
            video = new Video(request);
            lesson.setVideo(video);
            video.setLesson(lesson);
        } else {
            throw new BadRequestException("У урока уже видео");
        }
        videoRepository.save(video);
        return videoRepository.getVideo(video.getId());
    }

    public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Video with id =%s not found", id)));
        videoRepository.update(id,
                request.getVideoName(),
                request.getDescription(),
                request.getLink());
        return new VideoResponse(
                video.getId(),
                request.getVideoName(),
                request.getDescription(),
                request.getLink());
    }

    public SimpleResponse deleteById(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new NotFoundException(String.format("Video with id =%s not found",id));
        }
        videoRepository.deleteVideoById(id);
        return new SimpleResponse("Video deleted");
    }

    public VideoResponse getVideoById(Long id) {
        Video video = videoRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Video with id =%s not found", id)));
        return videoRepository.getVideo(video.getId());
    }
}
