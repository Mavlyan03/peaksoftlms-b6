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

    private String groupName;

    @Column(length = 100000)
    private String groupDescription;

    private LocalDate dateOfStart;

    private String groupImage;

    @OneToMany(cascade = {
            MERGE,
            REFRESH,
            DETACH}, mappedBy = "group")
    private List<Course> courses;

    @OneToMany(cascade = ALL, mappedBy = "group")
    private List<Student> students;

}
