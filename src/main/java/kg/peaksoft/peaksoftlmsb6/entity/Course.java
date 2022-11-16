package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.CourseRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @SequenceGenerator(name = "course_seq", sequenceName = "course_seq", allocationSize = 1, initialValue = 10)
    @GeneratedValue(generator = "course_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String courseName;

    @Column(length = 10000)
    private String courseDescription;

    private LocalDate dateOfStart;

    private String courseImage;

    @ManyToMany(cascade = {
            DETACH,
            MERGE,
            REFRESH}, mappedBy = "courses")
    private List<Group> group;

    @ManyToMany(cascade = {
            DETACH,
            MERGE,
            PERSIST,
            REFRESH}, mappedBy = "courses")
    private List<Instructor> instructors;

    @OneToMany(cascade = ALL, mappedBy = "course")
    private List<Lesson> lessons;

    public void addInstructor(Instructor instructor ) {
        if(this.instructors == null) {
            this.instructors = new ArrayList<>();
        }
        this.instructors.add(instructor);
    }

    public void addGroup(Group group) {
        if(this.group == null) {
            this.group = new ArrayList<>();
        }
        this.group.add(group);
    }

    public void addLesson(Lesson lesson) {
        if(this.lessons == null) {
            this.lessons = new ArrayList<>();
        }
        this.lessons.add(lesson);
    }

    public Course(CourseRequest request) {
        this.courseName = request.getCourseName();
        this.courseDescription = request.getDescription();
        this.dateOfStart = request.getDateOfStart();
        this.courseImage = request.getImage();
    }
}
