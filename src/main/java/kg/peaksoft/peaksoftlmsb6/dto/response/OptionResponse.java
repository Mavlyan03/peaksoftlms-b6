package kg.peaksoft.peaksoftlmsb6.dto.response;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import kg.peaksoft.peaksoftlmsb6.entity.Option;
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

    public OptionResponse(OptionRequest option, Long id) {
        this.id = id;
        this.optionValue = option.getOption();
    }
    public OptionResponse(Option option) {
        this.id = option.getId();
        this.optionValue = option.getOptionValue();
    }
}
