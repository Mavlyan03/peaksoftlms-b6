package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionResponse {
    private Long id;
    private String optionValue;

    public OptionResponse(Long id, String optionValue) {
        this.id = id;
        this.optionValue = optionValue;
    }
}
