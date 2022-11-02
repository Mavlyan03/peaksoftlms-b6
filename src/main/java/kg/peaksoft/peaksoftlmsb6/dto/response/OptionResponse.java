package kg.peaksoft.peaksoftlmsb6.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OptionResponse {
    private Long id;
    private String optionValue;
    private String isTrue;

    public OptionResponse(Long id, String optionValue) {
        this.id = id;
        this.optionValue = optionValue;
    }
}
