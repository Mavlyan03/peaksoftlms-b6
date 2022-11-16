package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

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

    @ManyToOne(cascade = {
            DETACH,
            REFRESH,
            MERGE,
            PERSIST})
    private Question question;
}
