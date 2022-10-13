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

    private String presentationName;

    @Column(length = 10000)
    private String presentationDescription;

    private String presentationLink;

    @OneToOne(cascade = {
            MERGE,
            REFRESH,
            DETACH})
    private Lesson lesson;
}
