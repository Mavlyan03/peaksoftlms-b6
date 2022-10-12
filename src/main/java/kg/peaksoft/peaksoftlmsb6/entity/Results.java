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
    @SequenceGenerator(name = "result_seq", sequenceName = "result_seq", allocationSize = 1)
    @GeneratedValue(generator = "result_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private LocalDate dateOfPass;

    private int percent;

    @OneToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Student student;

    @ManyToOne(cascade = {
            PERSIST,
            MERGE,
            REFRESH,
            DETACH})
    private Test test;

}
