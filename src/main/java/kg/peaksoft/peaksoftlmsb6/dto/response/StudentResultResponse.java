package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class StudentResultResponse {
    private String totalPoint;
    private Map<QuestionResponse, List<OptionResponse>> points;
}
