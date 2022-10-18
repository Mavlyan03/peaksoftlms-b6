package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @SequenceGenerator(name = "course_seq", sequenceName = "course_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "course_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String courseName;

    @Column(length = 10000)
    private String courseDescription;

    private LocalDate dateOfStart;

    private String courseImage;

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            REFRESH})
    private Group group;

    @ManyToMany(cascade = {
            DETACH,
            MERGE,
            PERSIST,
            REFRESH}, mappedBy = "courses")
    private List<Instructor> instructors;

    @OneToMany(cascade = ALL, mappedBy = "course")
    private List<Lesson> lessons;
}
