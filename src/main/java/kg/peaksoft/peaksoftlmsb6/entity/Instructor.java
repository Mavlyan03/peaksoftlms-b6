package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.InstructorRequest;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.exception.NotFoundException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
public class Instructor {

    @Id
    @SequenceGenerator(name = "instructor_seq", sequenceName = "instructor_seq", allocationSize = 1,initialValue = 2)
    @GeneratedValue(generator = "instructor_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String specialization;

    public Instructor(InstructorRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.phoneNumber = request.getPhoneNumber();
        this.specialization = request.getSpecialization();
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(Role.INSTRUCTOR);
        this.user = user;
    }

    @ManyToMany(cascade = {
            MERGE,
            REFRESH,
            DETACH})
    private List<Course> courses;

    @OneToOne(cascade = ALL)
    private User user;
}
