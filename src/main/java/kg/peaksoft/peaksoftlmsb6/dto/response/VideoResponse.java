package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VideoResponse {
    private Long id;
    private String videoName;
    private String description;
    private String link;
}
