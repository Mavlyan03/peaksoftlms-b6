package kg.peaksoft.peaksoftlmsb6.dto.response;

import java.util.List;

public class TestResponse {
    private Long id;
    private String testName;
    private List<QuestionResponse> questionResponseList;

    public TestResponse(Long id, String testName) {
        this.id = id;
        this.testName = testName;
    }
}
