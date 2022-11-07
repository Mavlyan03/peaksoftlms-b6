package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.LinkRequest;
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
    @SequenceGenerator(name = "link_seq", sequenceName = "link_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "link_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String linkText;

    private String link;

    @OneToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Lesson lesson;

    public Link(LinkRequest request) {
        this.linkText = request.getLinkText();
        this.link = request.getLink();
    }
}
