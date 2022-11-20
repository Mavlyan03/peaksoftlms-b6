package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.LessonRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
public class Lesson {

    @Id
    @SequenceGenerator(name = "lesson_seq", sequenceName = "lesson_seq", allocationSize = 1,initialValue = 10)
    @GeneratedValue(generator = "lesson_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String lessonName;

    @OneToOne(cascade = ALL, mappedBy = "lesson")
    private Video video;

    @OneToOne(cascade = ALL, mappedBy = "lesson")
    private Presentation presentation;

    @OneToOne(cascade = ALL, mappedBy = "lesson")
    private Task task;

    @OneToOne(cascade = ALL, mappedBy = "lesson")
    private Link link;

    @OneToOne(cascade = ALL, mappedBy = "lesson")
    private Test test;

    public Lesson(LessonRequest request) {
        this.lessonName = request.getLessonName();
        this.video = getVideo();
        this.presentation = getPresentation();
        this.task = getTask();
        this.link = getLink();
        this.test = getTest();
    }

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH})
    private Course course;
}
