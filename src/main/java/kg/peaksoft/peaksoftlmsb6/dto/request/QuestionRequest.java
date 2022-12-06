package kg.peaksoft.peaksoftlmsb6.dto.request;

import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionRequest {
    private String question;
    private QuestionType questionType;
    private List<OptionRequest> options;
}
