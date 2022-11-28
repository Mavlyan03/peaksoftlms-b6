package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContentRequest {
    private Long taskId;
    private String contentName;
    private ContentFormat contentFormat;
    private String contentValue;
}
