package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.VideoRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Video(VideoRequest request) {
        this.videoName = request.getVideoName();
        this.videoDescription = request.getDescription();
        this.videoLink = request.getLink();
    }
}
