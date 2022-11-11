package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {
    private Long lessonId;
    private String lessonName;
    private Long videoId;
    private Long presentationId;
    private Long linkId;
    private Long taskId;
    private Long testId;

    public LessonResponse(Lesson lesson) {
        this.lessonId = lesson.getId();
        this.lessonName = lesson.getLessonName();
        if (lesson.getVideo() != null) {
            setVideoId(lesson.getVideo().getId());
        } else setVideoId(null);

        if (lesson.getPresentation() != null) {
            setPresentationId(lesson.getPresentation().getId());
        } else setPresentationId(null);

        if (lesson.getLink() != null) {
            setLinkId(lesson.getLink().getId());
        } else setLinkId(null);

        if (lesson.getTest() != null) {
            setTestId(lesson.getTest().getId());
        } else setTestId(null);

        if (lesson.getTask() != null) {
            setTaskId(lesson.getTask().getId());
        } else setTaskId(null);
    }
}
