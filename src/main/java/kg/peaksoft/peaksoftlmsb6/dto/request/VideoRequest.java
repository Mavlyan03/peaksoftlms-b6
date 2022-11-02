package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequest {
    private Long lessonId;
    private String videoName;
    private String description;
    private String link;
}
