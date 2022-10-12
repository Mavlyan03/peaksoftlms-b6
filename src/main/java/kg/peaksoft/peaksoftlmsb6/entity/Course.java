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
    @SequenceGenerator(name = "course_seq", sequenceName = "course_seq", allocationSize = 1)
    @GeneratedValue(generator = "course_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String courseName;

    @Column
    private String courseDescription;

    @Column
    private LocalDate dateOfStart;

    @Column
    private String courseImage;

    @ManyToOne(cascade = {
            DETACH,
            MERGE,
            PERSIST,
            REFRESH})
    private Group group;

    @ManyToMany(cascade = ALL, mappedBy = "courses")
    private List<Instructor> instructors;

    @OneToMany(cascade = ALL, mappedBy = "course")
    private List<Lesson> lessons;
}
