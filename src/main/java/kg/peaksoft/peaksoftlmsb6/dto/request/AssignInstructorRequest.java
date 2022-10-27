package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NotBlank
public class AssignInstructorRequest {
    private Long instructorId;
    private Long courseId;
}