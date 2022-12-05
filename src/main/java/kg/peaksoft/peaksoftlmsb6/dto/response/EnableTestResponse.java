package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnableTestResponse {
    private Long testId;
    private Integer amountOfAnswers;

    public EnableTestResponse(Long id, Integer amountOfAnswers) {
        this.testId = id;
        this.amountOfAnswers = amountOfAnswers;
    }
}
