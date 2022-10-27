package kg.peaksoft.peaksoftlmsb6.repository;

import kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse;
import kg.peaksoft.peaksoftlmsb6.entity.Student;
import kg.peaksoft.peaksoftlmsb6.entity.enums.StudyFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "id," +
            "concat(firstName,' ',lastName)," +
            "group.groupName," +
            "studyFormat," +
            "phoneNumber," +
            "email) from Student")
    List<StudentResponse> getAllStudents();

    @Modifying
    @Transactional
    @Query("update Student set " +
            "firstName = :firstName, " +
            "lastName = :lastName, " +
            "studyFormat = :studyFormat, " +
            "phoneNumber = :phoneNumber, " +
            "email = :email," +
            "user.password = :password where id = :id")
    void update(@Param("id") Long id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("studyFormat") StudyFormat studyFormat,
                @Param("phoneNumber") String phoneNumber,
                @Param("email") String email,
                @Param("password") String password);



    @Query("select new kg.peaksoft.peaksoftlmsb6.dto.response.StudentResponse(" +
            "s.id," +
            "concat(s.firstName,' ', s.lastName)," +
            "s.group.groupName," +
            "s.studyFormat," +
            "s.phoneNumber," +
            "s.email)from Student s where s.id = ?1")
    StudentResponse getStudent(Long id);
}
