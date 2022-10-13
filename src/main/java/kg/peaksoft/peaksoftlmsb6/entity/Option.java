package kg.peaksoft.peaksoftlmsb6.entity;

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
    @SequenceGenerator(name = "option_seq", sequenceName = "option_seq", allocationSize = 1)
    @GeneratedValue(generator = "option_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String optionValue;

    private Boolean isTrue;
}
