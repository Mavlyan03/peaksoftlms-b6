package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String taskName;
    private List<ContentResponse> contentResponses;

    public TaskResponse(Long id, String taskName) {
        this.id = id;
        this.taskName = taskName;
    }
}
