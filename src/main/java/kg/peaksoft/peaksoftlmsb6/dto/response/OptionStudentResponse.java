package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.entity.Option;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionStudentResponse {
    private Long id;
    private String optionValue;

    public OptionStudentResponse(Option option) {
        this.id = option.getId();
        this.optionValue = option.getOptionValue();
    }
}
