package kg.peaksoft.peaksoftlmsb6.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTaskRequest {
    private String taskName;
    private List<ContentRequest> contentRequests;
}
