package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "videos")
@Setter
@Getter
@NoArgsConstructor
public class Video {

    @Id
    @SequenceGenerator(name = "video_seq", sequenceName = "video_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "video_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String videoName;

    @Column(length = 100000)
    private String videoDescription;

    @Column
    private String videoLink;

    @OneToOne(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH})
    private Lesson lesson;
}
