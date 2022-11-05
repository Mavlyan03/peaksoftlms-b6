package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.entity.enums.ContentFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
public class Content {

    @Id
    @SequenceGenerator(name = "content-seq", sequenceName = "content_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "content_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String contentName;

    @Enumerated(EnumType.STRING)
    private ContentFormat contentFormat;

    private String contentValue;

    @ManyToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Task task;

    public Content(String contentName, ContentFormat contentFormat, String contentValue) {
        this.contentName = contentName;
        this.contentFormat = contentFormat;
        this.contentValue = contentValue;
    }
}
