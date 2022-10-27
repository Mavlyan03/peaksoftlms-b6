package kg.peaksoft.peaksoftlmsb6.entity;

import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1 , initialValue = 2)
    @GeneratedValue(generator = "student_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    @Enumerated(EnumType.STRING)
    private StudyFormat studyFormat;

    @ManyToOne(cascade = {
            MERGE,
            DETACH})
    private Group group;

    @OneToOne(cascade = ALL)
    private User user;

    public Student(StudentRequest request){
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.studyFormat = request.getStudyFormat();
        this.phoneNumber = request.getPhoneNumber();
        this.email = request.getEmail();
        User user1 = new User();
        user1.setEmail(request.getEmail());
        user1.setPassword(request.getPassword());
        user1.setRole(Role.STUDENT);
        this.user = user1;
    }

}
