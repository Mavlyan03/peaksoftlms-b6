package kg.peaksoft.peaksoftlmsb6.entity;

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
    @SequenceGenerator(name = "lesson_seq", sequenceName = "lesson_seq", allocationSize = 10)
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

    @OneToOne(cascade = ALL, mappedBy = "lesson", orphanRemoval = true)
    private Test test;
    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH})
    private Course course;
}
