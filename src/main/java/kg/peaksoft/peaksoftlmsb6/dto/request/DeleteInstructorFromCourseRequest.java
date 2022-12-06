package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteInstructorFromCourseRequest {
    private Long instructorId;
    private Long courseId;
}
