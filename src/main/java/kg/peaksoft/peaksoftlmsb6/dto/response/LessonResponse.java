package kg.peaksoft.peaksoftlmsb6.dto.response;

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

//    public LessonResponse(Lesson lesson) {
//        this.lessonId = lesson.getId();
//        this.lessonName = lesson.getLessonName();
//        this.videoId = lesson.getVideo().getId();
//        this.presentationId = lesson.getPresentation().getId();
//        this.linkId = lesson.getLink().getId();
//        this.taskId = lesson.getTask().getId();
//        this.testId = lesson.getTest().getId();
//    }
}
