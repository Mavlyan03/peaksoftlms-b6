package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PresentationResponse {
    Long id;
    private String presentationName;
    private String description;
    private String presentationLink;
}
