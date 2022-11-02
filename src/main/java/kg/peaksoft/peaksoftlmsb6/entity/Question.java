package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.entity.enums.QuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "questions")
@Setter
@Getter
@NoArgsConstructor
public class Question {

    @Id
    @SequenceGenerator(name = "question_seq", sequenceName = "question_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "question_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @OneToMany(cascade = ALL, orphanRemoval = true)
    private List<Option> options;

    public void addOption(Option option) {
        if(this.options == null) {
            this.options = new ArrayList<>();
        }
        this.options.add(option);
    }
}
