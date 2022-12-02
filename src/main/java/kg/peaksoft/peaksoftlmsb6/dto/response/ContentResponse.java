package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ContentResponse {
    private Long id;
    private String contentName;
    private ContentFormat contentFormat;
    private String contentValue;
}
