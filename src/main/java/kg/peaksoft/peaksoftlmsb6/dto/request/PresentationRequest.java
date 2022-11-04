package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationRequest {
    private Long lessonId;
    private String presentationName;
    private String description;
    private String presentationLink;
}
