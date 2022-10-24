package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignInstructorRequest {
    private Long instructorId;
    private Long courseId;
}