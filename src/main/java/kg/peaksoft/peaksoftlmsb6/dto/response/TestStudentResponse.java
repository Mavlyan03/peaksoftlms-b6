package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TestStudentResponse {
    private Long id;
    private String name;
    private List<QuestionStudentResponse> questions;

    public TestStudentResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}