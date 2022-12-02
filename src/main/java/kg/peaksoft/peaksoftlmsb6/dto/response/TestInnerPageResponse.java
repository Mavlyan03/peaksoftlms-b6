package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestInnerPageResponse {
    private Long id;
    private String name;
    private List<QuestionResponse> questions;

    public TestInnerPageResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}