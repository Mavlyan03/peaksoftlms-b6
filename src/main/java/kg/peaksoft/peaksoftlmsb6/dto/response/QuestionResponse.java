package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.Question;
import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String question;
    private QuestionType questionType;
    private List<OptionResponse> optionResponses;

    public QuestionResponse(Long id, String question, QuestionType questionType) {
        this.id = id;
        this.question = question;
        this.questionType = questionType;
    }

    public QuestionResponse(Question question) {
        this.id = question.getId();
        this.question = question.getQuestion();
        this.questionType = question.getQuestionType();
    }
}
