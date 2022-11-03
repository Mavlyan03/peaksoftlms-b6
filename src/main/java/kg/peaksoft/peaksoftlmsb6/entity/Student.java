package kg.peaksoft.peaksoftlmsb6.entity;

import com.poiji.annotation.*;
import kg.peaksoft.peaksoftlmsb6.dto.request.StudentRequest;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@ExcelSheet("Students")
public class Student {


    @Id
    @SequenceGenerator(name = "student_seq", sequenceName = "student_seq", allocationSize = 1 , initialValue = 2)
    @GeneratedValue(generator = "student_seq", strategy = GenerationType.SEQUENCE)
    @ExcelRow
    private Long id;

    @ExcelCell(0)
    private String firstName;

    @ExcelCell(1)
    private String lastName;

    @ExcelCell(2)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @ExcelCell(3)
    private StudyFormat studyFormat;

    @ManyToOne(cascade = {
            MERGE,
            DETACH})
    @ExcelCell(3)
    private Group group;

    @OneToOne(cascade = {
            DETACH,
            MERGE})
    private Results results;

    @OneToOne(cascade = ALL)
    @ExcelCell(3)
    private User user;

    public Student(StudentRequest request){
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        this.studyFormat = request.getStudyFormat();
        this.phoneNumber = request.getPhoneNumber();
        User user1 = new User();
        user1.setEmail(request.getEmail());
        user1.setPassword(request.getPassword());
        user1.setRole(Role.STUDENT);
        this.user = user1;
    }



}
