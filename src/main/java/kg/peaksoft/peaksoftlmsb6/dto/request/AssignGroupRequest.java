package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NotBlank
public class AssignGroupRequest {
    private Long groupId;
    private Long courseId;
}
