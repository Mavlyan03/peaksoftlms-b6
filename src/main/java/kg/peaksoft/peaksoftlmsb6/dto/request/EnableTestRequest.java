package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnableTestRequest {
    private Long testId;
    private boolean isEnable;
}
