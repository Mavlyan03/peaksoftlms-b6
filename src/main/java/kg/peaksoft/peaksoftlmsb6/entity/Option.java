package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.OptionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "options")
@Getter
@Setter
@NoArgsConstructor
public class Option {

    @Id
    @SequenceGenerator(name = "option_seq", sequenceName = "option_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "option_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String optionValue;

    private Boolean isTrue;

    public Option(String option, Boolean isTrue) {
        this.optionValue = option;
        this.isTrue = isTrue;
    }

    public Option(OptionRequest optionRequest) {
        this.optionValue = optionRequest.getOption();
        this.isTrue = optionRequest.getIsTrue();
    }
}
