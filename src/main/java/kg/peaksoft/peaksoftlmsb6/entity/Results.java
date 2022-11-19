package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "results")
@Getter
@Setter
@NoArgsConstructor
public class Results {

    @Id
    @SequenceGenerator(name = "result_seq", sequenceName = "result_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "result_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private LocalDate dateOfPass;

    private Integer amountOfCorrectAnswers;

    private Integer amountOfIncorrectAnswers;

    private int percent;

    @OneToOne(cascade = {
            MERGE,
            DETACH})
    private Student student;

    @ManyToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH})
    private Test test;


}
