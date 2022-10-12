package kg.peaksoft.peaksoftlmsb6.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1)
    @GeneratedValue(generator = "group_gen", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String groupName;

    @Column
    private String groupDescription;

    @Column
    private LocalDate dateOfStart;

    @Column
    private String groupImage;

    @OneToMany(cascade = {
            MERGE,
            PERSIST,
            REFRESH,
            DETACH}, mappedBy = "group")
    private List<Course> courses;

    @OneToMany(cascade = ALL, mappedBy = "group")
    private List<Student> students;

}
