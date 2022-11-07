package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkRequest {
    private Long lessonId;
    private String linkText;
    private String link;
}
