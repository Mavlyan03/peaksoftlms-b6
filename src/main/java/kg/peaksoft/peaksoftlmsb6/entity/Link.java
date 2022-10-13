package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "links")
@Getter
@Setter
@NoArgsConstructor
public class Link {


    @Id
    @SequenceGenerator(name = "link_seq", sequenceName = "link_seq", allocationSize = 1)
    @GeneratedValue(generator = "link_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String linkText;

    private String link;

    @OneToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Lesson lesson;
}
