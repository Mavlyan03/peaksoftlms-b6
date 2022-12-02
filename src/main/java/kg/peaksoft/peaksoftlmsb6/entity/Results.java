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
    @SequenceGenerator(name = "result_seq", sequenceName = "result_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "result_seq", strategy = GenerationType.SEQUENCE)
    private Long id;
    private Integer amountOfCorrectAnswers;
    private Integer amountOfIncorrectAnswers;
    private LocalDate dateOfPass;

    private Integer percent;

    @OneToOne(cascade = {
            MERGE,
            DETACH})
    private Student student;

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH})
    private Test test;

    public Results(Test test, LocalDate localDate, Integer percent, Student student) {
        this.test = test;
        this.dateOfPass = localDate;
        this.percent = percent;
        this.student = student;
    }

    public Results(Test test, Integer amountOfCorrectAnswers, Integer amountOfIncorrectAnswers, LocalDate dateOfPass, Integer percent, Student student) {
        this.test = test;
        this.amountOfCorrectAnswers = amountOfCorrectAnswers;
        this.amountOfIncorrectAnswers = amountOfIncorrectAnswers;
        this.dateOfPass = dateOfPass;
        this.percent = percent;
        this.student = student;
    }
}
