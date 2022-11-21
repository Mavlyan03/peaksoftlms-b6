package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.Results;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResultResponse {
    private Long id;
    private String studentFullName;
    private Integer amountOfCorrectAnswers;
    private Integer amountOfIncorrectAnswers;
    private Integer point;

    public ResultResponse(Results results) {
        this.id = results.getId();
        this.studentFullName = results.getStudent().getFirstName() + " " + results.getStudent().getLastName();
        this.amountOfCorrectAnswers = results.getAmountOfCorrectAnswers();
        this.amountOfIncorrectAnswers = results.getAmountOfIncorrectAnswers();
        this.point = results.getPercent();
    }

    public ResultResponse(Integer amountOfCorrectAnswers) {
        this.amountOfCorrectAnswers = amountOfCorrectAnswers;
    }

}
