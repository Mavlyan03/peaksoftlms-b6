package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "presentations")
@Getter
@Setter
@NoArgsConstructor
public class Presentation {
    @Id
    @SequenceGenerator(name = "presentation_seq", sequenceName = "presentation_seq", allocationSize = 1)
    @GeneratedValue(generator = "presentation_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String presentationName;

    @Column
    private String presentationDescription;

    @Column
    private String presentationLink;
    @OneToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Lesson lesson;
}
