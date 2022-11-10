package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonRequest {
    private String lessonName;
    private Long courseId;

}
