package kg.peaksoft.peaksoftlmsb6.entity;

import com.poiji.annotation.ExcelCellName;
import kg.peaksoft.peaksoftlmsb6.dto.request.GroupRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1, initialValue = 2)
    @GeneratedValue(generator = "group_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String groupName;

    @Column(length = 100000)
    private String groupDescription;

    private LocalDate dateOfStart;

    private String groupImage;

    @ManyToMany(cascade = {
            MERGE,
            REFRESH,
            DETACH}, mappedBy = "group")
    private List<Course> courses;

    @OneToMany(cascade = ALL, mappedBy = "group")
    private List<Student> students;

    public void addStudents(Student student) {
        if(this.students == null) {
            this.students = new ArrayList<>();
        }
        this.students.add(student);
    }

    public void addCourse(Course course) {
        if(this.courses == null) {
            this.courses = new ArrayList<>();
        }
        this.courses.add(course);
    }


    public Group(GroupRequest request) {
        this.groupName = request.getGroupName();
        this.groupDescription = request.getDescription();
        this.dateOfStart = request.getDateOfStart();
        this.groupImage = request.getImage();
    }

}
